package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Visitors.ASTNode;

public class BinaryOperation extends ASTNode {

    public final ASTNode left;
    public final ASTNode right;
    public final ASTNode operator;

    public final BinaryOperationKind kind;

    public final Term term;

    public BinaryOperation( Term term, ASTNode left, ASTNode right, ASTNode operator, BinaryOperationKind kind ) {
        this.left = left;
        this.right = right;
        this.kind = kind;
        this.operator = operator;

        this.term = term;
    }

    @Override
    public void accept(VisitorBase visitor) {
        accept3Child(visitor);
        
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
        return 3;
    }

    public String toString() {
        return left + " " + kind + " " + right;
    }

    @Override
    protected Optional<ASTNode> getChild1() {
        return Optional.of(operator);
    }

    @Override
    protected Optional<ASTNode> getChild2() {
        return Optional.of(left);
    }

    @Override
    protected Optional<ASTNode> getChild3() {
        return Optional.of(right);
    }

    @Override
    protected Optional<ASTNode> getChild4() {
        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return "BinaryOperation";
    }
    
}
