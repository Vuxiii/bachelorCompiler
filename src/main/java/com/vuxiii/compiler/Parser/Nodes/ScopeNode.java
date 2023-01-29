package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class ScopeNode extends ASTNode {


    @VisitNumber( number = 1 ) 
    public Optional<Capture> capture;

    @VisitNumber( number = 2 ) 
    public Statement body;
    
    public ScopeNode( Term term, Statement body ) {
        super( term ); 
        this.body = body;
        this.capture = Optional.empty();
        super.setup_ASTNodeQueue();
    }

    public ScopeNode( Term term, Statement body, Capture capture ) {
        super( term ); 
        this.body = body;
        this.capture = Optional.of( capture );

        super.setup_ASTNodeQueue();
    }

    public String toString() {
        if ( capture.isPresent() )
            return "Accesses " + capture.get() +  " Body " + body;
        return "Body " + body;
    }

    @Override
    public Optional<ASTNode> getChild1() {
        if ( capture.isEmpty() )
            return Optional.empty();
        return Optional.of( capture.get() );
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
