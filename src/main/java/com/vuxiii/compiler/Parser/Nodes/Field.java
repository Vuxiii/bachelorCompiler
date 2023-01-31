package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Field extends ASTNode {


    @VisitNumber( number = 1 ) public final ASTNode field;

    @VisitNumber( number = 2 ) public final Optional<Field> next;

    public Field( Term term, ASTNode field ) {
        super( term );
        this.field = field;
        this.next = Optional.empty();

        super.setup_ASTNodeQueue();
    }
    
    public Field( Term term, ASTNode field, Field next ) {
        super( term );
        this.field = field;
        this.next = Optional.of(next);

        super.setup_ASTNodeQueue();
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of( field );
    }

    @Override
    public Optional<ASTNode> getChild2() {
        if ( next.isPresent() )
            return Optional.of( next.get() );
        return Optional.empty();
    }


    public String toString() {
        return "Field_List";
    }

    @Override
    public String getPrintableName() {
        if ( next.isPresent() )
            return "Field_List";
        return "Field";
    }
}
