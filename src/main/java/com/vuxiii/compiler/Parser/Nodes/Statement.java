package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Statement extends ASTNode {

    @VisitNumber( number = 1 ) public final ASTNode node;

    @VisitNumber( number = 2 ) public final Optional<Statement> next;

    public final StatementKind kind;

    public Statement( Term term, ASTNode node, StatementKind kind ) {
        super( term ); 
        this.node = node;
        next = Optional.empty();
        this.kind = kind;
        super.setup_ASTNodeQueue();
    }

    public Statement( Term term, ASTNode node, Statement next, StatementKind kind ) {
        super( term ); 
        this.node = node;
        this.next = Optional.of(next);
        this.kind = kind;
        super.setup_ASTNodeQueue();
    }

    public static Statement make_if_else( Term term, Statement if_block, Statement else_block ) {
        return new Statement( term, if_block, else_block, StatementKind.IF_ELSE );
    }


    public static Statement make_if_else_if( Term term, Statement if_block, Statement else_block ) {
        return new Statement( term, if_block, else_block, StatementKind.IF_ELSE_IF );
    }

    public static Statement make_if( Term term, Expression guard, Statement statement ) {
        return new Statement( term, guard, statement, StatementKind.IF );
    }

    public static Statement make_else( Term term, Expression guard, Statement statement ) {
        return new Statement( term, guard, statement, StatementKind.ELSE );
    }

    public String toString() {
        if ( next.isEmpty() )
            return "(" + kind + " " + node.toString() + ")";
        return "(" + kind + " " + node.toString() + "); " + next.get().toString();
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
    public Optional<ASTNode> getChild3() {
        return Optional.empty();
    }

    @Override
    public Optional<ASTNode> getChild4() {
        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return (next.isEmpty()  ? "Statement" 
                                : "Statement_List");
    }
    
}
