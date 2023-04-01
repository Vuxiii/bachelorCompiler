package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection;

import java.util.HashMap;
import java.util.Map;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.PrintKind;
import com.vuxiii.compiler.Parser.Nodes.Root;
import com.vuxiii.compiler.VisitorPattern.Visitor;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class StringCollector extends Visitor {
    
    Root root;

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void init_root( Root root ) {
        this.root = root;
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void string_collector( Print node ) {
        if ( node.kind != PrintKind.STRING_SUBSTITUTE ) return;
        
        LexLiteral literal = (LexLiteral)node.value;

        System.out.println( "Found string: " + node );

        root.strings.put( node, new StringNode( literal.val, node.arg_list.get() ) );

    }
}
