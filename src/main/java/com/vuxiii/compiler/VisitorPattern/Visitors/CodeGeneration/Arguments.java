package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class Arguments {
    
    public final Register src_1;
    public final Register src_2;
    public final Register target;

    public final ASTNode value;

    public final String variable;

    public final int kind;

    public Arguments( Register src_1, Register src_2, Register target ) {
        this.src_1 = src_1;
        this.src_2 = src_2;
        this.target = target;
        this.value = null;
        this.variable = null;
        kind = 0;
    }
    public Arguments( Register src_1, Register target ) {
        this.src_1 = src_1;
        this.src_2 = null;
        this.target = target;
        this.value = null;
        this.variable = null;
        kind = 1;
    }

    public Arguments( ASTNode src ) {
        this.value = src;
        this.src_1 = null;
        this.src_2 = null;
        this.target = null;
        this.variable = null;
        kind = 2;
    }

    public Arguments( Register src_1 ) {
        this.value = null;
        this.src_1 = src_1;
        this.src_2 = null;
        this.target = null;
        this.variable = null;
        kind = 3;
    }
    
    public Arguments( Register src_1, String target ) {
        this.src_1 = src_1;
        this.src_2 = null;
        this.target = null;
        this.variable = target;
        this.value = null;
        kind = 4;
    }

    public Arguments( String src_1, Register target ) {
        this.src_1 = null;
        this.src_2 = null;
        this.target = target;
        this.variable = src_1;
        this.value = null;
        kind = 5;
    }

    public String toString() {
        if ( kind == 2 ) {
            return value.toString();
        } else if ( kind == 0 ) {
            return src_1 + ", " + src_2 + " -> " + target;
        } else if (kind == 3 ) {
            return src_1.toString();
        } else if ( kind == 4 ) {
            return src_1  + " -> " + variable;
        } else if ( kind == 5 ) {
            return variable + " -> " + target;
        } else {
            return src_1 + " -> " + target;
        }
    }
}