package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import java.util.Optional;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

@VisitLeaf
public class LexRParen extends ASTNode {

    public final MatchInfo matchInfo;

    public LexRParen( MatchInfo matchInfo ) {
        super( Symbol.t_RParen );
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenRParen: ')'";
    }

    @Override
    public String getPrintableName() {
        return "right_paren: ')'" + " ~ Line " + matchInfo.lineNumber();
    }
    
}
