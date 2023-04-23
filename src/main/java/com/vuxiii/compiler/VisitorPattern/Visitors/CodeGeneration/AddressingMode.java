package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

public enum AddressingMode {
    DIRECT_MEMORY,
    DIRECT_OFFSET,
    INDIRECT_MEMORY,
    IMMEDIATE,
    LABEL,
    REGISER, FUNCTION_POINTER,
}
