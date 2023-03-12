package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;

@VisitLeaf
public class LexIf extends ASTNode {
    public final String name;
    public final MatchInfo matchInfo;

    public LexIf( MatchInfo matchInfo, TokenType type ) {
        super( type.symbol );
        this.name = matchInfo.str();
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "if: " + name;
    }

    @Override
    public String getPrintableName() {
        return "if: " + name + " ~ Line " + matchInfo.lineNumber();
    }
}
