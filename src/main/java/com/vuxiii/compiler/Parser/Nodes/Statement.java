package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Visitors.ASTNode;

public class Statement extends ASTNode {

    public final ASTNode node;

    public final Optional<Statement> next;

    public final Term term;

    public final StatementKind kind;

    public Statement( Term term, ASTNode node, StatementKind kind ) {
        this.term = term;
        this.node = node;
        next = Optional.empty();
        this.kind = kind;
    }

    public Statement( Term term, ASTNode node, Statement next, StatementKind kind ) {
        this.term = term;
        this.node = node;
        this.next = Optional.of(next);
        this.kind = kind;
    }

    @Override
    public void accept(VisitorBase visitor) {
        visitor.visit( this );
        
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

    @Override
    public boolean isLeaf() {
        return false;
    }
    
    @Override
    public int getChildrenCount() {
        return next.isEmpty() ? 1 : 2;
    }

    public String toString() {
        if ( next.isEmpty() )
            return "(" + kind + " " + node.toString() + ")";
        return "(" + kind + " " + node.toString() + "); " + next.get().toString();
    }

    @Override
    protected Optional<ASTNode> getChild1() {
        // TODO Auto-generated method stub
        return Optional.of(node);
    }

    @Override
    protected Optional<ASTNode> getChild2() {
        if ( next.isPresent() )
            return Optional.of( next.get() );

        return Optional.empty();
    }

    @Override
    protected Optional<ASTNode> getChild3() {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    protected Optional<ASTNode> getChild4() {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return (next.isEmpty()  ? "Statement" 
                                : "Statement_List");
    }
    
}
