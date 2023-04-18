package com.vuxiii.compiler.Parser.Nodes.Types;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;


public class UnknownType extends Type  {

    // public final MatchInfo aliasInfo;

    @VisitNumber( number = 1 ) public LexIdent unknown_type;

    public UnknownType( Term term, LexIdent identifier ) {
        super(term);
        this.unknown_type = identifier;
        super.setup_ASTNodeQueue();
    }

    @Override
    public boolean equals( Object other ) {
        return false;
    }

    public String toString() {
        return "UnknownType";
    }

    @Override
    public String getPrintableName() {
        return "Unknown_Type ";
    }
    
    @Override
    public String simple_type_name() {
        return unknown_type.name;
    }

    @Override
    public int physical_size() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'physical_size'");
    }

}
