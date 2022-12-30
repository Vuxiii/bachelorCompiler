package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;

public class LexIdent implements ASTToken {
    public final String name;

    public LexIdent( String name ) {
        this.name = name;
    }

    public String toString() {
        return "id: " + name;
    }

    @Override
    public void accept(VisitorBase arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return Symbol.t_Identifier;
    }
}
