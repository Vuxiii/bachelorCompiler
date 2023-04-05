package com.vuxiii.compiler.Parser.Nodes;

import java.util.ArrayList;
import java.util.List;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class FieldList extends ASTNode {

    @VisitNumber( number = 1 ) public final List<Field> fields;

    public FieldList( Term term ) {
        super( term ); 
        fields = new ArrayList<>();
        super.setup_ASTNodeQueue();
    }

    public void push( Field stmt ) {
        fields.add( stmt );
    }

    public String toString() {
        return fields.toString();
    }

    @Override
    public String getPrintableName() {
        return "Field_List";
    }
    
}
