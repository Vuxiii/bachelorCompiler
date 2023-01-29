package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class Capture extends ASTNode {

    @VisitNumber( number = 1 )
    public Optional<Argument> accesses;

    public Capture( Term term, Argument accesses ) {
        super(term);
        this.accesses = Optional.of(accesses);
        super.setup_ASTNodeQueue();
    }

    public Capture( Term term ) {
        super(term);
        this.accesses = Optional.empty();
        super.setup_ASTNodeQueue();
    }

    @Override
    public Optional<ASTNode> getChild1() {
        if ( accesses.isEmpty() ) 
            return Optional.empty();
        return Optional.of( accesses.get() );
    }
    
    @Override
    public String getPrintableName() {
        return "Capture" + (accesses.isEmpty() ? " [..]" : "");
    }

    @Override
    public String toString() {
        if ( accesses.isPresent() )
            return "(Capture Uses: [" + accesses.get().toString() + "])";
        return "(Capture Uses: [..])"; // Or '*'?
    }

    
    

}
