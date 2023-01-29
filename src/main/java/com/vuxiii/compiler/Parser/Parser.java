package com.vuxiii.compiler.Parser;

import java.util.List;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Grammar;
import com.vuxiii.LR.LRParser;
import com.vuxiii.LR.ParseTable;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.compiler.Lexer.Tokens.ConcreteType;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexOperator;
import com.vuxiii.compiler.Parser.Nodes.Argument;
import com.vuxiii.compiler.Parser.Nodes.ArgumentKind;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperation;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperationKind;
import com.vuxiii.compiler.Parser.Nodes.Capture;
import com.vuxiii.compiler.Parser.Nodes.Expression;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.Scope;
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

        // n_Start -> n_StatementList t_Dollar
        g.addRuleWithReduceFunction( Symbol.n_Start, List.of( Symbol.n_StatementList, Symbol.t_Dollar ), t -> {
            return t.get(0);
        });
        

        // n_StatementList -> n_Statement t_Semicolon n_StatementList
        g.addRuleWithReduceFunction( Symbol.n_StatementList, List.of( Symbol.n_Statement, Symbol.t_Semicolon, Symbol.n_StatementList ), t -> {
            Statement stm1 = (Statement)t.get(0);
            Statement stm2 = (Statement)t.get(2);
            return new Statement( Symbol.n_StatementList, stm1.node, stm2, stm1.kind );
        });

        // n_StatementList -> n_Statement t_Semicolon
        g.addRuleWithReduceFunction( Symbol.n_StatementList, List.of( Symbol.n_Statement, Symbol.t_Semicolon ), t -> {
            Statement stm1 = (Statement)t.get(0);
            return new Statement( Symbol.n_StatementList, stm1.node, stm1.kind );
        });

        // n_Statement -> n_Assignment
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Assignment ), t -> {
            return new Statement( Symbol.n_Statement, (ASTNode)t.get(0), StatementKind.ASSIGNMENT );
        });

        // n_Statement -> n_Print
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Print ), t -> {
            return new Statement( Symbol.n_Statement, (ASTNode)t.get(0), StatementKind.PRINT );
        });


        // --[[ Scopes ]]--

        // n_Statement -> n_Scope
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Scope ), t -> {
            return new Statement( Symbol.n_Statement, (ASTNode)t.get(0), StatementKind.SCOPE );
        });

        // n_Scope -> n_Capture n_Scope_Block
        g.addRuleWithReduceFunction( Symbol.n_Scope, List.of( Symbol.n_Capture, Symbol.n_Scope_Block ), t -> {
            return new Scope( Symbol.n_Scope, (Statement)t.get(1), (Capture)t.get(0) );
        });

        // n_Scope -> n_Scope_Block
        g.addRuleWithReduceFunction( Symbol.n_Scope, List.of( Symbol.n_Scope_Block ), t -> {
            return new Scope( Symbol.n_Scope, (Statement)t.get(0) );
        });

        // n_Scope_Block -> t_LCurly n_StatementList t_RCurly
        g.addRuleWithReduceFunction( Symbol.n_Scope_Block, List.of( Symbol.t_LCurly, Symbol.n_StatementList, Symbol.t_RCurly ), t -> {
            Statement stm = (Statement)t.get(1);
            if ( stm.next.isPresent() )
                return new Statement( Symbol.n_Scope_Block, stm.node, stm.next.get(), stm.kind );
            else
                return new Statement( Symbol.n_Scope_Block, stm.node, stm.kind );

        });


        // n_Capture -> t_LBracket n_Arg_List t_RBracket
        g.addRuleWithReduceFunction( Symbol.n_Capture, List.of( Symbol.t_LBracket, Symbol.n_Arg_List, Symbol.t_RBracket ), t -> {
            return new Capture( Symbol.n_Capture, (Argument)t.get(1) );
        });

        // n_Capture -> t_LBracket t_Dot t_Dot t_RBracket
        g.addRuleWithReduceFunction( Symbol.n_Capture, List.of( Symbol.t_LBracket, Symbol.t_Dot, Symbol.t_Dot, Symbol.t_RBracket ), t -> {
            return new Capture( Symbol.n_Capture );
        });






        // --[[ Functions ]]--
        
        // n_Arg_List -> n_Arg t_Comma n_Arg_List
        g.addRuleWithReduceFunction( Symbol.n_Arg_List, List.of( Symbol.n_Arg, Symbol.t_Comma, Symbol.n_Arg_List ), t -> {
            Argument arg = (Argument)t.get(0);
            return new Argument( Symbol.n_Arg_List, arg.node, (Argument)t.get(2), arg.kind );
        });

        // n_Arg_List -> n_Arg
        g.addRuleWithReduceFunction( Symbol.n_Arg_List, List.of( Symbol.n_Arg ), t -> {
            Argument arg = (Argument)t.get(0);
            return new Argument( Symbol.n_Arg_List, arg.node, arg.kind );
        });

        // n_Arg -> t_Identifier
        g.addRuleWithReduceFunction( Symbol.n_Arg, List.of( Symbol.t_Identifier ), t -> {
            return new Argument( Symbol.n_Arg, (ASTNode)t.get(0), ArgumentKind.IDENTIFIER );
        });

        // n_Arg -> t_Literal
        g.addRuleWithReduceFunction( Symbol.n_Arg, List.of( Symbol.n_Literal ), t -> {
            return new Argument( Symbol.n_Arg, (ASTNode)t.get(0), ArgumentKind.LITERAL );
        });


        // --[[ Arithmetic ]]--

        // n_Expression -> n_Expression_Arithmetic
        g.addRuleWithReduceFunction( Symbol.n_Expression, List.of( Symbol.n_Expression_Arithmetic ), t -> {
            return new Expression( Symbol.n_Expression, (ASTNode)t.get(0) );
        });

        // --[[ PLUS, MINUS ]]--

        // n_Expression_Arithmetic -> n_Expression_Arithmetic t_Plus n_Term
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Expression_Arithmetic, Symbol.t_Plus, Symbol.n_Term ), t -> {
            return new BinaryOperation( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.PLUS );
        });

        // n_Expression_Arithmetic -> n_Expression_Arithmetic t_Minus n_Term
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Expression_Arithmetic, Symbol.t_Minus, Symbol.n_Term ), t -> {
            return new BinaryOperation( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.MINUS );
        });

        // n_Expression_Arithmetic -> n_Term
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Term ), t -> {
            return new Expression( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0) );
        });


        // --[[ TIMES, DIVISION ]]--

        // n_Term -> t_Minus n_Term t_Times n_Value
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Term, Symbol.t_Times, Symbol.n_Value ), t -> {
            return new BinaryOperation( Symbol.n_Term, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.TIMES );
        });

        // n_Term -> t_Minus n_Term t_Division n_Value
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Term, Symbol.t_Division, Symbol.n_Value ), t -> {
            return new BinaryOperation( Symbol.n_Term, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.DIVISION );
        });

        // n_Term -> t_Minus n_Value
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Value ), t -> {
            return new Expression( Symbol.n_Term, (ASTNode)t.get(0) );
        });

        // --[[ UNARY MINUS ]]-- do better. quick solution

        // n_Value -> t_Minus n_Factor
        g.addRuleWithReduceFunction( Symbol.n_Value, List.of( Symbol.t_Minus, Symbol.n_Factor ), t -> {
            return new BinaryOperation( Symbol.n_Value, new LexLiteral(new MatchInfo("0", -1, -1), TokenType.INT), (ASTNode)t.get(1), new LexOperator( new MatchInfo("-", -1, -1), TokenType.MINUS ), BinaryOperationKind.MINUS );
        });

        // n_Value -> n_Factor
        g.addRuleWithReduceFunction( Symbol.n_Value, List.of( Symbol.n_Factor ), t -> {
            return new Expression( Symbol.n_Value, (ASTNode)t.get(0) );
        });

        // --[[ (), int, variable ]]--

        // n_Factor -> t_LParen n_Expression_Arithmetic t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.t_LParen, Symbol.n_Expression_Arithmetic, Symbol.t_RParen ), t -> {
            return new Expression( Symbol.n_Factor, (ASTNode)t.get(1) );
        });

        // n_Factor -> n_Identifier
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.t_Identifier ), t -> {
            return new Expression( Symbol.n_Factor, (ASTNode)t.get(0) );
        });

        // n_Factor -> n_Literal
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.n_Literal ), t -> {
            return new Expression( Symbol.n_Factor, (ASTNode)t.get(0) );
        });

        // n_Literal -> t_Integer
        g.addRuleWithReduceFunction( Symbol.n_Literal, List.of( Symbol.t_Integer ), t -> {
            return new Expression( Symbol.n_Literal, (ASTNode)t.get(0) );
        } );

        // n_Literal -> t_Double
        g.addRuleWithReduceFunction( Symbol.n_Literal, List.of( Symbol.t_Double ), t -> {
            return new Expression( Symbol.n_Literal, (ASTNode)t.get(0) );
        } );



        // n_Assignment -> t_Identifier t_Equals n_Expression
        g.addRuleWithReduceFunction( Symbol.n_Assignment, List.of( Symbol.t_Identifier, Symbol.t_Equals, Symbol.n_Expression ), t -> {
            return new Assignment( Symbol.n_Assignment, (LexIdent)t.get(0), (ASTNode)t.get(2)  );
        });

        // n_Print -> t_Print t_Equals t_LParen n_Expression t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Print, List.of( Symbol.t_Print, Symbol.t_LParen, Symbol.n_Expression, Symbol.t_RParen ), t -> {
            return new Print( Symbol.n_Print, (ASTNode)t.get(2) );
        });
        
    }


}
