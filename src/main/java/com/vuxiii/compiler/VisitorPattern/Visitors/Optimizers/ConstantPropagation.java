package com.vuxiii.compiler.VisitorPattern.Visitors.Optimizers;

import java.util.HashMap;
import java.util.Map;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.ConcreteType;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
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
        if ( binop.left.isLeaf() && binop.left instanceof LexLiteral ) {
            if ( binop.right.isLeaf() && binop.right instanceof LexLiteral ) {

                // Perform the addition. And replace the current node.
                String value = "";
                //! DOESN'T CURRENTLY WORK! BECAUSE OF THE TYPES OF VAL ATTRIBUTE. IT CAN BE EITHER BE INT OR DOUBLE
                switch (binop.kind) {
                    case DIVISION: value = "" + ( Integer.parseInt(((LexLiteral)binop.left).val) / Integer.parseInt(((LexLiteral)binop.right).val) );
                    break;
                    case MINUS: value = "" + ( Integer.parseInt(((LexLiteral)binop.left).val) - Integer.parseInt(((LexLiteral)binop.right).val) );
                    break;
                    case TIMES: value = "" + ( Integer.parseInt(((LexLiteral)binop.left).val) * Integer.parseInt(((LexLiteral)binop.right).val) );
                    break;
                    case PLUS: value = "" + ( Integer.parseInt(((LexLiteral)binop.left).val) + Integer.parseInt(((LexLiteral)binop.right).val) );
                    break;
                    case MODULO: value = "" + ( Integer.parseInt(((LexLiteral)binop.left).val) % Integer.parseInt(((LexLiteral)binop.right).val) );
                    break;
                }

                int line = ((LexLiteral)binop.left).matchInfo.lineNumber();
                int column = ((LexLiteral)binop.left).matchInfo.columnNumber();
                replace_node = new LexLiteral( new MatchInfo( value, line, column ), TokenType.INT );
            }
        }
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 0 )
    public void register_variable( Assignment node ) {
        if ( !node.value.isLeaf() ) return;
        if ( !(node.value instanceof LexLiteral) ) return;
        
        scope.put( node.id.name, Integer.parseInt(((LexLiteral)node.value).val) );

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

            node.left = new LexLiteral( new MatchInfo( value + "", n.matchInfo.lineNumber(), n.matchInfo.columnNumber() ), TokenType.INT );
            run_again = true;

        } else if ( node.right instanceof LexIdent ) {
            LexIdent n = (LexIdent) node.right;
            Integer value = scope.get( n.name );
            if ( value == null ) return; // Not registered in scope yet.

            node.right = new LexLiteral( new MatchInfo( value + "", n.matchInfo.lineNumber(), n.matchInfo.columnNumber() ), TokenType.INT );
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
