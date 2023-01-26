package com.vuxiii.compiler.Lexer.Tokens;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexEqual;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexInt;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLParen;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexOperator;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexPrint;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexRParen;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexSemicolon;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexType;
import com.vuxiii.compiler.Parser.Symbol;

public class TokenConstructor {
    public static ASTToken construct( MatchInfo matchInfo, TokenType type ) {
        switch (type) {
            case EQUAL: {
                return new LexEqual( matchInfo );
            }
            case IDENTIFIER: {
                return new LexIdent( matchInfo );
            }
            case INT: {
                return new LexInt( matchInfo );
            }
            case TYPE_INT: {
                return new LexType( matchInfo, ConcreteType.INT );
            }
            case LEFT_PARENTHESIS: {
                return new LexLParen( matchInfo );
            }
            case RIGHT_PARENTHESIS: {
                return new LexRParen( matchInfo );
            }
            case PLUS:  {
                return new LexOperator( matchInfo, Symbol.t_Plus, type );
            }
            case MINUS: {
                return new LexOperator( matchInfo, Symbol.t_Minus, type );
            }
            case DIVISION:  {
                return new LexOperator( matchInfo, Symbol.t_Division, type );
            }
            case TIMES: {
                return new LexOperator( matchInfo, Symbol.t_Times, type );
            }
            case PRINT: {
                return new LexPrint( matchInfo );

            }
            case SEMICOLON: {
                return new LexSemicolon( matchInfo );

            }
            default: {
                // Unkown. Not implemented yet. Throw error
                System.out.println( "Found un-recognized token:\n\n " + matchInfo.toString() + "\nMaybe you forgot to implement this token?\n" );

                System.exit(-1);

            }         
        }
        System.out.println( "Should never reach this... In TokenConstructor" );

        System.exit(-1);
        return null; // Should never reach here
    }
}
