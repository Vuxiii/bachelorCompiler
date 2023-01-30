package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;

@VisitLeaf
public class LexRBracket extends ASTNode {
    public final MatchInfo matchInfo;

    public LexRBracket( MatchInfo matchInfo, TokenType type ) {
        super( type.symbol );
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenRBracket: ']'";
    }

    @Override
    public String getPrintableName() {
        return "right_bracket: ']'" + " ~ Line " + matchInfo.lineNumber();
    }
}
