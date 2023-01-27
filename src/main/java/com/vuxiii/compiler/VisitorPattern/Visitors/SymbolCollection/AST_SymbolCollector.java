package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.List;

import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class AST_SymbolCollector extends VisitorBase {
    
    private final List<LexIdent> variables = new ArrayList<>(); 
    private final List<LexIdent> functions = new ArrayList<>(); 

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void collect_variable( Assignment assignment_node ) {

        variables.add( assignment_node.id );
        

    }

    public List<LexIdent> get_variables() {
        return variables;
    }

    public List<LexIdent> get_functions() {
        return functions;
    }

}
