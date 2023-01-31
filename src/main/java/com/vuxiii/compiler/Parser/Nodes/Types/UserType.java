package com.vuxiii.compiler.Parser.Nodes.Types;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;


public class UserType extends Type {
    
    @VisitNumber( number = 1 ) public final LexIdent identifier;

    @VisitNumber( number = 2 ) public final Field fields;

    public UserType( Term term, LexIdent user_type, Field fields ) {
        super(term);
        this.identifier = user_type;
        this.fields = fields;
        super.setup_ASTNodeQueue();
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of( identifier );
    }

    @Override
    public Optional<ASTNode> getChild2() {
        return Optional.of( fields );
    }

    @Override
    public String getPrintableName() {
        return "User_Type";
    }
    
}
