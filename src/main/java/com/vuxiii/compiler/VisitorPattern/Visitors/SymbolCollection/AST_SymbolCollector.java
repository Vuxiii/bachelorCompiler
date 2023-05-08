package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Error.Error;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.Parser.Nodes.FieldList;
import com.vuxiii.compiler.Parser.Nodes.Root;
import com.vuxiii.compiler.Parser.Nodes.SymbolNode;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.Parser.Nodes.Types.RecordType;
import com.vuxiii.compiler.Parser.Nodes.Types.UserType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_SymbolCollector extends VisitorBase {

    public List<Assignment> functions; // pointer to root's function list

    List<String> scope_stack = new ArrayList<>();

    Map<String, Symbols> scope_map;

    Set<RecordType> visited_records = new HashSet<>();

    Set<ASTNode> ignore_decl = new HashSet<>();


    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void init_root_scope( Root rootie ) {
        Symbols new_scope = new Symbols( );
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

        SymbolNode parent_scope = current_symbol_node(func_decl);

        parent_scope.scope.add_variable( func_decl.id, func_decl.kind == DeclarationKind.POINTER );


        Symbols new_scope = new Symbols(  );
        SymbolNode node = new SymbolNode( Symbol.n_Scope, func_decl, "function " + func_decl.id.name, new_scope, Optional.of(parent_scope) );
        node.parent = Optional.of( func_decl.parent.get() );
        func_decl.parent.get().replace_child_with(func_decl, node);
        func_decl.parent.get().setup_ASTNodeQueue();
        func_decl.parent = Optional.of( node ); 
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void init_function_scopes( Assignment func_assignment ) {
        if ( !(func_assignment.value instanceof FunctionType) ) return;
        if ( !(func_assignment.id instanceof LexIdent) ) return;
        SymbolNode parent_scope = current_symbol_node(func_assignment);
        Symbols new_scope = new Symbols();

        new_scope.add_variable( (LexIdent)func_assignment.id, false ); // Name of function
        System.out.println( "Adding " + func_assignment.id + " to scope " + new_scope);

        SymbolNode node = new SymbolNode( Symbol.n_Scope, func_assignment, "function " + func_assignment.name(), new_scope, Optional.of(parent_scope) );
        node.parent = Optional.of( func_assignment.parent.get() );
        func_assignment.parent.get().replace_child_with(func_assignment, node);
        func_assignment.parent.get().setup_ASTNodeQueue();
        func_assignment.parent = Optional.of( node ); 

        functions.add( func_assignment );

        scope_map.put( "function " + func_assignment.name(), new_scope );

        new_scope.register_static_scope( parent_scope.scope );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void collect_var( Declaration decl ) {
        if ( decl.kind != DeclarationKind.VARIABLE && decl.kind != DeclarationKind.POINTER ) return;
        if ( decl.parent.get() instanceof Field ) return;
        // if ( ignore_decl.contains(decl) ) return;

        Symbols scope = current_scope( decl );

        if ( decl.type instanceof RecordType ) {
            scope.add_fields(decl);
        } else {
            scope.add_variable( decl.id, decl.kind == DeclarationKind.POINTER );
        }
        
        // if ( decl.kind == DeclarationKind.HEAP ) {
        //     current_scope( decl ).identifier_is_heap_allocated( decl.id.name );
        //     decl.layout = new ScopeLayout();
        // }
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2 )
    public void collect_param( Declaration decl ) {
        if ( decl.kind != DeclarationKind.PARAMETER ) return;
        current_scope( decl ).add_parameter( decl.id, decl.kind == DeclarationKind.POINTER );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void check_use_before_decl( LexIdent ident ) {
        if ( ident.parent.get() instanceof Declaration ) return;
        SymbolNode current_node = current_symbol_node(ident);

        while( current_node.scope.can_access( ident.name ) == false ) {
            if ( current_node.parent_scope.isEmpty()) {
                System.out.println( new Error( "Symbol Collection Error", "Tried to access undeclared variable '" + ident.name + "' on line " + ident.matchInfo.lineNumber() + " column " + ident.matchInfo.columnNumber() ));
                break; // Should exit here!
            } else {
                current_node = current_node.parent_scope.get();
            }

        }
    }

    public static SymbolNode current_symbol_node( ASTNode current ) {
        while ( !(current instanceof SymbolNode) ) {
            if ( current instanceof Root ) {
                System.out.println( "At root. Lmao" );
                System.exit(-1);
            }
            current = current.parent.get();
        }
        return (SymbolNode)current;
    }

    public static Symbols current_scope( ASTNode current ) {
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
