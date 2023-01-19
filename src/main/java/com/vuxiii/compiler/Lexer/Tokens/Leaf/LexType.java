package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import java.util.Optional;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Lexer.Tokens.ConcreteType;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Visitors.ASTNode;

public class LexType extends ASTNode {
    public final ConcreteType type;

    public final MatchInfo matchInfo;

    public LexType( MatchInfo matchInfo, ConcreteType type ) {
        this.matchInfo = matchInfo;
        this.type = type;
    }

    public String toString() {
        return "ASTTokenType: " + type;
    }

    
    @Override
    public void accept(VisitorBase visitor) {
        acceptNoChild(visitor);      
    }

    //! TODO: UPDATE THIS RETURN SYMBOL..
    @Override
    public Term getTerm() {
        // TODO Auto-generated method stub
        return Symbol.t_TypeInt;
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
        return "Type: " + type;
    }
}
