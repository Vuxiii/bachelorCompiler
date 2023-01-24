package com.vuxiii.compiler.VisitorPattern.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * This annotation denotes that the given field is a child that should
 * be visited by the Visitor Pattern.
 */
public @interface VisitNumber {
    /**
     * The number in the queue this child will be visited in.
     * @return The number in the queue to accept the visitor.
     */
    int number();
}
