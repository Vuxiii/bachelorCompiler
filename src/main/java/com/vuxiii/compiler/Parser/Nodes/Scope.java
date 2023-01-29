package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class Scope extends ASTNode {


    @VisitNumber( number = 1 ) 
    public Optional<Capture> accesses;

    @VisitNumber( number = 2 ) 
    public Statement body;

    
    public Scope( Term term, Statement body ) {
        super( term ); 
        this.body = body;
        this.accesses = Optional.empty();

        super.setup_ASTNodeQueue();
    }

    public Scope( Term term, Statement body, Capture capture ) {
        super( term ); 
        this.body = body;
        this.accesses = Optional.of( capture );

        super.setup_ASTNodeQueue();
    }

    public String toString() {
        if ( accesses.isPresent() )
            return "Accesses " + accesses +  " Body " + body;
        return "Body " + body;
    }

    @Override
    public Optional<ASTNode> getChild1() {
        if ( accesses.isEmpty() )
            return Optional.empty();
        return Optional.of( accesses.get() );
    }

    @Override
    public Optional<ASTNode> getChild2() {
        return Optional.of(body);
    }

    @Override
    public String getPrintableName() {
        return "Scope";
    }
    
}
