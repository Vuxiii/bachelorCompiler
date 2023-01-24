package com.vuxiii.compiler.VisitorPattern.Visitors;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.VisitorPattern.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class SimplePrinter extends VisitorBase {
    
    @VisitorPattern(when = VisitOrder.ENTER_NODE )
    public void print( ASTNode node ) {
        System.out.println( node.getPrintableName() + " ~ " + node.getChildrenCount() );
    }
}
