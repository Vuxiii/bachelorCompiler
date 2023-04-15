
package com.vuxiii.compiler.Parser.Nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection.StringNode;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.ScopeLayout;

public class Root extends ASTNode {

    @VisitNumber( number = 1 ) public ASTNode node;

    public Map<Print, StringNode> strings;
    public List<Assignment> functions;

    public Map<String, ScopeLayout> scope_map;


    public Root( Term term, ASTNode node ) {
        super( term ); 
        this.node = node;
        this.node = node;
        super.setup_ASTNodeQueue();

        strings = new HashMap<>();
        functions = new ArrayList<>();
        scope_map = new HashMap<>();
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
