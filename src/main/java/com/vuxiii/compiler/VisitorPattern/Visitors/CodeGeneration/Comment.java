package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

public record Comment( String value ) {

    public String toString() {
        return "\u001B[32m# " + value + "\u001B[0m";
    }

}
