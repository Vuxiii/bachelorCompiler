package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Visitors.ASTNode;

public class Expression extends ASTNode {

    public final ASTNode node;

    public final Term term;

    public Expression( Term term, ASTNode node ) {
        this.term = term;
        this.node = node;
    }

    @Override
    public void accept(VisitorBase visitor) {
        accept1Child(visitor);
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
        return 1;
    }

    public String toString() {
        return "(EXP " + node.toString() + ")";
    }

    @Override
    protected Optional<ASTNode> getChild1() {
        return Optional.of(node);
    }

    @Override
    protected Optional<ASTNode> getChild2() {
        return Optional.empty();
    }

    @Override
    protected Optional<ASTNode> getChild3() {
        return Optional.empty();
    }

    @Override
    protected Optional<ASTNode> getChild4() {
        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return "Expression";
    }
    
}
