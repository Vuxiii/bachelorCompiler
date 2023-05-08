package com.vuxiii.compiler.VisitorPattern.Visitors;

import java.util.Optional;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_Setup_Parents extends VisitorBase {
    
    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void call_other_shrinkers( ASTNode node ) {
        for ( ASTNode child : node.getChildren() ) {
            child.parent = Optional.of(node);
        }
    }
}