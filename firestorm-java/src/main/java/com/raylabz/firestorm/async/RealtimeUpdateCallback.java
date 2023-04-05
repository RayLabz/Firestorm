package com.raylabz.firestorm.async;

public interface RealtimeUpdateCallback<T> {

    void onUpdate(T data);

    void onError(Throwable t);

}
