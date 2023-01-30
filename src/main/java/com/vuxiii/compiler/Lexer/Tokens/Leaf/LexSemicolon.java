package com.vuxiii.compiler.Lexer.Tokens.Leaf;


import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;

@VisitLeaf
public class LexSemicolon extends ASTNode {

    public final MatchInfo matchInfo;

    public LexSemicolon( MatchInfo matchInfo, TokenType type ) {
        super( type.symbol );
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }
    
    public String toString() {
        return "ASTTokenSemicolon: ;";
    }



    @Override
    public String getPrintableName() {
        return "Semicolon" + " ~ Line " + matchInfo.lineNumber();
    }
}
