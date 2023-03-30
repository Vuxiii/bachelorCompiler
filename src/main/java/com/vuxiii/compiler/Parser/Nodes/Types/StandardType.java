package com.vuxiii.compiler.Parser.Nodes.Types;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.PrimitiveType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;


public class StandardType extends Type  {

    @VisitNumber( number = 1 ) public final LexType type;
    

    public StandardType( Term term, LexType type ) {
        super(term);
        this.type = type;
        super.setup_ASTNodeQueue();
    }

    @Override
    public boolean equals( Object other ) {
        if ( other == null ) return false;
        if ( other instanceof PrimitiveType ) {
            return type.type.equals(other);
        } else if ( other instanceof Type ) {
            if ( other instanceof StandardType ) {
                StandardType o = (StandardType)other;
                return type.equals(o.type);
            } else if ( other instanceof FunctionType ) {
                return false;
            } else if ( other instanceof AliasType ) {
                AliasType o = (AliasType)other;
                return equals(o.alias_type);
            } else if ( other instanceof UnknownType ) {
                return false;
            } else if ( other instanceof UserType ) {
                return false;
            }
        }
        return false;
    }

    public String toString() {
        return "StandardType";
    }

    @Override
    public String getPrintableName() {
        return "STD_Type";
    }
    
    @Override
    public String simple_type_name() {
        return type.type.name;
    }
}
