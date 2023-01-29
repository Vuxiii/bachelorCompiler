package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexType;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class Declaration extends ASTNode {

    @VisitNumber( number = 1 ) public final LexIdent id;

    @VisitNumber( number = 2 ) public final LexType type;

    public Declaration( Term term, LexIdent id, LexType type ) {
        super( term ); 
        this.id = id;
        this.type = type;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return id.name + ": " + type;
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(id);
    }

    @Override
    public Optional<ASTNode> getChild2() {
        return Optional.of(type);
    }

    @Override
    public String getPrintableName() {
        return "Declaration";
    }
    
}
