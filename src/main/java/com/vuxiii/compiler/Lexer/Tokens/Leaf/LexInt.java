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
public class LexInt extends ASTNode {
    
    public final int val;
    public final MatchInfo matchInfo;

    public LexInt( MatchInfo matchInfo ) {
        super( Symbol.t_Integer );
        this.val = Integer.parseInt( matchInfo.str() );
        // this.val = Integer.parseInt( val );
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "int: " + val;
    }

    @Override
    public String getPrintableName() {
        return "int: " + val + " ~ Line " + matchInfo.lineNumber();
    }
}
