package com.vuxiii.compiler.Lexer.Tokens;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Parser.Symbol;

/**
 * Inernal types for use in the compiler.
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
    ARROW_RIGHT( Symbol.t_Arrow_Right ),

    INT( Symbol.t_Integer, PrimitiveType.INT ),
    DOUBLE( Symbol.t_Double, PrimitiveType.DOUBLE ),
    TYPE_DECL( Symbol.t_Type_Declare ),
    TYPE_INT( Symbol.t_Type_Int, PrimitiveType.INT ),
    TYPE_DOUBLE( Symbol.t_Type_Double, PrimitiveType.DOUBLE );


    public Term symbol;
    public PrimitiveType literal_type;

    TokenType( Term term ) {
        this.symbol = term;
    }

    TokenType( Term term, PrimitiveType type ) {
        this.symbol = term;
        this.literal_type = type;
    }
}
