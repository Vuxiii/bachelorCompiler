package com.vuxiii.compiler.Lexer.Tokens.Leaf;


import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;


@VisitLeaf
public class LexEqual extends ASTNode {

    public final MatchInfo matchInfo;
    
    public LexEqual( MatchInfo matchInfo, TokenType type ) {
        super( type.symbol );
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenEqual: =";
    }
    @Override
    public String getPrintableName() {
        return "Equals" + " ~ Line " + matchInfo.lineNumber();
    }
    
}
