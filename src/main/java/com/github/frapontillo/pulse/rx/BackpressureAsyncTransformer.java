package com.github.frapontillo.pulse.rx;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Custom {@link rx.Observable.Transformer} that makes the {@link Observable} it is applied on:
 * <ul>
 * <li>subscribe on the computation thread</li>
 * <li>buffer incoming items on backpressure</li>
 * <li>observe on the I/O thread</li>
 * </ul>
 *
 * @author Francesco Pontillo
 */
public class BackpressureAsyncTransformer<T> implements Observable.Transformer<T, T> {
    @Override public Observable<T> call(Observable<T> rObservable) {
        return rObservable
                // don't block waiting on the work in downstream schedulers before generating and
                // processing more values
                .subscribeOn(Schedulers.computation())
                // when downstream observers can't keep up, buffer the generated values
                .onBackpressureBuffer()
                // observer on an IO thread
                .observeOn(Schedulers.io());
    }
}
