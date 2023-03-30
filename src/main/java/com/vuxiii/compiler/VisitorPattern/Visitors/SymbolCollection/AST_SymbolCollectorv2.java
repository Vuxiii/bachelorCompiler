package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;
import com.vuxiii.compiler.Parser.Nodes.Root;
import com.vuxiii.compiler.Parser.Nodes.ScopeNode;
import com.vuxiii.compiler.Parser.Nodes.SymbolNode;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_SymbolCollectorv2 extends VisitorBase {
    List<String> scope_stack = new ArrayList<>();

    Map<String, Scope> scope_map = new HashMap<>();

    Map<FunctionType, Scope> function_scope_map = new HashMap<>();

    public AST_SymbolCollectorv2() {
        scope_stack.add( "root" );
        scope_map.put( "root", new Scope() );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void init_root_scope( Root rootie ) {
        Scope new_scope = new Scope();
        SymbolNode node = new SymbolNode( Symbol.n_Scope, rootie.node, "root", new_scope, new_scope );
        rootie.node.parent = Optional.of( node );
        rootie.node = node;
        rootie.setup_ASTNodeQueue();
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 3 )
    public void init_function_scopes( Declaration func_decl ) {
        if ( !(func_decl.kind == DeclarationKind.FUNCTION) ) return;

        Scope parent_scope = _current_scope(func_decl);
        Scope new_scope = new Scope();
        SymbolNode node = new SymbolNode( Symbol.n_Scope, func_decl, "function " + func_decl.id.name, new_scope, parent_scope );
        node.parent = Optional.of( func_decl.parent.get() );
        func_decl.parent.get().replace_child_with(func_decl, node);
        func_decl.parent.get().setup_ASTNodeQueue();
        func_decl.parent = Optional.of( node ); 
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void init_function_scopes( Assignment func_assignment ) {
        if ( !(func_assignment.value instanceof FunctionType) ) return;
        
        Scope parent_scope = _current_scope(func_assignment);
        Scope new_scope = new Scope();
        SymbolNode node = new SymbolNode( Symbol.n_Scope, func_assignment, "function " + func_assignment.id.name, new_scope, parent_scope );
        node.parent = Optional.of( func_assignment.parent.get() );
        func_assignment.parent.get().replace_child_with(func_assignment, node);
        func_assignment.parent.get().setup_ASTNodeQueue();
        func_assignment.parent = Optional.of( node ); 
    }

    // @VisitorPattern( when = VisitOrder.ENTER_NODE )
    // public void new_scope( Assignment func ) {
    //     if ( !(func.value instanceof FunctionType) ) return;

    //     _current_scope().add_variable( func.id );

    //     function_scope_map.put( (FunctionType)func.value, new Scope() );
    // }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void collect_var( Declaration decl ) {
        if ( decl.kind != DeclarationKind.VARIABLE ) return;
        _current_scope( decl ).add_variable( decl.id );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2 )
    public void collect_param( Declaration decl ) {
        if ( decl.kind != DeclarationKind.PARAMETER ) return;
        _current_scope( decl ).add_parameter( decl.id );
    }

    // private Scope _parent_scope() {
    //     return scope_map.get( scope_stack.get( Math.max( 0, scope_stack.size()-2 ) ) );
    // }

    // private Scope _current_func_scope( ASTNode n ) {
    //     ASTNode current = n;
    //     boolean found = false;
    //     while ( !(current instanceof Assignment) && found == false ) {
    //         current = n.parent.get();
    //         if ( current instanceof Assignment ) {
    //             found = function_scope_map.containsKey(((Assignment)current).value);
    //         }
    //     }
    //     return function_scope_map.get( current );
    // }

    private Scope _current_scope( ASTNode current ) {
        while ( !(current instanceof SymbolNode) ) {
            if ( current instanceof Root ) {
                System.out.println( "At root. Lmao" );
                System.exit(-1);
            }
            current = current.parent.get();
        }
        System.out.println( "Found scope " + ((SymbolNode)current).scope_name);
        return ((SymbolNode)current).scope;
    }
}
