package com.github.frapontillo.pulse.rx;

import rx.Subscriber;
import rx.observers.SafeSubscriber;

/**
 * Generic stream {@link SafeSubscriber} that can be used for Pulse tasks that simply want to
 * handle the {#link Subscriber#onNext} event.
 * <p>
 * Default actions are:
 * <ul>
 * <li>when completed, notifies the completion downstream</li>
 * <li>in case of an error, it prints the stacktrace and notifies the error</li>
 * <li>for each item needing processing, calls a custom method that has to be implemented</li>
 * </ul>
 *
 * @author Francesco Pontillo
 */
public abstract class PulseSubscriber<T> extends SafeSubscriber<T> {

    public PulseSubscriber(Subscriber<? super T> actual) {
        super(new Subscriber<T>() {
            @Override public void onCompleted() {
                actual.onCompleted();
            }

            @Override public void onError(Throwable e) {
                e.printStackTrace();
                actual.onError(e);
            }

            @Override public void onNext(T t) {
                onNext(t);
            }
        });
    }

    public abstract void onNext(T t);
}

