package com.raylabz.firestorm;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Firestorm {

    /**
     * Returns a random RFC4122 ID
     * @return Returns a random string-based ID.
     */
    public static String randomID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static final int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors();

    private static final ExecutorService SINGLE_THREAD_EXECUTOR = Executors.newSingleThreadExecutor();

    private static final ExecutorService MULTI_THREAD_EXECUTOR = Executors.newFixedThreadPool(NUM_OF_THREADS);

    private static ExecutorService SELECTED_EXECUTOR = SINGLE_THREAD_EXECUTOR;

    /**
     * Enables multithreading support.
     */
    public static void enableMultithreading() {
        SELECTED_EXECUTOR = MULTI_THREAD_EXECUTOR;
    }

    /**
     * Disables multithreading support.
     */
    public static void disableMultithreading() {
        SELECTED_EXECUTOR = SINGLE_THREAD_EXECUTOR;
    }

    /**
     * Checks if multithreading is enabled.
     * @return Returns true if multithreading is enabled, false otherwise.
     */
    public static boolean isMultithreaded() {
        return SELECTED_EXECUTOR == MULTI_THREAD_EXECUTOR;
    }

    /**
     * Returns the selected executor.
     * @return Returns an {@link ExecutorService}.
     */
    public static ExecutorService getSelectedExecutor() {
        return SELECTED_EXECUTOR;
    }

}
