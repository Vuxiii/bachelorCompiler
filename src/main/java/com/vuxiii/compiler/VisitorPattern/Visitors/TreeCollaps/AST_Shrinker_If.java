package com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps;

import java.util.HashMap;
import java.util.Map;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Nodes.IfElseNode;
import com.vuxiii.compiler.Parser.Nodes.IfList;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;


public class AST_Shrinker_If extends VisitorBase {
    
    
    private Map<IfElseNode, IfList> mapper = new HashMap<>();

    private IfElseNode prev = null;

    public AST_Shrinker_If( Map<IfElseNode, IfList> mapper ) {
        this.mapper = mapper;
    }

    @VisitorPattern( when = VisitOrder.AFTER_CHILD, order = 1 )
    public void set_new_child_if( ASTNode node ) {
        if ( !mapper.containsKey( prev ) ) return;

        node.replace_child_with( prev, mapper.get(prev) );
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void reset( IfElseNode stmt ) {
        prev = stmt;
    }

}
