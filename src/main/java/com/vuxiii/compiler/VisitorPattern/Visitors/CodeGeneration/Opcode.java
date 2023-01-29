package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

public enum Opcode {
    ADD,
    MINUS,
    MULT,
    DIV,
    DIV_INTEGER,
    MODULO,
    PUSH,
    POP,
    RETURN,
    MOVE,
    CALL,
    COMPARE,
    JUMP,
    JUMP_LESS,
    JUMP_LESS_EQUAL,
    JUMP_GREATER,
    JUMP_GREATER_EQUAL,
    JUMP_EQUAL,

    LOAD_VARIABLE,
    STORE_VARIABLE,

    PRINT,
}
