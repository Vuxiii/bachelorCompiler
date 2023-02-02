
package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Parameter extends ASTNode {


    @VisitNumber( number = 1 ) public ASTNode param; // Not ident because this is an actual argument. It could be a literal!

    @VisitNumber( number = 2 ) public final Optional<Parameter> next;


    public Parameter( Term term, ASTNode parameter ) {
        super( term ); 
        this.param = parameter;
        next = Optional.empty();
        super.setup_ASTNodeQueue();
    }
    public Parameter( Term term, ASTNode parameter, Parameter next ) {
        super( term ); 
        this.param = parameter;
        this.next = Optional.of(next);
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        if ( next.isEmpty() )
            return "(Parameter : " + param.toString() + ")";
        return "(Arg : " + param.toString() + "); " + next.get().toString();
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(param);
    }

    @Override
    public Optional<ASTNode> getChild2() {
        if ( next.isPresent() )
            return Optional.of( next.get() );

        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return (next.isEmpty()  ? "Parameter" 
                                : "Parameter_List");
    }
    
}
