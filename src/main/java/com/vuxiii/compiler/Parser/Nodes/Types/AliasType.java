package com.vuxiii.compiler.Parser.Nodes.Types;

import java.util.Optional;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.PrimitiveType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;


public class AliasType extends Type  {

    public final MatchInfo aliasInfo;

    @VisitNumber( number = 1 ) public Optional<Type> alias_type = Optional.empty();

    public AliasType( Term term, MatchInfo aliasInfo, Type alias ) {
        super(term);
        this.alias_type = Optional.of(alias);
        this.aliasInfo = aliasInfo;
        super.setup_ASTNodeQueue();
    }

    public AliasType( Term term, MatchInfo aliasInfo ) {
        super(term);
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
        if ( alias_type.isPresent() )
            return alias_type.get().physical_size();
        return 0;
    }
}
