package com.raylabz.firestorm.api;

public interface BasicWriteOperationProvider<WriteResult, ExceptionType extends Exception> {

    WriteResult create(final Object object) throws ExceptionType;

    WriteResult update(final Object object) throws ExceptionType;

    WriteResult delete(final Object object) throws ExceptionType;

    <T> WriteResult delete(final Class<T> aClass, final String objectID) throws ExceptionType;

}
