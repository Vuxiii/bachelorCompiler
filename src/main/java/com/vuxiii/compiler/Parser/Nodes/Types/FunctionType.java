package com.vuxiii.compiler.Parser.Nodes.Types;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class FunctionType extends Type {
    
    @VisitNumber( number = 1 ) public final Optional<Statement> parameters;
    @VisitNumber( number = 2 ) public final Type return_type;
    @VisitNumber( number = 3 ) public final Statement body;

    public FunctionType( Term term, Optional<Statement> parameter_list, Type return_type, Statement body ) {
        super(term);
        this.parameters = parameter_list;
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
        return Optional.of( return_type );
    }
    @Override
    public Optional<ASTNode> getChild3() {
        return Optional.of( body );
    }

    public String toString() {
        return "FunctionType";
    }

    @Override
    public String getPrintableName() {
        return "FUNC_Type";
    }
    
}
