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

    public FunctionType( Term term, Parameter parameter_list, Type return_type, Statement body ) {
        super(term);
        
        this.parameters = Optional.of( parameter_list );
        this.return_type = Optional.of(return_type);
        this.body = Optional.of(body);
        super.setup_ASTNodeQueue();
    }

    public FunctionType( Term term, Parameter parameter_list, Statement body ) {
        super(term);
        
        this.parameters = Optional.of( parameter_list );
        this.return_type = Optional.empty();
        this.body = Optional.of(body);
        super.setup_ASTNodeQueue();
    }

    public FunctionType( Term term, Parameter parameter_list, Type return_type ) {
        super(term);
        
        this.parameters = Optional.of( parameter_list );
        this.return_type = Optional.of(return_type);
        this.body = Optional.empty();
        super.setup_ASTNodeQueue();
    }

    public FunctionType( Term term, Type return_type ) {
        super(term);
        
        this.parameters = Optional.empty();
        this.return_type = Optional.of(return_type);
        this.body = Optional.empty();
        super.setup_ASTNodeQueue();
    }

    public FunctionType( Term term, Parameter parameters ) {
        super(term);
        
        this.parameters = Optional.of(parameters);
        this.return_type = Optional.empty();
        this.body = Optional.empty();
        super.setup_ASTNodeQueue();
    }

    public FunctionType( Term term ) {
        super(term);
        
        this.parameters = Optional.empty();
        this.return_type = Optional.empty();
        this.body = Optional.empty();
        super.setup_ASTNodeQueue();
    }

    public FunctionType( Term term, Statement body ) {
        super(term);
        
        this.parameters = Optional.empty();
        this.return_type = Optional.empty();
        this.body = Optional.of(body);
        super.setup_ASTNodeQueue();
    }

    public FunctionType( Term term, Type return_type, Statement body ) {
        super(term);
        
        this.parameters = Optional.empty();
        this.return_type = Optional.of(return_type);
        this.body = Optional.of(body);
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
        return "FUNC_Type";
    }
    
}
