package com.vuxiii.compiler.VisitorPattern.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This Annotation is used to indicate that the following method is 
 * called by the visitor pattern!
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface  VisitorPattern {
    // Maybe add some sort of pre and post for children?
    /**
     * This attribute indicates when the method will be called in the Visitor Pattern sequence.
     * @return When in the sequence this method will be called.
     */
    VisitOrder when() default VisitOrder.ENTER_NODE;

    /**
     * This indicates in which order the methods will be called that 
     * share the same VisitOrder directive.
     * @return The number in the queue when this method will be called.
     */
    int order();
}
