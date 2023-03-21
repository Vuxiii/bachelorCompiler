package com.vuxiii.compiler.Parser.Nodes;

import java.util.ArrayList;
import java.util.List;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class IfList extends ASTNode {

    @VisitNumber( number = 1 ) public final List<ASTNode> if_nodes;

    public IfList( Term term ) {
        super( term ); 
        if_nodes = new ArrayList<>();

        super.setup_ASTNodeQueue();
    }

    public void push( ASTNode stmt ) {
        if_nodes.add( stmt );
    }

    public String toString() {
        return if_nodes.toString();
    }


    @Override
    public String getPrintableName() {
        return "If_Statement_List";
    }
    
}
