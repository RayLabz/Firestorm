package com.raylabz.firestorm.reflector;

import com.raylabz.firestorm.annotation.FirestormObjectAnnotation;
import com.raylabz.firestorm.annotation.ID;
import com.raylabz.firestorm.exception.FirestormObjectException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ClassChecker {

    /**
     * Checks if the given object and its class contain the required fields, types and annotations.
     * @param object The object to check
     * @throws FirestormObjectException a) when the 'id' field has not been declared, is not of type string and not annotated with @ID, b) when the class of this object is not annotated with @FirestormObjectAnnotation.
     */
    public static void checkObject(Object object) throws FirestormObjectException {
        Class<?> clazz = object.getClass();

        //Check if object is annotated as @FirestormObjectAnnotation:
        final FirestormObjectAnnotation classAnnotation = clazz.getAnnotation(FirestormObjectAnnotation.class);
        if (classAnnotation == null) {
            throw new FirestormObjectException("The class '" + clazz.getSimpleName() + "' needs to be annotated with @" + FirestormObjectAnnotation.class.getSimpleName() + ".");
        }

        //Check if the field 'id' exists at all:
        try {
            Field idField = clazz.getDeclaredField("id");
            final Class<?> idFieldType = idField.getType();

            //Check if field 'id' exists:
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
                throw new FirestormObjectException("The 'id' field of class '" + clazz.getSimpleName() + "' does not have a getter method called '" + ClassChecker.getGetterMethodName(idField) + "'.");
            }

        } catch (NoSuchFieldException e) {
            throw new FirestormObjectException("A field named 'id' of type String needs to exist in class '" + clazz.getSimpleName() + "' but was not found.");
        }

    }

    /**
     * Checks if a given method is a getter.
     * @param method The method to check.
     * @return Returns true if this method is a getter, false otherwise.
     */
    private static boolean isPublicGetter(Method method) {
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
    private static boolean isPublicSetter(Method method) {
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
    private static boolean fieldHasPublicGetter(Field field, Class<?> clazz) {
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
    private static String getGetterMethodName(Field field) {
        if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
            return "is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        }
        else {
            return "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        }
    }

}