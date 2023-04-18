package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Assignment extends ASTNode {

    @VisitNumber( number = 1 ) public ASTNode id;
    @VisitNumber( number = 2 ) public ASTNode value;

    public Assignment( Term term, LexIdent id, ASTNode value ) {
        super( term ); 
        this.id = id;
        this.value = value;
        super.setup_ASTNodeQueue();
    }

    public Assignment( Term term, NestedField id, ASTNode value ) {
        super( term ); 
        this.id = id;
        this.value = value;
        super.setup_ASTNodeQueue();
    }

    public String name() {
        if ( id instanceof LexIdent )
            return ((LexIdent)id).name;
        NestedField f = (NestedField)id;

        return f.name();
    }

    public String toString() {
        if ( id instanceof LexIdent )
            return ((LexIdent)id).name + " = " + value.toString();
        return ((NestedField)id).toString() + " = " + value.toString();
    }

    @Override
    public String getPrintableName() {
        return "Assignment " + getChildrenCount();
    }
    
}
