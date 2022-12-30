package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;

public class LexPrint implements ASTToken {

    @Override
    public void accept(VisitorBase arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return Symbol.t_Print;
    }
    
    public String toString() {
        return "ASTTokenPrint";
    }
}
