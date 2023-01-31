package com.vuxiii.compiler.Parser.Nodes.Types;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;


public class StandardType extends Type  {

    @VisitNumber( number = 1 ) public final LexType type;
    

    public StandardType( Term term, LexType type ) {
        super(term);
        this.type = type;
        super.setup_ASTNodeQueue();
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of( type );
    }

    public String toString() {
        return "StandardType";
    }

    @Override
    public String getPrintableName() {
        return "STD_Type";
    }
    
}
