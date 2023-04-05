package com.vuxiii.compiler.Parser.Nodes.Types;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;


public class UserType extends Type {
    
    @VisitNumber( number = 1 ) public Optional<LexIdent> identifier;

    @VisitNumber( number = 2 ) public ASTNode fields;

    public UserType( Term term, LexIdent user_type, ASTNode fields ) {
        super(term);
        this.identifier = Optional.of(user_type);
        this.fields = fields;
        super.setup_ASTNodeQueue();
    }

    public UserType( Term term, ASTNode fields ) {
        super(term);
        this.identifier = Optional.empty();
        this.fields = fields;
        super.setup_ASTNodeQueue();
    }

    @Override
    public String getPrintableName() {
        return "User_Type";
    }
    
    @Override
    public String simple_type_name() {
        if ( identifier.isPresent() )
            return identifier.get().name;
        return "identifier not set yet for user Type";
    }

    @Override
    public int physical_size() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'physical_size'");
    }

}
