package com.github.frapontillo.pulse.rx;

import rx.Observable;

/**
 * Utility RxJava methods.
 *
 * @author Francesco Pontillo
 */
public class RxUtil {

    /**
     * Custom {@link rx.Observable.Transformer} that flattens a sequence of {@link Observable} in a
     * single {@link Observable}.
     *
     * @param <T> Type parameter of the input/output object.
     *
     * @return {@link rx.Observable.Transformer} to be applied on sequences of {@link Observable}s.
     */
    public static <T> Observable.Transformer<Iterable<T>, T> flatten() {
        return observable -> observable.flatMapIterable(x -> x);
    }

}
