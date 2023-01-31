package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexType;
import com.vuxiii.compiler.Parser.Nodes.Types.AliasType;
import com.vuxiii.compiler.Parser.Nodes.Types.Type;
import com.vuxiii.compiler.Parser.Nodes.Types.UserType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Declaration extends ASTNode {

    @VisitNumber( number = 1 ) public final LexIdent id;

    @VisitNumber( number = 2 ) public final Type type;

    public final DeclarationKind kind;

    /**
     * This constructor constructs either a variable declaration or a type alias declaration, where:
     * 
     * @param term
     * @param id Is the variable name
     * @param type The variable's type
     * @param kind Determines whether it is a variable declaration (VARIABLE) or alias declaration (ALIAS_TYPE).
     */
    public Declaration( Term term, LexIdent id, Type type, DeclarationKind kind ) {
        // Either variable or alias to std_type
        super( term ); 
        this.id = id;
    
        this.type = type;
        this.kind = kind;
    
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return kind + " " + id.name + ": " + type;
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(id);
    }

    @Override
    public Optional<ASTNode> getChild2() {
        return Optional.of( type );
    }

    @Override
    public String getPrintableName() {
        return "Declaration: " + kind;
    }
    
}
