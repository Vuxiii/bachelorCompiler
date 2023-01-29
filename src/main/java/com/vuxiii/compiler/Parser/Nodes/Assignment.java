package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class Assignment extends ASTNode {

    @VisitNumber( number = 1 ) public final LexIdent id;
    @VisitNumber( number = 2 ) public ASTNode value;

    public Assignment( Term term, LexIdent id, ASTNode value ) {
        super( term ); 
        this.id = id;
        this.value = value;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return id.name + " = " + value.toString();
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(id);
    }

    @Override
    public Optional<ASTNode> getChild2() {
        return Optional.of(value);
    }

    @Override
    public Optional<ASTNode> getChild3() {
        return Optional.empty();
    }

    @Override
    public Optional<ASTNode> getChild4() {
        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return "Assignment";
    }
    
}
