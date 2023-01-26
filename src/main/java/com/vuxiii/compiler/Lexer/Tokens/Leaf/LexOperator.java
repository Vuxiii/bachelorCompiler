package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import java.util.Optional;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

@VisitLeaf
public class LexOperator extends ASTNode {

    public final MatchInfo matchInfo;
    public final TokenType operator;

    public LexOperator( MatchInfo matchInfo, Term term, TokenType operator ) {
        super( term );
        this.matchInfo = matchInfo;

        this.operator = operator;

        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenPlus: " + operator;
    }
    @Override
    public String getPrintableName() {
        return operator.name();
    }
    
}
