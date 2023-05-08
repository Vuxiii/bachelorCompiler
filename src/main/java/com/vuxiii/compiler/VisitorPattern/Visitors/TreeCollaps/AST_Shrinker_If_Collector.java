package com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps;

import java.util.HashMap;
import java.util.Map;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Parser.Nodes.ElseNode;
import com.vuxiii.compiler.Parser.Nodes.IfElseNode;
import com.vuxiii.compiler.Parser.Nodes.IfList;
import com.vuxiii.compiler.Parser.Nodes.IfNode;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;


public class AST_Shrinker_If_Collector extends VisitorBase {
    
    public Map<IfElseNode, IfList> mapper = new HashMap<>();

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void collect_if_else( IfElseNode if_elses ) {
        IfList s = new IfList(Symbol.n_StatementList);
        
        collect( s, if_elses );

        mapper.put( if_elses, s );
        s.setup_ASTNodeQueue();
    }

    private void collect( IfList list, IfElseNode if_else ) {

        list.push( if_else.if_block );

        if ( !(if_else.else_block.body instanceof Statement) ) return;
        
        Statement stm = (Statement) if_else.else_block.body;
        if ( stm.node instanceof IfNode ) {
            list.push( stm.node );
        } else if ( stm.node instanceof ElseNode ) {
            list.push( stm.node );
        } else if ( stm.node instanceof IfElseNode ) {
            collect( list, (IfElseNode) stm.node );
        } 
    }

}
