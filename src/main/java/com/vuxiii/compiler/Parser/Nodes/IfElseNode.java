package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class IfElseNode extends ASTNode {

    @VisitNumber( number = 1 ) public final IfNode if_block;

    @VisitNumber( number = 2 ) public final ElseNode else_block;

    public IfElseNode( Term term, IfNode if_block, ElseNode else_block ) {
        super( term ); 
        this.if_block = if_block;
        this.else_block = else_block;

        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return if_block.toString() + " : " + else_block.toString();
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(if_block);
    }

    @Override
    public Optional<ASTNode> getChild2() {
        return Optional.of(else_block);
    }

    @Override
    public String getPrintableName() {
        return "If_Else_Statement";
    }
    
}
