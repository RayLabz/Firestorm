package com.raylabz.firestorm;

import com.raylabz.firestorm.exception.FirestormObjectException;

import java.lang.reflect.Field;
import java.util.HashMap;
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
     * Stores the linked fields for each registered class.
     */
    private static final HashMap<Class<?>, HashSet<Field>> LINKED_FIELDS = new HashMap<>();

    /**
     * Stores the ID fields for each registered class.
     */
    private static final HashMap<Class<?>, Field> ID_FIELDS = new HashMap<>();

    /**
     * Checks a class for a valid structure and registers it.
     * @param aClass The class to register.
     * @throws FirestormObjectException Thrown when the class provided does not have a valid structure.
     */
    static void register(Class<?> aClass) throws FirestormObjectException {
        Reflector.checkClass(aClass);
        HashSet<Field> linkedFields = Reflector.getLinkedFields(aClass);
        LINKED_FIELDS.put(aClass, linkedFields);
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

    /**
     * Retrieves the linked fields of a class.
     * @param aClass The class (must be registered).
     * @return Returns a HashSet of Field.
     * @throws FirestormObjectException Thrown when the class is not registered.
     */
    static HashSet<Field> getLinkedFields(Class<?> aClass) throws FirestormObjectException {
        if (!isRegistered(aClass)) {
            throw new FirestormObjectException("Cannot get linked fields of class '" + aClass.getName() + "', as it is not registered.");
        }
        HashSet<Field> fields = LINKED_FIELDS.get(aClass);
        if (fields == null) {
            return new HashSet<>();
        }
        return fields;
    }

    /**
     * Sets the ID field of a class.
     * @param aClass The class.
     * @param idField The ID field.
     */
    static void setIDField(Class<?> aClass, Field idField) {
        ID_FIELDS.put(aClass, idField);
    }

    /**
     * Retrieves the ID field of a class.
     * @param aClass The class.
     * @return Returns a {@link Field}.
     */
    static Field getIDField(Class<?> aClass) {
        return ID_FIELDS.get(aClass);
    }

}
