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

public class AST_RegisterStackFrames extends VisitorBase {

    Set<Declaration> ignore_list = new HashSet<>();

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void register_root( Root root ) {
        StackFrame frame = new StackFrame();

        ((SymbolNode)root.node).stack_frame = frame;


    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void register_var( Declaration decl ) {
        if ( decl.kind != DeclarationKind.VARIABLE && decl.kind != DeclarationKind.FIELD && decl.kind != DeclarationKind.POINTER ) return;
        if ( ignore_list.contains( decl ) ) return;
        
        if ( decl.kind == DeclarationKind.POINTER && decl.type instanceof RecordType ) {
            RecordType record = (RecordType)decl.type;
            for ( Field field : record.fields.fields ) {
                ignore_list.add( field.field );
            }
        }
        AST_SymbolCollector.current_symbol_node(decl).stack_frame.register_variable(decl);
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void register_function( Assignment func_assignment ) {
        // if ( func_assignment.)
    }

}
