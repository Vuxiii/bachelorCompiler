package com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.Parser.Nodes.FieldList;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.Parser.Nodes.StatementList;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_Shrinker_Field_Collector extends VisitorBase {
    
    public FieldList stmts = new FieldList( Symbol.n_Field_List );

    private Set<Field> visited = new HashSet<>();

    public Map<Field, FieldList> mapper = new HashMap<>();

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void collect( Field stmt ) {
        if ( visited.contains( stmt ) ) return;
        if ( stmt.next.isEmpty() ) return;

        FieldList s = new FieldList( Symbol.n_Field_List );
        
        shrink( s, stmt );
        stmts = s;
        mapper.put( stmt, s );
    }

    private void shrink( FieldList list, Field stmt ) {
        visited.add( stmt );
        list.push( stmt );
        if ( stmt.next.isPresent() )
            shrink( list, stmt.next.get() );
    }

    public void cleanup() {
        for ( Field s : visited ) {
            s.next = Optional.empty();
            s.setup_ASTNodeQueue();
        }
    }

}
