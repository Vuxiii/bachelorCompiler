package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

public record Comment( String value ) {

    public String toString() {
        return "# " + value;
    }

}
