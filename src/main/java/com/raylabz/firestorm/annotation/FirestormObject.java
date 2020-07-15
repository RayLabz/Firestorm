package com.raylabz.firestorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a class as a Firestorm-managed object.
 * Any class that will have its objects written to the Firestore using Firestorm needs to be annotated with @FirestormObject.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FirestormObject {
}
