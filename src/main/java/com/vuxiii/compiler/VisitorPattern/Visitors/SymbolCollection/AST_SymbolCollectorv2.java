package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Error.Error;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;
import com.vuxiii.compiler.Parser.Nodes.Root;
import com.vuxiii.compiler.Parser.Nodes.SymbolNode;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_SymbolCollectorv2 extends VisitorBase {

    public List<Assignment> functions; // pointer to root's function list

    List<String> scope_stack = new ArrayList<>();

    Map<String, Scope> scope_map;

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void init_root_scope( Root rootie ) {
        Scope new_scope = new Scope();
        SymbolNode node = new SymbolNode( Symbol.n_Scope, rootie.node, "root", new_scope, Optional.empty() );

        rootie.node.parent = Optional.of( node );
        rootie.node = node;
        rootie.setup_ASTNodeQueue();

        functions = rootie.functions;
        scope_map = rootie.scope_map;

        scope_map.put( "root", new_scope );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 3 )
    public void init_function_scopes( Declaration func_decl ) {
        if ( !(func_decl.kind == DeclarationKind.FUNCTION) ) return;

        SymbolNode parent_scope = _current_symbol_node(func_decl);

        parent_scope.scope.add_variable( func_decl.id );


        Scope new_scope = new Scope();
        SymbolNode node = new SymbolNode( Symbol.n_Scope, func_decl, "function " + func_decl.id.name, new_scope, Optional.of(parent_scope) );
        node.parent = Optional.of( func_decl.parent.get() );
        func_decl.parent.get().replace_child_with(func_decl, node);
        func_decl.parent.get().setup_ASTNodeQueue();
        func_decl.parent = Optional.of( node ); 
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void init_function_scopes( Assignment func_assignment ) {
        if ( !(func_assignment.value instanceof FunctionType) ) return;
        
        SymbolNode parent_scope = _current_symbol_node(func_assignment);
        Scope new_scope = new Scope();

        new_scope.add_variable( func_assignment.id );
        System.out.println( "Adding " + func_assignment.id + " to scope " + new_scope);

        SymbolNode node = new SymbolNode( Symbol.n_Scope, func_assignment, "function " + func_assignment.id.name, new_scope, Optional.of(parent_scope) );
        node.parent = Optional.of( func_assignment.parent.get() );
        func_assignment.parent.get().replace_child_with(func_assignment, node);
        func_assignment.parent.get().setup_ASTNodeQueue();
        func_assignment.parent = Optional.of( node ); 

        functions.add( func_assignment );

        scope_map.put( "function " + func_assignment.id.name, new_scope );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void collect_var( Declaration decl ) {
        if ( decl.kind != DeclarationKind.VARIABLE ) return;
        _current_scope( decl ).add_variable( decl.id );
        System.out.println( "Adding " + decl.id + " to scope " + _current_scope(decl));
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2 )
    public void collect_param( Declaration decl ) {
        if ( decl.kind != DeclarationKind.PARAMETER ) return;
        _current_scope( decl ).add_parameter( decl.id );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void check_use_before_decl( LexIdent ident ) {
        if ( ident.parent.get() instanceof Declaration ) return;
        SymbolNode current_node = _current_symbol_node(ident);

        while( current_node.scope.can_access( ident.name ) == false ) {
            if ( current_node.parent_scope.isEmpty()) {
                System.out.println( new Error( "Symbol Collection Error", "Tried to access undeclared variable '" + ident.name + "' on line " + ident.matchInfo.lineNumber() + " column " + ident.matchInfo.columnNumber() ));
                break; // Should exit here!
            } else {
                current_node = current_node.parent_scope.get();
            }

        }
    }

    private SymbolNode _current_symbol_node( ASTNode current ) {
        while ( !(current instanceof SymbolNode) ) {
            if ( current instanceof Root ) {
                System.out.println( "At root. Lmao" );
                System.exit(-1);
            }
            current = current.parent.get();
        }
        return (SymbolNode)current;
    }

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
