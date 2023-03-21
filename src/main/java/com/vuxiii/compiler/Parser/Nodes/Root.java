
package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class Root extends ASTNode {

    @VisitNumber( number = 1 ) public ASTNode node;

    public Root( Term term, ASTNode node ) {
        super( term ); 
        this.node = node;
        this.node = node;
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "Root";
    }

    @Override
    public Optional<ASTNode> getChild1() {
        return Optional.of(node);
    }

    @Override
    public String getPrintableName() {
        return "Root Node";
    }
    
}
