package com.raylabz.firestorm;

import com.raylabz.firestorm.exception.ClassRegistrationException;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.exception.FirestormObjectException;

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

    /**
     * Registers a class.
     * @param aClass The class to register.
     * @throws FirestormException Thrown when the class cannot be registered.
     */
    public static void register(Class<?> aClass) throws FirestormException {
        try {
            FirestormRegistry.register(aClass);
        } catch (FirestormObjectException e) {
            throw new FirestormException(e.getMessage());
        }
    }

    /**
     * Checks if an object's class is registered.
     * @param object The object to check the class of.
     * @throws ClassRegistrationException Thrown when the object's class is not registered.
     */
    public static void checkRegistration(final Object object) throws ClassRegistrationException {
        if (!FirestormRegistry.isRegistered(object.getClass())) {
            throw new ClassRegistrationException(object.getClass());
        }
    }

    /**
     * Checks if a class is registered.
     * @param aClass The class to check.
     * @throws ClassRegistrationException Thrown when the class is not registered.
     */
    public static void checkRegistration(final Class<?> aClass) throws ClassRegistrationException {
        if (!FirestormRegistry.isRegistered(aClass)) {
            throw new ClassRegistrationException(aClass);
        }
    }

}
