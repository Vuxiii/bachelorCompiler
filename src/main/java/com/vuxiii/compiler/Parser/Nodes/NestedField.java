package com.vuxiii.compiler.Parser.Nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class NestedField extends ASTNode {

    @VisitNumber( number = 1 ) public List<LexIdent> idents = new LinkedList<>();

    public NestedField( Term term, LexIdent id ) {
        super( term ); 
        idents.add( id );
        super.setup_ASTNodeQueue();
    }


    public void addFront( LexIdent id ) {
        idents.add(0, id );
        super.setup_ASTNodeQueue();
    }

    public void push( LexIdent id ) {
        idents.add( id );
        super.setup_ASTNodeQueue();
    }

    public String name() {
        String out = idents.stream().map( id -> id.name + "." ).reduce("", (o, n) -> o + n);
        return out.substring(0, out.length()-1);
    }

    public String toString() {
        return name();
    }

    @Override
    public String getPrintableName() {
        return name();
    }
    
}
