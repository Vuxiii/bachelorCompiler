package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class BinaryOperation extends ASTNode {


    @VisitNumber( number = 2 ) 
    public ASTNode left;
    
    @VisitNumber( number = 3 ) 
    public ASTNode right;
    
    @VisitNumber( number = 1 )
    public final ASTNode operator;
    

    public final BinaryOperationKind kind;

    public BinaryOperation( Term term, ASTNode left, ASTNode right, ASTNode operator, BinaryOperationKind kind ) {
        super( term ); 
        this.left = left;
        this.right = right;
        this.kind = kind;
        this.operator = operator;
        super.setup_ASTNodeQueue();
        
    }

    public String toString() {
        return left + " " + kind + " " + right;
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(operator);
    }

    @Override
    public Optional<ASTNode> getChild2() {
        return Optional.of(left);
    }

    @Override
    public Optional<ASTNode> getChild3() {
        return Optional.of(right);
    }

    @Override
    public Optional<ASTNode> getChild4() {
        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return "BinaryOperation";
    }
    
}
