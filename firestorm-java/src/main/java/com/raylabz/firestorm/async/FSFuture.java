package com.raylabz.firestorm.async;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.exception.FirestormException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents an operation that will be completed in the future.
 * @param <ResultType> The result type.
 */
public class FSFuture<ResultType> {

    public enum Status {
        IN_PROGRESS,
        CANCELLED,
        COMPLETED,
    }

    private final ApiFuture<ResultType> future;
    private ApiFutureCallback<ResultType> jointCallback = null;
    private SuccessCallback<ResultType> successCallback = null;
    private FailureCallback failureCallback = null;

    FSFuture(ApiFuture<ResultType> future) {
        this.future = future;
    }

    /**
     * Waits until the operation is finished before proceeding.
     * @param onFailure A callback to handle a possible failure.
     * @return Returns {@link ResultType}.
     */
    public ResultType now(FailureCallback onFailure) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            onFailure.execute(e);
            return null;
        }
    }

    /**
     * Waits until the operation is finished before proceeding.
     * @return Returns {@link ResultType}.
     */
    public ResultType now() {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
           throw new FirestormException(e);
        }
    }

    /**
     * Waits for a specified amount of time for the operation to complete before proceeding. If the operation is not completed
     * by the specified timeout, it will be dropped.
     * @param timeout The timeout.
     * @param timeUnit The time unit.
     * @return Returns {@link ResultType}.
     */
    public ResultType waitFor(long timeout, TimeUnit timeUnit) {
        try {
            return future.get(timeout, timeUnit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return null;
        }
    }

    /**
     * Waits for a specified amount of time for the operation to complete before proceeding. If the operation is not completed
     * by the specified timeout, it will be dropped.
     * @param timeout The timeout.
     * @param timeUnit The time unit.
     * @param callback A callback to handle a possible failure.
     * @return Returns {@link ResultType}.
     */
    public ResultType waitFor(long timeout, TimeUnit timeUnit, FailureCallback callback) {
        try {
            return future.get(timeout, timeUnit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            callback.execute(e);
            return null;
        }
    }

    /**
     * Returns the status of the operation.
     * @return Returns {@link Status}.
     */
    public Status getStatus() {
        if (future.isCancelled()) {
            return Status.CANCELLED;
        }
        else if (future.isDone()) {
            return Status.COMPLETED;
        }
        else {
            return Status.IN_PROGRESS;
        }
    }

    public FSFuture<ResultType> then(SuccessCallback<ResultType> callback) {
        this.successCallback = callback;
        updateJointCallback();
        return this;
    }

    public FSFuture<ResultType> onError(FailureCallback callback) {
        this.failureCallback = callback;
        updateJointCallback();
        return this;
    }

    public void run() {
        ApiFutures.addCallback(future, jointCallback, Firestorm.getSelectedExecutor());
    }

    private void updateJointCallback() {
        this.jointCallback = new ApiFutureCallback<ResultType>() {
            @Override
            public void onFailure(Throwable throwable) {
                if (failureCallback != null) {
                    failureCallback.execute(throwable);
                }
            }

            @Override
            public void onSuccess(ResultType resultType) {
                if (successCallback != null) {
                    successCallback.execute(resultType);
                }
            }
        };
    }

    ApiFuture<ResultType> getAPIFuture() {
        return future;
    }

    /**
     * Creates FSFuture from APIFuture.
     * @param apiFuture The API future
     * @return Returns an {@link ResultType}
     * @param <ResultType> The result type.
     */
    public static <ResultType> FSFuture<ResultType> fromAPIFuture(ApiFuture<ResultType> apiFuture) {
        return new FSFuture<>(apiFuture);
    }

}
