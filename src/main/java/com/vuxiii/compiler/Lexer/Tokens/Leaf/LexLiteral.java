package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.ConcreteType;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

@VisitLeaf
public class LexLiteral extends ASTNode {
    
    public final String val;
    public final MatchInfo matchInfo;

    public final ConcreteType literal_type;

    public LexLiteral( MatchInfo matchInfo, TokenType type ) {
        super( type.symbol );
        this.val =  matchInfo.str();
        this.literal_type = type.literal_type;
        this.matchInfo = matchInfo;

        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "Literal: " + val + " :: " + literal_type;
    }

    @Override
    public String getPrintableName() {
        return "Literal: " + val + " :: " + literal_type;
    }
}
