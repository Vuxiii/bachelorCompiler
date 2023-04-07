package com.vuxiii.compiler.Parser.Nodes;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Field extends ASTNode {


    @VisitNumber( number = 1 ) public Declaration field;

    public Field( Term term, Declaration field ) {
        super( term );
        this.field = field;
        super.setup_ASTNodeQueue();
    }
    
    public String toString() {
        return "Field";
    }

    @Override
    public String getPrintableName() {
        return "Field";
    }
}
