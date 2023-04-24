package com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps;

import java.util.HashMap;
import java.util.Map;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.Parser.Nodes.StatementList;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_Shrinker_Statement extends VisitorBase {
    
    private Map<Statement, StatementList> mapper = new HashMap<>();

    private Statement prev = null;

    public AST_Shrinker_Statement( Map<Statement, StatementList> mapper ) {
        this.mapper = mapper;
    }

    @VisitorPattern( when = VisitOrder.AFTER_CHILD, order = 1 )
    public void set_new_child_statement( ASTNode node ) {
        if ( !mapper.containsKey( prev ) ) return;

        node.replace_child_with( prev, mapper.get(prev) );

    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void reset( Statement stmt ) {
        prev = stmt;
    }


}
