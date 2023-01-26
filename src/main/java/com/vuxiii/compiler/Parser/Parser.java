package com.vuxiii.compiler.Parser;

import java.util.List;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Grammar;
import com.vuxiii.LR.LRParser;
import com.vuxiii.LR.ParseTable;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexInt;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexOperator;
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


        // --[[ Arithmetic ]]--

        g.addRuleWithReduceFunction( Symbol.n_Expression, List.of( Symbol.n_Expression_Arithmetic ), t -> {
            return new Expression( Symbol.n_Expression, (ASTNode)t.get(0) );
        });

        // --[[ PLUS, MINUS ]]--
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Expression_Arithmetic, Symbol.t_Plus, Symbol.n_Term ), t -> {
            return new BinaryOperation( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.PLUS );
        });
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Expression_Arithmetic, Symbol.t_Minus, Symbol.n_Term ), t -> {
            return new BinaryOperation( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.MINUS );
        });
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Term ), t -> {
            return new Expression( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0) );
        });


        // --[[ TIMES, DIVISION ]]--
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Term, Symbol.t_Times, Symbol.n_Value ), t -> {
            return new BinaryOperation( Symbol.n_Term, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.TIMES );
        });

        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Term, Symbol.t_Division, Symbol.n_Value ), t -> {
            return new BinaryOperation( Symbol.n_Term, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.DIVISION );
        });
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Value ), t -> {
            return new Expression( Symbol.n_Term, (ASTNode)t.get(0) );
        });

        // --[[ UNARY MINUS ]]-- do better. quick solution
        g.addRuleWithReduceFunction( Symbol.n_Value, List.of( Symbol.t_Minus, Symbol.n_Factor ), t -> {
            return new BinaryOperation( Symbol.n_Value, new LexInt(new MatchInfo("0", -1, -1)), (ASTNode)t.get(1), new LexOperator( new MatchInfo("-", -1, -1), Symbol.t_Minus, TokenType.MINUS ), BinaryOperationKind.MINUS );
        });
        g.addRuleWithReduceFunction( Symbol.n_Value, List.of( Symbol.n_Factor ), t -> {
            return new Expression( Symbol.n_Value, (ASTNode)t.get(0) );
        });

        // --[[ (), int, variable ]]--
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.t_LParen, Symbol.n_Expression_Arithmetic, Symbol.t_RParen ), t -> {
            return new Expression( Symbol.n_Factor, (ASTNode)t.get(1) );
        });
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.t_Identifier ), t -> {
            return new Expression( Symbol.n_Factor, (ASTNode)t.get(0) );
        });
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.t_Integer ), t -> {
            return new Expression( Symbol.n_Factor, (ASTNode)t.get(0) );
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
