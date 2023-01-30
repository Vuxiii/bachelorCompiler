package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;

@VisitLeaf
public class LexLCurly extends ASTNode {
    public final MatchInfo matchInfo;

    public LexLCurly( MatchInfo matchInfo, TokenType type ) {
        super( type.symbol );
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenLCurly: '{'";
    }

    @Override
    public String getPrintableName() {
        return "left_curly: '{'" + " ~ Line " + matchInfo.lineNumber();
    }
}
