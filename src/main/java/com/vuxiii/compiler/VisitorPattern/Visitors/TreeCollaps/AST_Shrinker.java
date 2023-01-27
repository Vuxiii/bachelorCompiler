package com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperation;
import com.vuxiii.compiler.Parser.Nodes.Expression;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class AST_Shrinker extends VisitorBase {
    
    private ASTNode current = null;


    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 3 )
    public void cleanup_expression_arithmetic( Expression exp ) {
        if ( exp.node instanceof Expression ) {
            exp.node = current;
        }
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void cleanup_expression_arithmetic( BinaryOperation binop ) {
        
        if ( binop.left instanceof Expression )
            binop.left = binop.left.getChild1().get();
        if ( binop.right instanceof Expression )
            binop.right = binop.right.getChild1().get();

    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 4 )
    public void cleanup_expression_arithmetic( ASTNode exp ) {
        if ( exp.isLeaf() ) {
            current = exp;
        }
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 2 )
    public void cleanup_assignment( Assignment assignment ) {
        assignment.value = assignment.value.getChild1().get();
    }
}
