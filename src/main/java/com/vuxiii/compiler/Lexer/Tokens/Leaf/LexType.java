package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;

public class LexType implements ASTToken {
    public final TokenType type;

    public LexType( String type ) {
        switch (type) {
            case "int":
                this.type = TokenType.INT;
                break;
        
            default:
                this.type = TokenType.UNKNOWN;
                break;
        }
    }

    public String toString() {
        return "ASTTokenType: " + type;
    }

    @Override
    public void accept(VisitorBase arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return Symbol.t_TypeInt;
    }
}
