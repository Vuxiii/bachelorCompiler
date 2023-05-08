package com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.Parser.Nodes.StatementList;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_Shrinker_Statement_Collector extends VisitorBase {
    
    public StatementList stmts = new StatementList( Symbol.n_StatementList );

    private Set<Statement> visited = new HashSet<>();

    public Map<Statement, StatementList> mapper = new HashMap<>();

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void collect( Statement stmt ) {
        if ( visited.contains( stmt ) ) return;
        if ( stmt.next.isEmpty() ) return;

        StatementList s = new StatementList(Symbol.n_StatementList);
        
        shrink( s, stmt );
        stmts = s;
        mapper.put( stmt, s );
    }

    private void shrink( StatementList list, Statement stmt ) {
        visited.add( stmt );
        list.push( stmt );
        if ( stmt.next.isPresent() )
            shrink( list, stmt.next.get() );
    }

    public void cleanup() {
        for ( Statement s : visited ) {
            s.next = Optional.empty();
            s.setup_ASTNodeQueue();
        }
    }

}
