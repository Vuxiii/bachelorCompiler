package com.vuxiii.compiler.Lexer.Tokens.Leaf;


import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

@VisitLeaf
public class LexOperator extends ASTNode {

    public final MatchInfo matchInfo;
    public final TokenType operator;

    public LexOperator( MatchInfo matchInfo, TokenType operator ) {
        super( operator.symbol );
        this.matchInfo = matchInfo;

        this.operator = operator;

        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenPlus: " + operator;
    }
    @Override
    public String getPrintableName() {
        return operator.name() + " ~ Line " + matchInfo.lineNumber();
    }
    
}
