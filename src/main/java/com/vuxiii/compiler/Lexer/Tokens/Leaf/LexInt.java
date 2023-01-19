package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import java.util.Optional;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Visitors.ASTNode;

public class LexInt extends ASTNode {
    
    public final int val;
    public final MatchInfo matchInfo;

    public LexInt( MatchInfo matchInfo ) {
        this.val = Integer.parseInt( matchInfo.str() );
        // this.val = Integer.parseInt( val );
        this.matchInfo = matchInfo;
    }

    public String toString() {
        return "int: " + val;
    }

    
    @Override
    public void accept(VisitorBase visitor) {
        acceptNoChild(visitor);      
    }

    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return Symbol.t_Integer;
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
        return "int: " + val;
    }
}
