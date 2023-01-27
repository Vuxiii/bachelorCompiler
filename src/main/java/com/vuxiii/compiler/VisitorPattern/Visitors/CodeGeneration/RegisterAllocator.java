package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.Stack;

public class RegisterAllocator {
    
    private int register_count = 0;
    private final Stack<VirtualRegister> registerStack = new Stack<>();

    public VirtualRegister next_register() {
        VirtualRegister vr = new VirtualRegister( register_count++ );
        registerStack.push( vr );
        System.out.println( "Stack: " + registerStack + " ~ " + registerStack.size() );
        return vr;
    }

    public VirtualRegister top() {
        return registerStack.get( register_count-1 );
    }

    public VirtualRegister second_top() {
        return registerStack.get( register_count-2 );
    }
}
