package com.vuxiii.compiler.Lexer.Tokens.Leaf;

import java.util.Optional;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Lexer.Tokens.ConcreteType;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

@VisitLeaf
public class LexType extends ASTNode {
    public final ConcreteType type;

    public final MatchInfo matchInfo;

    public LexType( MatchInfo matchInfo, ConcreteType type ) {
        super( Symbol.t_TypeInt ); //! TODO: UPDATE THIS RETURN SYMBOL..
        this.matchInfo = matchInfo;
        this.type = type;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenType: " + type;
    }

    @Override
    public String getPrintableName() {
        return "Type: " + type;
    }
}
