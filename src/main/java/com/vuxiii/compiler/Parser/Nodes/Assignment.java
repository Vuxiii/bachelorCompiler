package com.vuxiii.compiler.Parser.Nodes;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexInt;

public class Assignment implements ASTToken {

    public final LexIdent id;
    public final ASTToken value;

    public final Term term;

    public Assignment( Term term, LexIdent id, ASTToken value ) {
        this.id = id;
        this.value = value;

        this.term = term;
    }

    @Override
    public void accept(VisitorBase arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return term;
    }

    public String toString() {
        return id.name + " = " + value.toString();
    }
    
}
