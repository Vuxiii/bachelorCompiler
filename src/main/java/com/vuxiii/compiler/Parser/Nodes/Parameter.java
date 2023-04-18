
package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Parameter extends ASTNode {


    @VisitNumber( number = 1 ) public Declaration param; // Not ident because this is an actual argument. It could be a literal!

    @VisitNumber( number = 2 ) public final Optional<Parameter> next;


    public Parameter( Term term, Declaration parameter ) {
        super( term ); 
        this.param = parameter;
        next = Optional.empty();
        super.setup_ASTNodeQueue();
    }
    public Parameter( Term term, Declaration parameter, Parameter next ) {
        super( term ); 
        this.param = parameter;
        this.next = Optional.of(next);
        super.setup_ASTNodeQueue();
    }

    public boolean equals( Object other ) {
        if ( other == null ) return false;
        if ( !(other instanceof Parameter) ) return false;
        Parameter o = (Parameter)other;
        boolean b = param.id.name.equals( o.param.id.name ) && param.type.equals( o.param.type );
        if ( b == false ) return false;

        if ( next.isPresent() && o.next.isPresent() ) {
            return next.get().equals( o.next.get() );
        } else if (next.isEmpty() && o.next.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        if ( next.isEmpty() )
            return "(Parameter : " + param.toString() + ")";
        return "(Arg : " + param.toString() + "); " + next.get().toString();
    }

    @Override
    public String getPrintableName() {
        return (next.isEmpty()  ? "Parameter" 
                                : "Parameter_List");
    }

    public String get_readable_parameter_list() {
        String out = "";

        Parameter current = this;
        while ( current != null ) {
            out += ((Declaration)current.param).get_parameter_form() + ", ";
            current = current.next.isPresent() ? current.next.get() : null;
        }

        return out.substring(0, out.length()-2);
    }
    
}
