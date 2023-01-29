package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Parser.Nodes.Argument;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.Capture;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.ScopeNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_SymbolCollector extends VisitorBase {
    

    public List<Scope> scopes;
    private int current_index = 0;

    private boolean checking_capture = false;

    public AST_SymbolCollector( Scope main_scope ) {
        scopes = new ArrayList<>();
        scopes.add( main_scope );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void new_scope( ScopeNode scope ) {
        // Create the new scope

        Scope new_scope = new Scope();
        scopes.add( new_scope );
        current_index++;

        if ( scope.capture.isPresent() && scope.capture.get().accesses.isEmpty() ) {
            // Can access everything from parent.
            Scope parent_scope = parent_scope();
            for ( String capture : parent_scope.get_captures() ) {
                new_scope.add_capture( parent_scope.lookup_capture(capture) );
            }
            for ( String capture : parent_scope.get_variables() ) {
                new_scope.add_capture( parent_scope.lookup_capture(capture) );
            }
        }
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void pop_scope( ScopeNode scope ) {
        current_index--;
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void enter_capture_check( Capture capture ) {
        checking_capture = true;
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void exit_capture_check( Capture capture ) {
        checking_capture = false;
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void check_literal_in_capture( Argument arg ) {
        if ( !checking_capture ) return;
        if ( !(arg.node instanceof LexLiteral) ) return;

        System.out.println( "\u001B[41m\u001B[37m--[[ Symbol Collection Error ]]--\u001B[0m\nUnexpected literal '" + ((LexLiteral)arg.node).val + "' on line " + ((LexLiteral)arg.node).matchInfo.lineNumber() + "\nOnly variables are allowed inside a capture block!" );
        System.exit(-1);
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2 )
    public void use_before_declared_check( Argument arg ) {

        if ( !(arg.node instanceof LexIdent) ) return; // It is a literal. Therefore we are ALWAYS allowed to access it.

        LexIdent node = (LexIdent) arg.node;
        
        if ( parent_scope().can_access( node.name ) == true ) return;

        // If above fails, return Error
        System.out.println( "\u001B[41m\u001B[37m--[[ Symbol Collection Error ]]--\u001B[0m\nTried to access illegal variable '" + node.name + "' on line " + node.matchInfo.lineNumber() + " column " + node.matchInfo.columnNumber() );
        System.exit(-1);
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 10 )
    public void register_capture( Argument capture_me ) {
        current_scope().add_capture( (LexIdent) capture_me.node );
    }


    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void collect_variable( Declaration declaration_node ) {
        current_scope().add( declaration_node.id );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void use_before_declared_check( LexIdent ident ) {

        if ( current_scope().can_access( ident.name ) == true ) return;

        // If above fails, return Error
        System.out.println( "\u001B[41m\u001B[37m--[[ Symbol Collection Error ]]--\u001B[0m\nTried to access illegal variable '" + ident.name + "' on line " + ident.matchInfo.lineNumber() + " column " + ident.matchInfo.columnNumber() );
        System.exit(-1);
    }

    private Scope parent_scope() {
        return scopes.get( Math.max( 0, current_index-1) );
    }

    private Scope current_scope() {
        return scopes.get( current_index );
    }

}
