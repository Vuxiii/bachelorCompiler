package com.vuxiii.compiler.VisitorPattern;

public enum VisitOrder {
    /**
     * This indicates that the method will be called when the visitor first
     * visits the node.
     */
    ENTER_NODE,

    /**
     * This indicates that the method will be called just before the visitor
     * will visit any child.
     */
    BEFORE_CHILD,
    /**
     * This indicates that the method will be called just after the visitor
     * has visited any child.
     */
    AFTER_CHILD,
    
    /**
     * This indicates that the method will be called when the visitor 
     * exits the node.
     */
    EXIT_NODE,
}
