package com.vuxiii.compiler.Parser;

import com.vuxiii.LR.Records.NonTerminal;
import com.vuxiii.LR.Records.Terminal;

public class Symbol {
    
    public static final NonTerminal n_Start = new NonTerminal( "START", true );
    public static final NonTerminal n_Expression = new NonTerminal( "EXPRESSION" );
    public static final NonTerminal n_ExpressionList = new NonTerminal( "EXPRESSION_LIST" );
    public static final NonTerminal n_Statement = new NonTerminal( "STATEMENT" );
    public static final NonTerminal n_StatementList = new NonTerminal( "STATEMENT_LIST" );
    public static final NonTerminal n_Assignment = new NonTerminal( "ASSIGNMENT" );
    public static final NonTerminal n_Print = new NonTerminal( "PRINT" );
    public static final NonTerminal n_Expression_Arithmetic = new NonTerminal( "ARITHMETIC_EXPRESSION" );

    // Arithmetic
    public static final NonTerminal n_Term = new NonTerminal( "TERM" );
    public static final NonTerminal n_Value = new NonTerminal( "VALUE" );
    public static final NonTerminal n_Factor = new NonTerminal( "FACTOR" );


    public static final Terminal t_Plus = new Terminal( "+" );
    public static final Terminal t_Minus = new Terminal( "-" );
    public static final Terminal t_Times = new Terminal( "*" );
    public static final Terminal t_Division = new Terminal( "/" );
    public static final Terminal t_Equals = new Terminal( "=" );
    public static final Terminal t_Identifier = new Terminal( "id" );
    public static final Terminal t_Integer = new Terminal( "integer" );
    public static final Terminal t_TypeInt = new Terminal( "type int" );
    public static final Terminal t_Semicolon = new Terminal( ";" );
    public static final Terminal t_Dollar = new Terminal( "$", true );
    public static final Terminal t_Print = new Terminal( "print" );
    public static final Terminal t_LParen = new Terminal( "'('" );
    public static final Terminal t_RParen = new Terminal( "')'" );

}
