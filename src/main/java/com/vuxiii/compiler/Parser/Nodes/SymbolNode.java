package com.vuxiii.compiler.Parser.Nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Scope;

public class SymbolNode extends ASTNode {

    public static final Map<String, Scope> scopes = new HashMap<>();

    @VisitNumber( number = 1 ) public ASTNode child;
    
    public final String scope_name;
    public final Scope scope;

    public final Optional<SymbolNode> parent_scope;

    public SymbolNode( Term term, ASTNode child, String name, Scope scope, Optional<SymbolNode> parent_scope ) {
        super(term);
        this.child = child;
        this.scope_name = name;
        this.scope = scope;
        this.parent_scope = parent_scope;
        super.setup_ASTNodeQueue();
        scopes.put( name, scope );
    }
    
    @Override
    public String getPrintableName() {
        return "Symbol [" + scope_name + "] -> Vars: " + scope.get_variables() + " | Params: " + scope.get_parameters();
    }

    @Override
    public String toString() {
        return "SymbolNode";
    }

    
    

}
