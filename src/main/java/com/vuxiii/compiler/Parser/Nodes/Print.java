package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Visitors.ASTNode;

public class Print extends ASTNode {

    public final ASTNode value;

    public final Term term;

    public Print( Term term, ASTNode value ) {
        this.value = value;

        this.term = term;
    }

    @Override
    public void accept( VisitorBase visitor ) {
        accept1Child(visitor);
    }

    @Override
    public Term getTerm() {
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
        return value.toString();
    }

    @Override
    protected Optional<ASTNode> getChild1() {
        return Optional.of(value);
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
        return "Print";
    }
    
}
