package com.raylabz.firestorm;

import java.util.List;

public interface BasicReadOperationProvider <ExceptionType extends Exception> {

    <T> T get(final Class<T> objectClass, final String documentID) throws ExceptionType;

    <T> List<T> list(final Class<T> objectClass) throws ExceptionType;

    <T> List<T> list(final Class<T> objectClass, final int limit) throws ExceptionType;

}
