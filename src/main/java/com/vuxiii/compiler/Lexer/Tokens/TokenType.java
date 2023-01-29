package com.vuxiii.compiler.Lexer.Tokens;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Parser.Symbol;

/**
 * Provides identification for the compiler.
 * Concrete types should be used outside after parsing is over.
 * SHOULD ONLY BE USED FOR PARSING
 */
public enum TokenType {
    EQUAL( Symbol.t_Equals ),
    IDENTIFIER( Symbol.t_Identifier ),
    LEFT_PARENTHESIS( Symbol.t_LParen ),
    RIGHT_PARENTHESIS( Symbol.t_RParen ),
    LEFT_BRACKET( Symbol.t_LBracket ),
    RIGHT_BRACKET( Symbol.t_RBracket ),
    LEFT_CURLY( Symbol.t_LCurly ),
    RIGHT_CURLY( Symbol.t_RCurly ),
    PLUS( Symbol.t_Plus ),
    MINUS( Symbol.t_Minus ),
    TIMES( Symbol.t_Times ),
    DIVISION( Symbol.t_Division ),
    PRINT( Symbol.t_Print ),
    SEMICOLON( Symbol.t_Semicolon ),
    COLON( Symbol.t_Colon ),
    COMMA( Symbol.t_Comma ),
    DOT( Symbol.t_Dot ),

    LET( Symbol.t_Let ),

    INT( Symbol.t_Integer, ConcreteType.INT ),
    DOUBLE( Symbol.t_Double, ConcreteType.DOUBLE ),
    TYPE_INT( Symbol.t_Type_Int, ConcreteType.INT ),
    TYPE_DOUBLE( Symbol.t_Type_Double, ConcreteType.DOUBLE );


    public Term symbol;
    public ConcreteType literal_type;

    TokenType( Term term ) {
        this.symbol = term;
    }

    TokenType( Term term, ConcreteType type ) {
        this.symbol = term;
        this.literal_type = type;
    }
}
