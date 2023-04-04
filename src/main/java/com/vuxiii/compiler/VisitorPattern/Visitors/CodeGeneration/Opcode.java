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
    JUMP_NOT_EQUAL,

    LOAD_VARIABLE,
    STORE_VARIABLE,

    SETUP_STACK,
    RESTORE_STACK,

    LABEL,

    PRINT, PRINT_STRING, LEA, COMMENT, PRINT_NUM, EQUALS,
}
