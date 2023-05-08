package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection;

import java.util.HashMap;
import java.util.Map;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Parser.Nodes.Argument;
import com.vuxiii.compiler.Parser.Nodes.ArgumentList;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.PrintKind;
import com.vuxiii.compiler.Parser.Nodes.Root;
import com.vuxiii.compiler.VisitorPattern.Visitor;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class StringCollector extends Visitor {
    public Map<Print, StringNode> strings;

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void init_root( Root root ) {
        this.strings = root.strings;
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void collect_substitute( Print node ) {
        if ( node.kind != PrintKind.STRING_SUBSTITUTE ) return;
        
        LexLiteral literal = (LexLiteral)node.value;

        System.out.println( "Found string: " + node );

        if ( node.arg_list.get() instanceof Argument )
            strings.put( node, new StringNode( literal.val, (Argument)node.arg_list.get() ) );
        else
            strings.put( node, new StringNode( literal.val, (ArgumentList)node.arg_list.get() ) );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2 )
    public void collect_normal( Print node ) {
        if ( node.kind != PrintKind.STRING ) return;
        
        // if ( )

        LexLiteral literal = (LexLiteral)node.value;

        System.out.println( "Found string: " + node );

        strings.put( node, new StringNode( literal.val ) );
    }
}
