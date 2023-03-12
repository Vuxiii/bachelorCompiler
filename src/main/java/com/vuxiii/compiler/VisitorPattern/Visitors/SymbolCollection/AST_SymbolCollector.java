package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Error.Error;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Parser.Nodes.Argument;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;
import com.vuxiii.compiler.Parser.Nodes.Parameter;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_SymbolCollector extends VisitorBase {
    
    public List<Assignment> functions;

    public Map<String, Scope> scope_map = new HashMap<>();



    public List<Scope> scopes;
    private int current_index = 0;

    private boolean checking_capture = false;

    public String current_scope_name = "root";
    public String prev_scope_name = "root"; // make it a stack. 

    public AST_SymbolCollector(  ) {
        Scope main_scope = new Scope();
        scopes = new ArrayList<>();
        scopes.add( main_scope );

        scope_map.put( current_scope_name, main_scope );

        functions = new ArrayList<>();
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void setup_parameter_mode( Declaration decl_node ) {
        if ( decl_node.kind != DeclarationKind.FUNCTION && decl_node.kind != DeclarationKind.NEW_FUNCTION_TYPE ) return;

        scope_map.put( decl_node.id.name, new Scope() );

        scope_map.get( current_scope_name ).add_variable( decl_node.id );

        prev_scope_name = current_scope_name;
        current_scope_name = decl_node.id.name;

    }
    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 4 )
    public void exit_parameter_mode( Declaration decl_node ) {
        if ( decl_node.kind != DeclarationKind.FUNCTION && decl_node.kind != DeclarationKind.NEW_FUNCTION_TYPE ) return;

        current_scope_name = prev_scope_name;

    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void parameter_mode( Parameter param ) {
        LexIdent id = param.param.id;
        Scope sc = scope_map.get( current_scope_name );
        sc.add_parameter( id );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void make_function_mode( Assignment assignment_node ) {
        if ( !(assignment_node.value instanceof FunctionType) ) return;

        functions.add( assignment_node );

        prev_scope_name = current_scope_name;
        current_scope_name = assignment_node.id.name;
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void pop_function_mode( Assignment assignment_node ) {
        if ( !(assignment_node.value instanceof FunctionType) ) return;
        
        current_scope_name = prev_scope_name;
    }


    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void check_literal_in_capture( Argument arg ) {
        if ( !checking_capture ) return;
        if ( !(arg.node instanceof LexLiteral) ) return;

        System.out.println( new Error( "Symbol Collection Error", "Unexpected literal '" + ((LexLiteral)arg.node).val + "' on line " + ((LexLiteral)arg.node).matchInfo.lineNumber() + "\nOnly variables are allowed inside a capture block!" ));
        System.out.println( "Current Scope: " + current_scope_name );
        System.out.println( scope_map.get( current_scope_name ) );
        System.exit(-1);
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2 )
    public void use_before_declared_check( Argument arg ) {

        if ( !(arg.node instanceof LexIdent) ) return; // It is a literal. Therefore we are ALWAYS allowed to access it.

        LexIdent node = (LexIdent) arg.node;
        
        if ( parent_scope().can_access( node.name ) == true ) return;

        // If above fails, return Error
        System.out.println( new Error( "Symbol Collection Error", "Tried to access illegal variable '" + node.name + "' on line " + node.matchInfo.lineNumber() + " column " + node.matchInfo.columnNumber() ));
        System.out.println( "Current Scope: " + current_scope_name );
        System.out.println( scope_map.get( current_scope_name ) );
        System.exit(-1);
    }


    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2 )
    public void collect_variable( Declaration declaration_node ) {
        if ( current_scope().get_parameters().contains(declaration_node.id.name) ) return;
        
        System.out.println( "Adding " + declaration_node.id + " to scope " + current_scope_name );

        current_scope().add_variable( declaration_node.id );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void use_before_declared_check( LexIdent ident ) {

        if ( current_scope().can_access( ident.name ) == true ) return;

        // If above fails, return Error
        System.out.println( new Error( "Symbol Collection Error", "Tried to access illegal variable '" + ident.name + "' on line " + ident.matchInfo.lineNumber() + " column " + ident.matchInfo.columnNumber() ));
        System.out.println( "Current Scope: " + current_scope_name );
        System.out.println( scope_map.get( current_scope_name ) );
        System.exit(-1);
    }

    private Scope parent_scope() {
        return scopes.get( Math.max( 0, current_index-1) );
    }

    private Scope current_scope() {
        return scope_map.get( current_scope_name );
    }

}
