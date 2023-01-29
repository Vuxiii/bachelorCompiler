package com.vuxiii.compiler.Lexer.Tokens.Leaf;


import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.ConcreteType;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

@VisitLeaf
public class LexType extends ASTNode {
    public final ConcreteType type;

    public final MatchInfo matchInfo;

    public LexType( MatchInfo matchInfo, TokenType type ) {
        super( type.symbol ); 
        this.matchInfo = matchInfo;
        this.type = type.literal_type;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenType: " + type;
    }

    @Override
    public String getPrintableName() {
        return "Type: " + type + " ~ Line " + matchInfo.lineNumber();
    }
}
