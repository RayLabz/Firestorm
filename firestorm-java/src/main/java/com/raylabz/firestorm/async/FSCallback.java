//package com.raylabz.firestorm.async;
//
//import com.google.api.core.ApiFutureCallback;
//import com.google.api.core.ApiFutures;
//
//public abstract class FSCallback<ResultType> {
//
//    private final ApiFutureCallback<ResultType> futureCallback;
//
//    public FSCallback(FSFuture<ResultType> future) {
//        futureCallback = new ApiFutureCallback<ResultType>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//                catchError(throwable);
//            }
//
//            @Override
//            public void onSuccess(ResultType resultType) {
//                then(resultType);
//            }
//        };
//        ApiFutures.addCallback(future.getAPIFuture(), futureCallback);
//    }
//
//    public abstract void then(ResultType result);
//
//    public abstract void catchError(Throwable t);
//
//}
