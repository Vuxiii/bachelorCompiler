package com.vuxiii.compiler.VisitorPattern.Visitors.Debug;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_SimplePrinter extends VisitorBase {
    
    @VisitorPattern(when = VisitOrder.ENTER_NODE, order = 1 )
    public void print( ASTNode node ) {
        System.out.println( node.getPrintableName() + " ~ " + node.getChildrenCount() );
    }
}
