package com.vuxiii.compiler.Parser.Nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class StatementList extends ASTNode {

    @VisitNumber( number = 1 ) public final List<Statement> statements;

    public StatementList( Term term ) {
        super( term ); 
        statements = new ArrayList<>();

        super.setup_ASTNodeQueue();
    }

    public void push( Statement stmt ) {
        statements.add( stmt );
    }

    public String toString() {
        return statements.toString();
    }

    @Override
    public Optional<ASTNode> getChild1() {
        // return Optional.of(node);
        return null;
    }

    @Override
    public String getPrintableName() {
        return "Statement_List";
    }
    
}
