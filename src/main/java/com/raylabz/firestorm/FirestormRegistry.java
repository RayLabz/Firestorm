package com.raylabz.firestorm;

import com.raylabz.firestorm.exception.FirestormObjectException;

import java.util.HashSet;

/**
 * Contains a registry of the registered classes that are known to be valid.
 *  @author Nicos Kasenides
 *  @version 1.1.0
 */
public class FirestormRegistry {

    /**
     * Stores the registered classes.
     */
    private static final HashSet<Class<?>> REGISTERED_CLASSES = new HashSet<>();

    /**
     * Checks a class for a valid structure and registers it.
     * @param aClass The class to register.
     * @throws FirestormObjectException Thrown when the class provided does not have a valid structure.
     */
    static void register(Class<?> aClass) throws FirestormObjectException {
        Reflector.checkClass(aClass);
        REGISTERED_CLASSES.add(aClass);
    }

    /**
     * Checks if a class is registered.
     * @param aClass The class to check.
     * @return Returns true if the class provided is registered (and valid), false otherwise.
     */
    static boolean isRegistered(final Class<?> aClass) {
        return REGISTERED_CLASSES.contains(aClass);
    }

}
