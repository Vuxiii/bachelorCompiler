package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class Expression extends ASTNode {

    @VisitNumber( number = 1 ) public final ASTNode node;

    public Expression( Term term, ASTNode node ) {
        super( term ); 
        this.node = node;
        super.setup_ASTNodeQueue();
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
