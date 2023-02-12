package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

public enum Register {
    RAX(0),
    RBX(1),
    RCX(2),
    RBP(3),
    RSP(4),
    VARIABLE(-1), RDI(5), RSI(6), RDX(7);

    public final int i;

    Register( int i ){
        this.i = i;
    } 
}
