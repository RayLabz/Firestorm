package com.raylabz.firestorm.exception;

public class ClassRegistrationException extends Exception {

    public ClassRegistrationException(Class<?> aClass) {
        super("The class '" + aClass.getName() + "' is not registered. Register your classes using com.raylabz.firestorm.Firestorm.register().");
    }

}
