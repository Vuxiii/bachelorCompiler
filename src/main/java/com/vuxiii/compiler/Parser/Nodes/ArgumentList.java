package com.vuxiii.compiler.Parser.Nodes;

import java.util.ArrayList;
import java.util.List;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class ArgumentList extends ASTNode {

    @VisitNumber( number = 1 ) public List<Argument> args; 

    public ArgumentList( Term term ) {
        super( term ); 
        this.args = new ArrayList<>();
        super.setup_ASTNodeQueue();
    }

    public void push( Argument stmt ) {
        args.add( stmt );
    }

    public String toString() {
        return args.toString();
    }

    @Override
    public String getPrintableName() {
        return "Arg_List";
    }
    
}