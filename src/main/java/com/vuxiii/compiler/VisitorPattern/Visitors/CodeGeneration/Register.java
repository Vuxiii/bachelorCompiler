package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

public enum Register {
    RAX(0),
    RBX(1),
    RCX(2),

    VARIABLE(-1);

    public final int i;

    Register( int i ){
        this.i = i;
    } 
}
