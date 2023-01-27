package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class OpcodeArgument {
    
    public final Register left;
    public final Register right;
    public final Register target;

    public final Register register;

    public final ASTNode value;

    public OpcodeArgument(Register left, Register right, Register target) {
        this.left = left;
        this.right = right;
        this.target = target;
        this.value = null;
        register = null;
    }

    public OpcodeArgument( ASTNode value ) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.target = null;
        register = null;
    }

    public OpcodeArgument( Register register ) {
        this.value = null;
        this.left = null;
        this.right = null;
        this.target = null;
        this.register = register;
    }

    public String toString() {
        if ( left == null && register == null ) {
            return value.toString();
        } else if ( register == null ) {
            return left + ", " + right + " -> " + target;
        } else {
            return register.toString();
        }
    }
}
