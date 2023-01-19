package com.vuxiii.compiler.Visitors;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.Visitor.VisitorBase;

public class ASTPrinter extends VisitorBase {


    private Stack<Integer> childStack = new Stack<>();

    public void visit_print( ASTNode token ) {
        
        System.out.print( getPrefix() );

        System.out.println( token.getPrintableName() );

        if ( !childStack.isEmpty() ) {
            int v = childStack.pop();      
            childStack.push(v - 1);
        }
    }

    private String getPrefix() {
        String pre = "";
        for ( int el : childStack ) {
            if ( el <= 0 ) {
                pre += "   ";
            } else {
                pre += "│";
                // Todo!: Figure our how to implement this. This will make the pretty-printer nicer. Like Unix tree command.
                if ( false ) 
                    pre += "__"; //└──
                else
                    pre += "  ";
            }
        }
        return pre;
    }

    public void preVisit_increaseIndent( ASTNode token ) {
        int v = token.getChildrenCount();
        childStack.push( v );
    }

    public void postVisit_decreaseIndent( ASTNode token ) {
        if ( childStack.size() > 0 )
            while ( !childStack.isEmpty() && childStack.peek() <= 0 )
                childStack.pop();
    }
    
}
