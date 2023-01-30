package com.vuxiii.compiler.Lexer.Tokens.Leaf;


import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.PrimitiveType;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;

@VisitLeaf
public class LexTypeDecl extends ASTNode {

    public final MatchInfo matchInfo;

    public LexTypeDecl( MatchInfo matchInfo, TokenType type ) {
        super( type.symbol ); 
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenTypeDecl";
    }

    @Override
    public String getPrintableName() {
        return "TypeDecl: ~ Line " + matchInfo.lineNumber();
    }
}
