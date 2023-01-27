package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

public class VirtualRegister {
    
    public final int id;

    public VirtualRegister( int id ) {
        this.id = id;
    }

    public String toString() {
        return "t" + id;
    }
}
