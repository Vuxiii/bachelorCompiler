package com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Parser.Nodes.Argument;
import com.vuxiii.compiler.Parser.Nodes.ArgumentList;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.Parser.Nodes.StatementList;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_Shrinker_Argument_Collector extends VisitorBase {
    
    public ArgumentList args = new ArgumentList( Symbol.n_Arg_List );

    private Set<Argument> visited = new HashSet<>();

    public Map<Argument, ArgumentList> mapper = new HashMap<>();

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void collect( Argument arg ) {
        if ( visited.contains( arg ) ) return;
        if ( arg.next.isEmpty() ) return;

        ArgumentList s = new ArgumentList(Symbol.n_Arg_List);
        
        shrink( s, arg );
        args = s;
        mapper.put( arg, s );
    }

    private void shrink( ArgumentList list, Argument arg ) {
        visited.add( arg );
        list.push( arg );
        if ( arg.next.isPresent() )
            shrink( list, arg.next.get() );
    }

    public void cleanup() {
        for ( Argument a : visited ) {
            a.next = Optional.empty();
            a.setup_ASTNodeQueue();
        }
    }

}
