package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Declaration extends ASTNode {

    @VisitNumber( number = 1 ) public final LexIdent id;

    @VisitNumber( number = 2 ) public final Optional<LexType> std_type;

    @VisitNumber( number = 3 ) public final Optional<UserType> user_type;

    @VisitNumber( number = 4 ) public final Optional<LexIdent> alias_type;

    public final DeclarationKind kind;

    /**
     * This constructor constructs either a variable declaration or a type alias declaration, where:
     * 
     * @param term
     * @param id Is the variable name
     * @param type The variable's type
     * @param kind Determines whether it is a variable declaration (VARIABLE) or alias declaration (ALIAS_TYPE).
     */
    public Declaration( Term term, LexIdent id, LexType type, DeclarationKind kind ) {
        // Either variable or alias to std_type
        super( term ); 
        this.id = id;
    
        this.std_type = Optional.of(type);
        this.user_type = Optional.empty();
        this.alias_type = Optional.empty();
        this.kind = kind;
    
        super.setup_ASTNodeQueue();
    }
    
    /**
     * This contructor declares that a user_type is to be constructed, where:
     * 
     * @param term
     * @param id Is the type's name
     * @param user_type Is the type's type-declaration
     */
    public Declaration( Term term, LexIdent id, UserType user_type, DeclarationKind kind ) {
        // Either User type or Variable declaration of type User type
        super( term ); 
        this.id = id;
        this.std_type = Optional.empty();
        this.user_type = Optional.of(user_type);
        this.alias_type = Optional.empty();
        this.kind = kind;
        super.setup_ASTNodeQueue();
    }
    
    /**
     * This constructor declares that an alias of another user_type or a standard_type is to be constructed, where
     * @param term
     * @param id Is the type's alias_name
     * @param alias_type Is the type's type.
     */
    public Declaration( Term term, LexIdent id, LexIdent alias_type, DeclarationKind kind ) {
        // Alias to another alias or declare variable of type user_alias
        super( term ); 
        this.id = id;
        this.std_type = Optional.empty();
        this.user_type = Optional.empty();
        this.alias_type = Optional.of(alias_type);
        this.kind = kind;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return kind + id.name + ": " + std_type;
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(id);
    }

    @Override
    public Optional<ASTNode> getChild2() {
        if ( std_type.isPresent() )
            return Optional.of( std_type.get() );
        else if ( user_type.isPresent() )
            return Optional.of( user_type.get() );
        else if ( alias_type.isPresent() )
            return Optional.of( alias_type.get() ); // This is an alias for another usertype.
        else 
            return Optional.of( std_type.get() ); // This is an alias for a standard type.

    }

    @Override
    public String getPrintableName() {
        return "Declaration: " + kind;
    }
    
}
