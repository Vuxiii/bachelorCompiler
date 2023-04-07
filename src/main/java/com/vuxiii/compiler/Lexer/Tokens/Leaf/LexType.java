package com.vuxiii.compiler.Lexer.Tokens.Leaf;


import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.PrimitiveType;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.Parser.Nodes.Types.Type;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;

@VisitLeaf
public class LexType extends Type {
    public final PrimitiveType type;

    public final MatchInfo matchInfo;

    public LexType( MatchInfo matchInfo, TokenType type ) {
        super( type.symbol ); 
        this.matchInfo = matchInfo;
        this.type = type.literal_type;
        super.setup_ASTNodeQueue();
    }

    public boolean equals( Object other ) {
        if ( other == null ) return false;
        if ( other instanceof LexType ) {
            return type.equals( ((LexType)other).type );
        } else if ( other instanceof PrimitiveType ) {
            return type.equals(other);
        }
        return false;
    }

    public String toString() {
        return "ASTTokenType: " + type;
    }

    @Override
    public String getPrintableName() {
        return type + " ~ Line " + matchInfo.lineNumber();
    }
    
    @Override
    public String simple_type_name() {
        return type.name;
    }

    @Override
    public int physical_size() {
        switch (type) {
            case VOID:
                return 0;
            case STRING:
                return matchInfo.str().length()-2; // Maybe not -2 for the '"'
            default:
                return 8;
        }
    }
}
