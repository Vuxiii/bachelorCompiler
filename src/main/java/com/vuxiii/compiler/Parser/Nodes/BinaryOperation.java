package com.vuxiii.compiler.Parser.Nodes;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;

public class BinaryOperation implements ASTToken {

    public final ASTToken left;
    public final ASTToken right;
    public final BinaryOperationKind kind;

    public final Term term;

    public BinaryOperation( Term term, ASTToken left, ASTToken right, BinaryOperationKind kind ) {
        this.left = left;
        this.right = right;
        this.kind = kind;

        this.term = term;
    }

    @Override
    public void accept(VisitorBase arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return term;
    }

    public String toString() {
        return left + " " + kind + " " + right;
    }
    
}
