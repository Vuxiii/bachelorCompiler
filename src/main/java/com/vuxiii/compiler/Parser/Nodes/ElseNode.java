package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class ElseNode extends ASTNode {
    private static int else_node_counter = 0;

    @VisitNumber( number = 1 ) public final Statement body;

    public final String label_enter;
    public final String label_exit;

    public ElseNode( Term term, Statement body ) {
        super( term ); 
        this.body = body;

        this.label_enter = "ElseLabelEnter" + (++else_node_counter);
        this.label_exit = "ElseLabelExit" + (else_node_counter);


        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "Else { " + body.toString() + " };";
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(body);
    }

    @Override
    public String getPrintableName() {
        return "Else_Statement";
    }
    
}
