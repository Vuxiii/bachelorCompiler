package com.vuxiii.compiler.Parser.Nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Symbols;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.HeapLayout;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.StackFrame;

public class SymbolNode extends ASTNode {

    public static final Map<String, Symbols> scopes = new HashMap<>();

    @VisitNumber( number = 1 ) public ASTNode child;
    
    public final String scope_name;
    public final Symbols scope;
    public final Optional<SymbolNode> parent_scope;

    public StackFrame stack_frame = new StackFrame();

    public Map<String, HeapLayout> heap_layouts = new HashMap<>();

    public SymbolNode( Term term, ASTNode child, String name, Symbols scope, Optional<SymbolNode> parent_scope ) {
        super(term);
        this.child = child;
        this.scope_name = name;
        this.scope = scope;
        this.parent_scope = parent_scope;

        super.setup_ASTNodeQueue();
        scopes.put( name, scope );
    }

    public void register_pointer( LexIdent pointer ) {
        
    }
    
    @Override
    public String getPrintableName() {
        return "Symbol [" + scope_name + "] -> Vars: " + scope.get_variables() + " | Params: " + scope.get_parameters() + " | Pointers " + stack_frame.pointer_offsets;
    }

    @Override
    public String toString() {
        return "SymbolNode";
    }

    public HeapLayout get_heap_layout( String var_name ) {
        System.out.println("-----------------------");
        System.out.println( heap_layouts );
        System.out.println( var_name );
        int i = var_name.lastIndexOf( "." );
        System.out.println( var_name.substring(0, i) );
        String new_string = var_name.substring(0, i);
        if ( new_string.contains(".")) {
            return heap_layouts.get( new_string.substring( new_string.indexOf("."), i) );
        } else {
            return heap_layouts.get( new_string );
        }
    }

    
    

}
