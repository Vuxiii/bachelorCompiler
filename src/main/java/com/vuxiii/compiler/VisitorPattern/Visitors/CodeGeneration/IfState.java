package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

public enum IfState {
    ENTER_GUARD,
    EXIT_GUARD,
    ENTER_BODY,
    EXIT_BODY,
    NONE,
}
