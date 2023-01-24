package com.vuxiii.compiler.Parser;

import java.util.List;

import com.vuxiii.LR.Grammar;
import com.vuxiii.LR.LRParser;
import com.vuxiii.LR.ParseTable;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperation;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperationKind;
import com.vuxiii.compiler.Parser.Nodes.Expression;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.Parser.Nodes.StatementKind;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;


public class Parser {
    
    private static Grammar g = null;

    public static ASTNode getAST( List<ASTToken> tokens ) {
        if ( g == null )
            init();

        
        ParseTable table = LRParser.parse( g, Symbol.n_Start );
        
        // System.out.println( tokens );

        ASTNode ast = (ASTNode)LRParser.getAST( table, tokens );
        // return AST;
        return ast;
    }

    private static void init() {
        g = new Grammar();

        g.addRuleWithReduceFunction( Symbol.n_Start, List.of( Symbol.n_StatementList, Symbol.t_Dollar ), t -> {
            return t.get(0);
        });
        
        g.addRuleWithReduceFunction( Symbol.n_StatementList, List.of( Symbol.n_Statement, Symbol.t_Semicolon, Symbol.n_StatementList ), t -> {
            Statement stm1 = (Statement)t.get(0);
            Statement stm2 = (Statement)t.get(2);
            return new Statement( Symbol.n_StatementList, stm1.node, stm2, stm1.kind );
        });

        g.addRuleWithReduceFunction( Symbol.n_StatementList, List.of( Symbol.n_Statement, Symbol.t_Semicolon ), t -> {
            Statement stm1 = (Statement)t.get(0);
            return new Statement( Symbol.n_StatementList, stm1.node, stm1.kind );
        });

        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Assignment ), t -> {
            return new Statement( Symbol.n_Statement, (ASTNode)t.get(0), StatementKind.ASSIGNMENT );
        });

        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Print ), t -> {
            return new Statement( Symbol.n_Statement, (ASTNode)t.get(0), StatementKind.PRINT );
        });


        g.addRuleWithReduceFunction( Symbol.n_Expression, List.of( Symbol.n_Expression, Symbol.t_Plus, Symbol.n_Expression ), t -> {
            return new BinaryOperation( Symbol.n_Expression, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.PLUS );
        });

        g.addRuleWithReduceFunction( Symbol.n_Expression, List.of( Symbol.t_Integer), t -> {
            return new Expression( Symbol.n_Expression, (ASTNode)t.get(0) );
        });


        g.addRuleWithReduceFunction( Symbol.n_Expression, List.of( Symbol.t_Identifier), t -> {
            return new Expression( Symbol.n_Expression, (ASTNode)t.get(0) );
        });

        g.addRuleWithReduceFunction( Symbol.n_Assignment, List.of( Symbol.t_Identifier, Symbol.t_Equals, Symbol.n_Expression ), t -> {
            return new Assignment( Symbol.n_Assignment, (LexIdent)t.get(0), (ASTNode)t.get(2)  );
        });

        g.addRuleWithReduceFunction( Symbol.n_Print, List.of( Symbol.t_Print, Symbol.t_LParen, Symbol.n_Expression, Symbol.t_RParen ), t -> {
            return new Print( Symbol.n_Print, (ASTNode)t.get(2) );
        });
        // g.addRuleWithReduceFunction( Term.n_Expression, List.of( Term.t_Integer ), tokens -> {
        //     return null;
        // });
    }


}
