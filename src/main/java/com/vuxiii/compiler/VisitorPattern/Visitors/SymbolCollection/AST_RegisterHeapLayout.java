package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Error.Error;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Symbol;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.Parser.Nodes.FieldList;
import com.vuxiii.compiler.Parser.Nodes.Root;
import com.vuxiii.compiler.Parser.Nodes.SymbolNode;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.Parser.Nodes.Types.RecordType;
import com.vuxiii.compiler.Parser.Nodes.Types.UserType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_RegisterHeapLayout extends VisitorBase {

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void register_var( Declaration decl ) {
        if ( decl.kind != DeclarationKind.HEAP ) return;

        decl.heap_layout = new HeapLayout(decl);

    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void register_record( RecordType record ) {
        
        record.heap_layout = new HeapLayout(record);    

        if ( record.parent.get() instanceof Declaration ) {
            Declaration decl = (Declaration)record.parent.get();
            if ( decl.kind == DeclarationKind.HEAP ) {
                AST_SymbolCollector.current_symbol_node(decl).heap_layouts.put( decl.id.name, record.heap_layout );
            }
        }

    }

    

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void register_function( Assignment func_assignment ) {
        // if ( func_assignment.)
    }

}
