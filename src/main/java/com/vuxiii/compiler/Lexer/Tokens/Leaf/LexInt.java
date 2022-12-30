package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;

public class LexInt implements ASTToken {
    
    public final int val;


    public LexInt( String val ) {
        this.val = Integer.parseInt( val );
        // this.val = Integer.parseInt( val );
    }

    public String toString() {
        return "int: " + val;
    }

    @Override
    public void accept(VisitorBase arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return Symbol.t_Integer;
    }
}
