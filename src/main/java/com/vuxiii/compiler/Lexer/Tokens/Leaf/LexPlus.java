package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import java.util.Optional;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Visitors.ASTNode;

public class LexPlus extends ASTNode {

    public final MatchInfo matchInfo;

    public LexPlus( MatchInfo matchInfo ) {
        this.matchInfo = matchInfo;
    }

    
    @Override
    public void accept(VisitorBase visitor) {
        acceptNoChild(visitor);      
    }

    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return Symbol.t_Plus;
    }

    public String toString() {
        return "ASTTokenPlus: +";
    }


    @Override
    protected Optional<ASTNode> getChild1() {
        // TODO Auto-generated method stub
        return Optional.empty();
    }


    @Override
    protected Optional<ASTNode> getChild2() {
        // TODO Auto-generated method stub
        return Optional.empty();
    }


    @Override
    protected Optional<ASTNode> getChild3() {
        // TODO Auto-generated method stub
        return Optional.empty();
    }


    @Override
    protected Optional<ASTNode> getChild4() {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public int getChildrenCount() {
        return 0;
    }

    @Override
    public String getPrintableName() {
        return "Plus";
    }
    
}
