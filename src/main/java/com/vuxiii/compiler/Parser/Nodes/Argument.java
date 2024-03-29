package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Argument extends ASTNode {


    @VisitNumber( number = 1 ) public ASTNode node; // Not ident because this is an actual argument. It could be a literal!

    @VisitNumber( number = 2 ) public Optional<Argument> next;

    public final ArgumentKind kind;


    public Argument( Term term, ASTNode node, ArgumentKind kind ) {
        super( term ); 
        this.node = node;
        this.kind = kind;
        next = Optional.empty();
        super.setup_ASTNodeQueue();
    }

    public Argument( Term term, ASTNode node, Argument next, ArgumentKind kind ) {
        super( term ); 
        this.node = node;
        this.kind = kind;
        this.next = Optional.of(next);
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        if ( next.isEmpty() )
            return "(Arg " + kind + ": " + node.toString() + ")";
        return "(Arg " + kind + ": " + node.toString() + "); " + next.get().toString();
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(node);
    }

    @Override
    public Optional<ASTNode> getChild2() {
        if ( next.isPresent() )
            return Optional.of( next.get() );

        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return (next.isEmpty()  ? "Argument" 
                                : "Argument_List");
    }
    
}
