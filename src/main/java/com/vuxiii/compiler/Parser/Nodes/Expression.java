package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;

public class Expression implements ASTToken {

    public final ASTToken node;

    public final Term term;

    public Expression( Term term, ASTToken node ) {
        this.term = term;
        this.node = node;
    }

    @Override
    public void accept(VisitorBase visitor) {
        visitor.preVisit( this );
        node.accept(visitor);
        visitor.midVisit( this );
        visitor.postVisit( this );
    }

    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return term;
    }

    public String toString() {
        return "(EXP " + node.toString() + ")";
    }
    
}
