package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Print extends ASTNode {

    @VisitNumber( number = 1 ) public ASTNode value;
    @VisitNumber( number = 2 ) public Optional<Argument> arg_list = Optional.empty();

    public final PrintKind kind;

    public Print( Term term, ASTNode value) {
        super( term ); 
        this.value = value;
        this.kind = PrintKind.STRING;
        super.setup_ASTNodeQueue();
    }
    public Print( Term term, ASTNode string_literal, Argument args ) {
        super( term ); 
        this.value = string_literal;
        this.arg_list = Optional.of( args );
        this.kind = PrintKind.STRING_SUBSTITUTE;

        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return value.toString();
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(value);
    }

    @Override
    public Optional<ASTNode> getChild2() {
        if ( arg_list.isPresent() )
            return Optional.of(arg_list.get());
        return Optional.empty();
    }

    @Override
    public String getPrintableName() {
        return "Print";
    }
    
}
