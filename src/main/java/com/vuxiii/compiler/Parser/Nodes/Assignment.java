package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Visitors.ASTNode;

public class Assignment extends ASTNode {

    public final LexIdent id;
    public final ASTNode value;

    public final Term term;

    public Assignment( Term term, LexIdent id, ASTNode value ) {
        this.id = id;
        this.value = value;

        this.term = term;
    }

    @Override
    public void accept( VisitorBase visitor ) {
        accept2Child(visitor);
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
        return 2;
    }

    public String toString() {
        return id.name + " = " + value.toString();
    }

    @Override
    protected Optional<ASTNode> getChild1() {
        return Optional.of(id);
    }

    @Override
    protected Optional<ASTNode> getChild2() {
        return Optional.of(value);
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
        return "Assignment";
    }
    
}
