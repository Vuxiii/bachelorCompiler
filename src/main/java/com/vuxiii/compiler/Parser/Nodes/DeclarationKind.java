package com.vuxiii.compiler.Parser.Nodes;

public enum DeclarationKind {
    HEAP,
    VARIABLE,
    PARAMETER,
    FUNCTION,
    NEW_FUNCTION_TYPE,
    USER_TYPE,
    ALIAS_TO_USER_TYPE,
    ALIAS_TO_STD_TYPE,
    FIELD,
    UNKNOWN,
}
