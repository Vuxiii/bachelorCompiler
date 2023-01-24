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
public class LexSemicolon extends ASTNode {

    public final MatchInfo matchInfo;

    public LexSemicolon( MatchInfo matchInfo ) {
        super( Symbol.t_Semicolon );
        this.matchInfo = matchInfo;
        super.setup_ASTNodeQueue();
    }
    
    public String toString() {
        return "ASTTokenSemicolon: ;";
    }


    @Override
    protected Optional<ASTNode> getChild1() {
        return Optional.empty();
    }


    @Override
    protected Optional<ASTNode> getChild2() {
        return Optional.empty();
    }


    @Override
    protected Optional<ASTNode> getChild3() {
        return Optional.empty();
    }


    @Override
    protected Optional<ASTNode> getChild4() {
        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return "Semicolon";
    }
}
