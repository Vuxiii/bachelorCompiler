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
public class LexIdent extends ASTNode {
    public final String name;
    public final MatchInfo matchInfo;

    public LexIdent( MatchInfo matchInfo ) {
        super( Symbol.t_Identifier );
        this.name = matchInfo.str();
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "id: " + name;
    }

    @Override
    public String getPrintableName() {
        return "id: " + name;
    }
}
