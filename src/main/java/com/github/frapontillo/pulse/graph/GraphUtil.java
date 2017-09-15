package com.github.frapontillo.pulse.graph;

import com.github.frapontillo.pulse.util.PulseLogger;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Graph configuration utilities.
 *
 * @author Francesco Pontillo
 */
public class GraphUtil {
    private final static Logger logger = PulseLogger.getLogger(GraphUtil.class);
    private final static Gson gson = new Gson();

    public static Graph readGraphFromString(String string) {
        return gson.fromJson(string, Graph.class).buildGraph();
    }

    /**
     * Read a {@link Graph} from a file.
     *
     * @param filePath The path of the file.
     *
     * @return The {@link Graph} read from the file.
     * @throws FileNotFoundException if the input path doesn't match any file on the file system.
     */
    public static Graph readGraphFromPath(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);
        return gson.fromJson(fileReader, Graph.class).buildGraph();
    }

    /**
     * Read a {@link Graph} from a file among the resources in the classpath.
     *
     * @param resourceName       The name of the resource file.
     * @param resourceClassOwner The {@link Class} in the same classpath as the resource.
     *
     * @return The {@link Graph} read from the resource file.
     */
    public static Graph readGraphFromResources(String resourceName, Class resourceClassOwner) {
        InputStream configInput;
        try {
            configInput = resourceClassOwner.getClassLoader().getResourceAsStream(resourceName);
            InputStreamReader inputStreamReader = new InputStreamReader(configInput);
            return gson.fromJson(inputStreamReader, Graph.class).buildGraph();
        } catch (Exception exc) {
            logger.error("Can't find graph file named {}.", resourceName);
            exc.printStackTrace();
        }
        return null;
    }
}
