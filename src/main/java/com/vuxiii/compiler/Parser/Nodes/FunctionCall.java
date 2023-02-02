
package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class FunctionCall extends ASTNode {

    @VisitNumber( number = 1 ) public final LexIdent func_name;
    @VisitNumber( number = 2 ) public final Optional<Argument> args;



    public FunctionCall( Term term, LexIdent func_name ) {
        super( term ); 
        this.func_name = func_name;
        this.args = Optional.empty();
        super.setup_ASTNodeQueue();
    }

    public FunctionCall( Term term, LexIdent func_name, Argument args ) {
        super( term ); 
        this.func_name = func_name;
        this.args = Optional.of(args);
        super.setup_ASTNodeQueue();
    }


    public String toString() {
        return "(Function_Call " + func_name.toString() + ")";
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(func_name);
    }


    @Override
    public Optional<ASTNode> getChild2() {
        if ( args.isPresent() )
            return Optional.of(args.get());
        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return "Function_Call";
    }
    
}
