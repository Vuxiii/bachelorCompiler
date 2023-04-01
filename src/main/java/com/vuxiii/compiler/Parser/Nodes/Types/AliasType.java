package com.vuxiii.compiler.Parser.Nodes.Types;

import java.util.Optional;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.PrimitiveType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;


public class AliasType extends Type  {

    public final MatchInfo aliasInfo;

    @VisitNumber( number = 1 ) public final Type alias_type;

    public AliasType( Term term, MatchInfo aliasInfo, Type alias ) {
        super(term);
        this.alias_type = alias;
        this.aliasInfo = aliasInfo;
        super.setup_ASTNodeQueue();
    }

    @Override
    public boolean equals( Object other ) {
        if ( other == null ) return false;
        if ( other instanceof Type ) {
            alias_type.equals( other );
        } else if ( other instanceof PrimitiveType ) {
            return alias_type.equals(other);
        }
        return false;
    }


    public String toString() {
        return "AliasType";
    }

    @Override
    public String getPrintableName() {
        return "Alias_Type ";
    }
    
    @Override
    public String simple_type_name() {
        return aliasInfo.str();
    }

    @Override
    public int physical_size() {
        return alias_type.physical_size();
    }
    
    
}
