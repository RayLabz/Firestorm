package com.raylabz.firestorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates an attribute/field of a class as a "linked" attribute.
 * Linked attributes are attributes of which the values are references from fields of other classes
 * (similar to a foreign key in RDBMSs).
 * @author Nicos Kasenides
 * @version 2.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LinkedField {
}
