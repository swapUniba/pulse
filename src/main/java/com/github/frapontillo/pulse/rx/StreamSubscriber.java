package com.github.frapontillo.pulse.rx;

import org.apache.logging.log4j.Logger;
import rx.Subscriber;

/**
 * @author Francesco Pontillo
 */
public class StreamSubscriber extends Subscriber<Object> {

    private SubscriptionGroupLatch allSubscriptions;
    private Logger logger;

    public StreamSubscriber(SubscriptionGroupLatch allSubscriptions, Logger logger) {
        this.allSubscriptions = allSubscriptions;
        this.logger = logger;
    }

    @Override public void onCompleted() {
        logger.debug("EXECUTION: COMPLETED");
        allSubscriptions.countDown();
    }

    @Override public void onError(Throwable e) {
        logger.error("EXECUTION: ERRORED", e);
        allSubscriptions.countDown();
    }

    @Override public void onNext(Object o) {
        logger.debug(o.toString());
    }
}
