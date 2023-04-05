package com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps;

import java.util.HashMap;
import java.util.Map;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.Parser.Nodes.FieldList;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;


public class AST_Shrinker_Field extends VisitorBase {
    
    
    private Map<Field, FieldList> mapper = new HashMap<>();

    private Field prev = null;

    public AST_Shrinker_Field( Map<Field, FieldList> mapper ) {
        this.mapper = mapper;
    }

    @VisitorPattern( when = VisitOrder.AFTER_CHILD )
    public void set_new_child_if( ASTNode node ) {
        if ( !mapper.containsKey( prev ) ) return;

        node.replace_child_with( prev, mapper.get(prev) );
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void reset( Field stmt ) {
        prev = stmt;
    }

}
