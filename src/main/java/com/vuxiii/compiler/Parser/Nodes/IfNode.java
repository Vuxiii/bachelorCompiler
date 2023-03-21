package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class IfNode extends ASTNode {
    private static int if_node_counter = 0;


    @VisitNumber( number = 1 ) public final Expression guard;

    @VisitNumber( number = 2 ) public ASTNode body;

    public final String label_enter;
    public final String end_of_body;

    public IfNode( Term term, Expression guard, Statement body ) {
        super( term ); 
        this.guard = guard;
        this.body = body;

        this.label_enter = "IfLabelEnter" + (++if_node_counter);
        this.end_of_body = "IfEndOfBody" + (if_node_counter);

        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "If (" + guard.toString() + ") { " + body.toString() + " };";
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(guard);
    }

    @Override
    public Optional<ASTNode> getChild2() {
        return Optional.of(body);
    }

    @Override
    public String getPrintableName() {
        return "If_Statement";
    }
    
}
