package com.vuxiii.compiler.VisitorPattern.Visitors.Optimizers;

import java.util.HashMap;
import java.util.Map;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexInt;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperation;
import com.vuxiii.compiler.Parser.Nodes.Expression;
import com.vuxiii.compiler.VisitorPattern.Visitor;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class ConstantPropagation extends Visitor {
    
    private ASTNode replace_node = null;

    public boolean run_again = false;

    // TODO: Remember to update this when scopes have been implemented! 
    Map<String, Integer> scope = new HashMap<>();

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void binop_eval( BinaryOperation binop ) {
        if ( binop.left.isLeaf() && binop.left instanceof LexInt ) {
            if ( binop.right.isLeaf() && binop.right instanceof LexInt ) {

                // Perform the addition. And replace the current node.
                String value = "";
                switch (binop.kind) {
                    case DIVISION: value = "" + ( ((LexInt)binop.left).val / ((LexInt)binop.right).val );
                    break;
                    case MINUS: value = "" + ( ((LexInt)binop.left).val - ((LexInt)binop.right).val );
                    break;
                    case TIMES: value = "" + ( ((LexInt)binop.left).val * ((LexInt)binop.right).val );
                    break;
                    case PLUS: value = "" + ( ((LexInt)binop.left).val + ((LexInt)binop.right).val );
                    break;
                    case MODULO: value = "" + ( ((LexInt)binop.left).val % ((LexInt)binop.right).val );
                    break;
                }

                int line = ((LexInt)binop.left).matchInfo.lineNumber();
                int column = ((LexInt)binop.left).matchInfo.columnNumber();
                replace_node = new LexInt( new MatchInfo( value, line, column ) );
            }
        }
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 0 )
    public void register_variable( Assignment node ) {
        if ( !node.value.isLeaf() ) return;
        if ( !(node.value instanceof LexInt) ) return;
        
        scope.put( node.id.name, ((LexInt)node.value).val );

    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 2 )
    public void remove_binop( BinaryOperation node ) {
        if ( replace_node == null ) return;

        if ( node.left.isLeaf() && node.right instanceof BinaryOperation ) {
            node.right = replace_node;
            replace_node = null;
            run_again = true;
        }

    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 5 )
    public void replace_idents( BinaryOperation node ) {
        if ( !(node.left.isLeaf() && node.right.isLeaf()) ) return;

        if ( node.left instanceof LexIdent ) {
            LexIdent n = (LexIdent) node.left;
            Integer value = scope.get( n.name );
            if ( value == null ) return; // Not registered in scope yet.

            node.left = new LexInt( new MatchInfo( value + "", n.matchInfo.lineNumber(), n.matchInfo.columnNumber() ) );
            run_again = true;

        } else if ( node.right instanceof LexIdent ) {
            LexIdent n = (LexIdent) node.right;
            Integer value = scope.get( n.name );
            if ( value == null ) return; // Not registered in scope yet.

            node.right = new LexInt( new MatchInfo( value + "", n.matchInfo.lineNumber(), n.matchInfo.columnNumber() ) );
            run_again = true;
        }
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 3 )
    public void remove_binop( Assignment node ) {
        if ( replace_node == null ) return;

        if ( node.value instanceof BinaryOperation ) {
            node.value = replace_node;
            replace_node = null;
            run_again = true;
        }

    }

}
