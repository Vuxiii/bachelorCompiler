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
public class LexEqual extends ASTNode {

    public final MatchInfo matchInfo;
    
    public LexEqual( MatchInfo matchInfo ) {
        super( Symbol.t_Equals );
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenEqual: =";
    }
    @Override
    public String getPrintableName() {
        return "Equals";
    }
    
}
