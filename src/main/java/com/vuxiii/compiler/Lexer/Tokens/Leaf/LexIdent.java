package com.vuxiii.compiler.Lexer.Tokens.Leaf;


import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

@VisitLeaf
public class LexIdent extends ASTNode {
    public final String name;
    public final MatchInfo matchInfo;

    public LexIdent( MatchInfo matchInfo, TokenType type ) {
        super( type.symbol );
        this.name = matchInfo.str();
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "id: " + name;
    }

    @Override
    public String getPrintableName() {
        return "id: " + name + " ~ Line " + matchInfo.lineNumber();
    }
}
