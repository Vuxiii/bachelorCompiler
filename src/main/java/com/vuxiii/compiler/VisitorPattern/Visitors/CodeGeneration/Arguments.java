package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class Arguments {
    
    public final Register operand_1;
    public final Register operand_2;
    public final Register target;

    public final ASTNode value;

    public final String variable;

    public final int kind;

    public Arguments(Register left, Register right, Register target) {
        this.operand_1 = left;
        this.operand_2 = right;
        this.target = target;
        this.value = null;
        this.variable = null;
        kind = 0;
    }
    public Arguments(Register left, Register target) {
        this.operand_1 = left;
        this.operand_2 = null;
        this.target = target;
        this.value = null;
        this.variable = null;
        kind = 1;
    }

    public Arguments( ASTNode value ) {
        this.value = value;
        this.operand_1 = null;
        this.operand_2 = null;
        this.target = null;
        this.variable = null;
        kind = 2;
    }

    public Arguments( Register operand_1 ) {
        this.value = null;
        this.operand_1 = operand_1;
        this.operand_2 = null;
        this.target = null;
        this.variable = null;
        kind = 3;
    }
    
    public Arguments(Register operand_1, String variable ) {
        this.operand_1 = operand_1;
        this.operand_2 = null;
        this.target = null;
        this.variable = variable;
        this.value = null;
        kind = 4;
    }

    public Arguments( String variable, Register target ) {
        this.operand_1 = null;
        this.operand_2 = null;
        this.target = target;
        this.variable = variable;
        this.value = null;
        kind = 5;
    }

    public String toString() {
        if ( kind == 2 ) {
            return value.toString();
        } else if ( kind == 0 ) {
            return operand_1 + ", " + operand_2 + " -> " + target;
        } else if (kind == 3 ) {
            return operand_1.toString();
        } else if ( kind == 4 ) {
            return operand_1  + " -> " + variable;
        } else if ( kind == 5 ) {
            return variable + " -> " + target;
        } else {
            return operand_1 + " -> " + target;
        }
    }
}
