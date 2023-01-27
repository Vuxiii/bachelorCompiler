package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class Arguments {
    
    public final Register left;
    public final Register right;
    public final Register target;

    public final Register register;

    public final ASTNode value;

    public final String variable;

    public final int kind;

    public Arguments(Register left, Register right, Register target) {
        this.left = left;
        this.right = right;
        this.target = target;
        this.value = null;
        this.variable = null;
        register = null;
        kind = 0;
    }
    public Arguments(Register left, Register target) {
        this.left = left;
        this.right = null;
        this.target = target;
        this.value = null;
        this.variable = null;
        register = null;
        kind = 1;
    }

    public Arguments( ASTNode value ) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.target = null;
        this.variable = null;
        register = null;
        kind = 2;
    }

    public Arguments( Register register ) {
        this.value = null;
        this.left = null;
        this.right = null;
        this.target = null;
        this.register = register;
        this.variable = null;
        kind = 3;
    }
    
    public Arguments(Register register, String variable ) {
        this.left = null;
        this.right = null;
        this.target = null;
        this.variable = variable;
        this.value = null;
        this.register = register;
        kind = 4;
    }

    public Arguments( String variable, Register register ) {
        this.left = null;
        this.right = null;
        this.target = null;
        this.variable = variable;
        this.value = null;
        this.register = register;
        kind = 5;
    }

    public String toString() {
        if ( kind == 2 ) {
            return value.toString();
        } else if ( kind == 0 ) {
            return left + ", " + right + " -> " + target;
        } else if (kind == 3 ) {
            return register.toString();
        } else if ( kind == 4 ) {
            return register  + " -> " + variable;
        } else if ( kind == 5 ) {
            return variable + " -> " + register;
        } else {
            return left + " -> " + target;
        }
    }
}
