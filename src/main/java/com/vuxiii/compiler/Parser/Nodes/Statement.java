package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;

public class Statement implements ASTToken {

    public final ASTToken node;

    public final Optional<Statement> next;

    public final Term term;

    public final StatementKind kind;

    public Statement( Term term, ASTToken node, StatementKind kind ) {
        this.term = term;
        this.node = node;
        next = Optional.empty();
        this.kind = kind;
    }

    public Statement( Term term, ASTToken node, Statement next, StatementKind kind ) {
        this.term = term;
        this.node = node;
        this.next = Optional.of(next);
        this.kind = kind;
    }

    @Override
    public void accept(VisitorBase visitor) {
        visitor.preVisit( this );
        node.accept(visitor);
        visitor.midVisit( this );
        if ( next.isPresent() )
            next.get().accept(visitor);
        visitor.postVisit( this );
    }

    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return term;
    }

    public String toString() {
        if ( next.isEmpty() )
            return "(" + kind + " " + node.toString() + ")";
        return "(" + kind + " " + node.toString() + "); " + next.get().toString();
    }
    
}
