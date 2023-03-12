package com.vuxiii.compiler.Lexer.Tokens;

/**
 * Primitive Types for the JuhlLang 
 */
public enum PrimitiveType {
    INT("int"),
    DOUBLE("double"),
    STRING("string"),
    BOOL("boolean");

    public String name;

    private PrimitiveType( String name ) {
        this.name = name;
    }
}
