package com.vuxiii.compiler.Parser.Nodes.Types;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Parser.Nodes.Parameter;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class FunctionType extends Type {
    
    @VisitNumber( number = 1 ) public final Optional<Parameter> parameters;
    @VisitNumber( number = 2 ) public final Optional<Type> return_type;
    @VisitNumber( number = 3 ) public final Optional<Statement> body;

    public FunctionType( Term term, Optional<Parameter> parameter_list, Optional<Type> return_type, Optional<Statement> body ) {
        super(term);
        
        this.parameters =  parameter_list;
        this.return_type = return_type;
        this.body = body;
        super.setup_ASTNodeQueue();
    }

    
    @Override
    public Optional<ASTNode> getChild1() {
        if ( parameters.isPresent() )
            return Optional.of( parameters.get() );
        return Optional.empty();
    }
    @Override
    public Optional<ASTNode> getChild2() {
        if ( return_type.isPresent() )
            return Optional.of( return_type.get() );
        return Optional.empty();
    }
    @Override
    public Optional<ASTNode> getChild3() {
        if ( body.isPresent() )
            return Optional.of( body.get() );
        return Optional.empty();
    }

    public String toString() {
        return "FunctionType";
    }

    @Override
    public String getPrintableName() {
        return "FUNC_Type: " + simple_type_name();

    }
    
    @Override
    public String simple_type_name() {
        String out = "(";

        if ( parameters.isPresent() ) {
            out += parameters.get().get_readable_parameter_list();
        }
        
        out += ") -> ";

        if ( return_type.isPresent() ) 
            out += ((Type)return_type.get()).simple_type_name();
        else
            out += "void";
        return out;
    }
    
}
