package com.raylabz.firestorm;

import com.raylabz.firestorm.annotation.FirestormObject;
import com.raylabz.firestorm.annotation.ID;
import com.raylabz.firestorm.exception.FirestormObjectException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

final class Reflector {

    /**
     * Checks if the given class contains the required fields, types and annotations.
     * @param clazz The class to check.
     * @throws FirestormObjectException a) when the 'id' field has not been declared, is not of type string and not annotated with @ID, b) when the class is not annotated with @FirestormObjectAnnotation.
     */
    public static void checkClass(final Class<?> clazz) throws FirestormObjectException {
        //Check if object is annotated as @FirestormObjectAnnotation:
        final FirestormObject classAnnotation = clazz.getAnnotation(FirestormObject.class);
        if (classAnnotation == null) {
            throw new FirestormObjectException("The class '" + clazz.getSimpleName() + "' needs to be annotated with @" + FirestormObject.class.getSimpleName() + ".");
        }

        //Check if object class has an no-parameter constructor:
        if (!hasEmptyConstructor(clazz)) {
            throw new FirestormObjectException("The class '" + clazz.getSimpleName() + "' does not have an empty (no-parameter) constructor.");
        }

        Field idField = null;
        Class<?> idFieldType = null;

        //Check if the field 'id' exists at all:
        try {
            idField = clazz.getDeclaredField("id");
            idFieldType = idField.getType();
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            while (superClass.getSuperclass() != null) {
                final Field[] superClassFields = superClass.getDeclaredFields();
                for (Field f : superClassFields) {
                    if (f.getName().equals("id")) {
                        idField = f;
                        idFieldType = f.getType();
                        break;
                    }
                }

                if (idField != null) {
                    break;
                }
                else {
                    superClass = superClass.getSuperclass();
                }
            }
            if (idField == null) {
                throw new FirestormObjectException("A field named 'id' of type String needs to exist in class '" + clazz.getSimpleName() + "' or its parent classes but was not found.");
            }
        }

        //Check if field 'id' is String:
        if (idFieldType != String.class) {
            throw new FirestormObjectException("The 'id' field of class '" + clazz.getSimpleName() + "' must be of type String, but type " + idFieldType.getSimpleName() + " found.");
        }

        //Check if field 'id' has been annotated with @ID:
        final ID idAnnotation = idField.getAnnotation(ID.class);
        if (idAnnotation == null) {
            throw new FirestormObjectException("The 'id' field of class '" + clazz.getSimpleName() + "' has not been annotated with @" + ID.class.getSimpleName() + ".");
        }

        //Check if the 'id' field has a public getter:
        if (!fieldHasPublicGetter(idField, clazz)) {
            throw new FirestormObjectException("The 'id' field of class '" + clazz.getSimpleName() + "' does not have a getter method called '" + Reflector.getGetterMethodName(idField) + "'.");
        }

    }

    /**
     * Checks if the given object and its class contain the required fields, types and annotations.
     * @param object The object to check
     * @throws FirestormObjectException a) when the 'id' field has not been declared, is not of type string and not annotated with @ID, b) when the class of this object is not annotated with @FirestormObjectAnnotation.
     */
    public static void checkObject(final Object object) throws FirestormObjectException {
        checkClass(object.getClass());
    }

    /**
     * Checks if a given method is a getter.
     * @param method The method to check.
     * @return Returns true if this method is a getter, false otherwise.
     */
    private static boolean isPublicGetter(final Method method) {
        if (Modifier.isPublic(method.getModifiers()) &&
                method.getParameterTypes().length == 0) {
            if (method.getName().matches("^get[A-Z].*") &&
                    !method.getReturnType().equals(void.class))
                return true;
            if (method.getName().matches("^is[A-Z].*") &&
                    method.getReturnType().equals(boolean.class))
                return true;
        }
        return false;
    }

    /**
     * Checks if a given method is a setter.
     * @param method The method to check.
     * @return Returns true if this method is a setter, false otherwise.
     */
    private static boolean isPublicSetter(final Method method) {
        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z].*");
    }

    /**
     * Checks if a given field has a public getter method.
     * @param field The field to check for a getter method.
     * @param clazz The class of the field.
     * @return Returns true if a public getter was found for this field, false otherwise.
     */
    private static boolean fieldHasPublicGetter(final Field field, final Class<?> clazz) {
        final String getterMethodName = getGetterMethodName(field);
        final Method[] publicMethods = clazz.getMethods();
        for (Method m : publicMethods) {
            if (
                    m.getName().equals(getterMethodName) && m.getReturnType().equals(field.getType()) ||
                    m.getName().equals(getterMethodName) && (m.getReturnType().equals(boolean.class) || m.getReturnType().equals(Boolean.class))
            ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the Java convention-based getter method name for a given field.
     * @param field The field to retrieve the getter method name for.
     * @return Returns the getter method name for the specified field.
     */
    private static String getGetterMethodName(final Field field) {
        if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
            return "is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        }
        else {
            return "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        }
    }

    /**
     * Checks if a provided class has a no-argument constructor.
     * @param clazz The class to check.
     * @return Returns true if the class has a no-argument constructor, false otherwise.
     */
    private static boolean hasEmptyConstructor(final Class<?> clazz) {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the ID field of an object.
     * @param object The object to set the ID of.
     * @param documentID The document ID to set.
     * @throws NoSuchFieldException Thrown when the field 'id' cannot be accessed.
     * @throws IllegalAccessException Thrown when the field 'id' cannot be accessed.
     */
    static void setIDField(final Object object, final String documentID) throws NoSuchFieldException, IllegalAccessException {
        Field idField;
        try {
            idField = object.getClass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            idField = findUnderlyingIDField(object.getClass());
        }

        if (idField != null) {
            boolean accessible = idField.isAccessible();
            idField.setAccessible(true);
            idField.set(object, documentID);
            idField.setAccessible(accessible);
        }
        else {
            throw new NoSuchFieldException();
        }
    }

    /**
     * Retrieves the ID value of an object.
     * @param object The object to retrieve the ID value of.
     * @return Returns a the ID of the object as a string.
     * @throws NoSuchFieldException Thrown when the field 'id' cannot be accessed.
     * @throws IllegalAccessException Thrown when the field 'id' cannot be accessed.
     */
    static String getIDField(final Object object) throws NoSuchFieldException, IllegalAccessException {
        Field idField;
        try {
            idField = object.getClass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            idField = findUnderlyingIDField(object.getClass());
        }
        if (idField != null) {
            boolean accessible = idField.isAccessible();
            idField.setAccessible(true);
            final String value = (String) idField.get(object);
            idField.setAccessible(accessible);
            return value;
        }
        else {
            throw new NoSuchFieldException();
        }
    }

    static Field findUnderlyingIDField(Class<?> clazz) {
        Class<?> current = clazz;
        do {
            try {
                return current.getDeclaredField("id");
            } catch (Exception ignored) {}
        } while((current = current.getSuperclass()) != null);
        return null;
    }

}
