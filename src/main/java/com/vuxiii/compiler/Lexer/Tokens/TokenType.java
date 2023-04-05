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
    CHECK_EQUAL( Symbol.t_Check_Equal ),
    CHECK_NOT_EQUAL( Symbol.t_Check_Not_Equal ),
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

    RETURN( Symbol.t_Return ),

    LET( Symbol.t_Let ),
    IF( Symbol.t_If ),
    ELSE( Symbol.t_Else ),
    ARROW_RIGHT( Symbol.t_Arrow_Right ),

    INT_LITERAL( Symbol.t_Integer, PrimitiveType.INT ),
    DOUBLE_LITERAL( Symbol.t_Double, PrimitiveType.DOUBLE ),
    STRING_LITERAL( Symbol.t_StringLiteral, PrimitiveType.STRING ),
    BOOL_LITERAL( Symbol.t_BoolLiteral, PrimitiveType.BOOL ),
    TYPE_DECL( Symbol.t_Type_Declare ),
    TYPE_INT( Symbol.t_Type_Int, PrimitiveType.INT ),
    TYPE_DOUBLE( Symbol.t_Type_Double, PrimitiveType.DOUBLE ),
    TYPE_BOOL( Symbol.t_Type_Bool, PrimitiveType.BOOL ),
    TYPE_STRING( Symbol.t_Type_String, PrimitiveType.STRING ), 
    TYPE_VOID( Symbol.t_Type_Void, PrimitiveType.VOID );


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
