package com.github.frapontillo.pulse.cli;

import com.beust.jcommander.JCommander;
import com.github.frapontillo.pulse.graph.Graph;
import com.github.frapontillo.pulse.graph.GraphUtil;
import com.github.frapontillo.pulse.graph.Node;
import com.github.frapontillo.pulse.rx.StreamSubscriber;
import com.github.frapontillo.pulse.rx.SubscriptionGroupLatch;
import com.github.frapontillo.pulse.spi.IPlugin;
import com.github.frapontillo.pulse.spi.PluginProvider;
import com.github.frapontillo.pulse.util.FileUtil;
import com.github.frapontillo.pulse.util.ProcessUtil;
import com.github.frapontillo.pulse.util.PulseLogger;
import org.apache.logging.log4j.Logger;
import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Dynamic pipeline builder from a JSON configuration file.
 * <p>
 * Because Blade Runner. Get it? HA!
 *
 * @author Francesco Pontillo
 */
public abstract class AbstractBlade<Parameters extends BladeParameters> {
    private final static Logger logger = PulseLogger.getLogger(AbstractBlade.class);
    private HashMap<String, Observable> observableMap;
    private List<Observable<Object>> terminalObservables;

    public AbstractBlade(String args[]) throws ClassNotFoundException, FileNotFoundException {
        logger.info("Pulse Blade Runner started with PID {}.", ProcessUtil.getPid());

        Parameters parameters = getNewBladeParameters();
        new JCommander(parameters, args);

        // get the appropriate input stream (file or stdin)
        InputStream inputStream;
        if (parameters.hasFile()) {
            inputStream = FileUtil.readFileFromPathOrResource(parameters.getFile(), AbstractBlade.class);
            logger.debug("Reading configuration from file...");
        } else {
            inputStream = System.in;
            logger.debug("Reading configuration from standard input...");
        }

        // read line by line from the input stream
        Scanner scanner = new Scanner(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
        }
        logger.debug("Configuration read.");

        String config = sb.toString();
        Graph graph = GraphUtil.readGraphFromString(config);
        logger.debug("Graph built.");

        // possibly alter the graph
        alterGraph(graph, parameters);

        observableMap = new HashMap<>(graph.getNodes().size());
        terminalObservables = new ArrayList<>();

        // start from the root nodes and transform the Graph into Observables
        List<Node> rootNodes = graph.getRoots();

        logger.debug("Building Observables...");
        buildObservables(graph, rootNodes);
        logger.debug("Observables built.");
    }

    public static Logger getLogger() {
        return logger;
    }

    /**
     * Alter an already built {@link Graph}.
     * The default implementation does nothing. If you want to change its behaviour, you need to
     * override the method.
     *
     * @param graph      The {@link Graph} to wrap.
     * @param parameters The {@link BladeParameters} to use (they can specify the log, db and
     *                   project run).
     */
    protected void alterGraph(Graph graph, Parameters parameters) {
    }

    protected abstract Parameters getNewBladeParameters();

    /**
     * Merges all of the terminal {@link Observable}s, publishes them, subscribes to them and
     * eventually connect to them.
     *
     * @throws ClassNotFoundException If an {@link IPlugin} instance could not be found.
     */
    public void run() throws ClassNotFoundException {
        ConnectableObservable<Object> stream = Observable.merge(terminalObservables).publish();

        SubscriptionGroupLatch allSubscriptions = new SubscriptionGroupLatch(1);

        // subscribe to the connectable stream
        Subscription subscription =
                stream.subscribe(new StreamSubscriber(allSubscriptions, logger));

        allSubscriptions.setSubscriptions(subscription);

        stream.connect();
        logger.info("Starting process...");

        allSubscriptions.waitAllUnsubscribed();
        logger.info("Process completed.");
    }

    /**
     * Build all of the {@link Observable}s referenced by list of {@link Node}s.
     *
     * @param graph The {@link Graph} that holds the nodes.
     * @param nodes The {@link List} of {@link Node}s to build observables for.
     *
     * @throws ClassNotFoundException If an {@link IPlugin} instance could not be found.
     */
    private void buildObservables(Graph graph, List<Node> nodes) throws ClassNotFoundException {
        for (Node node : nodes) {
            buildObservable(graph, node);
        }
    }

    /**
     * Build an {@link Observable} for a {@link Node} in a given {@link Graph}.
     *
     * @param graph The {@link Graph} that holds the {@link Node}.
     * @param node  The {@link Node} to build the {@link Observable} for.
     *
     * @throws ClassNotFoundException If an {@link IPlugin} instance could not be found.
     */
    @SuppressWarnings("unchecked")
    private void buildObservable(Graph graph, Node node) throws ClassNotFoundException {
        // if the observableMap already has an Observable for the current Node, it was already
        // built before
        if (observableMap.get(node.getName()) != null) {
            return;
        }
        logger.debug("Building node {} with plugin {}...", node.getName(), node.getPlugin());
        // if the node has previous nodes bound to it
        List<Node> previousNodes = node.getPrev();
        List<Observable> previousObservables = null;
        if (node.hasPrev()) {
            // build the previous nodes if they're not already built
            buildObservables(graph, previousNodes);
            // get all the previous Observables from the observableMap
            previousObservables =
                    previousNodes.stream().map(prevNode -> observableMap.get(prevNode.getName()))
                                 .collect(Collectors.toList());
        }
        // at this point we know for sure that the previous nodes of node are already built and
        // in the observableMap

        // the previous observable creation may have already created the observable we're trying
        // to build: skip it!
        if (observableMap.get(node.getName()) != null) {
            return;
        }

        IPlugin plugin = PluginProvider.getPlugin(node.getPlugin());
        plugin.setProcessInfo(graph.getProcess());
        plugin.setJobName(node.getName());
        // use the previous observables in the plugin
        Observable observable = plugin.process(node.getConfig(), previousObservables);
        logger.info("Node {} built with plugin {}.", node.getName(), plugin.getName());

        // if the node has n > 1 exit points, cache the emitted values so not to repeat its
        // execution n - 1 more times
        if (node.getNext() != null && node.getNext().size() > 1) {
            observable = observable.cache();
        }

        // save the new observable into the map
        observableMap.put(node.getName(), observable);

        // if the node has no next nodes, save it among the terminal nodes so to subscribe on it
        if (!node.hasNext()) {
            terminalObservables.add(observable);
        } else {
            buildObservables(graph, node.getNext());
        }
    }

}
