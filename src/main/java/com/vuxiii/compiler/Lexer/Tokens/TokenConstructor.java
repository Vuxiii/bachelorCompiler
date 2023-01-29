package com.vuxiii.compiler.Lexer.Tokens;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexComma;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexDot;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexEqual;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLBracket;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLCurly;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLParen;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexOperator;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexPrint;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexRBracket;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexRCurly;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexRParen;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexSemicolon;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexType;

public class TokenConstructor {
    public static ASTToken construct( MatchInfo matchInfo, TokenType type ) {
        switch (type) {
            case EQUAL: {
                return new LexEqual( matchInfo, type );
            }
            case IDENTIFIER: {
                return new LexIdent( matchInfo, type );
            }
            case INT: {
                return new LexLiteral( matchInfo, type );
            }
            case DOUBLE: {
                return new LexLiteral( matchInfo, type );
            }
            case TYPE_INT: {
                return new LexType( matchInfo, type );
            }
            case LEFT_PARENTHESIS: {
                return new LexLParen( matchInfo, type );
            }
            case RIGHT_PARENTHESIS: {
                return new LexRParen( matchInfo, type );
            }
            case LEFT_BRACKET: {
                return new LexLBracket( matchInfo, type );
            }
            case RIGHT_BRACKET: {
                return new LexRBracket( matchInfo, type );
            }
            case LEFT_CURLY: {
                return new LexLCurly( matchInfo, type );
            }
            case RIGHT_CURLY: {
                return new LexRCurly( matchInfo, type );
            }
            case PLUS:  {
                return new LexOperator( matchInfo, type );
            }
            case MINUS: {
                return new LexOperator( matchInfo, type );
            }
            case DIVISION:  {
                return new LexOperator( matchInfo, type );
            }
            case TIMES: {
                return new LexOperator( matchInfo, type );
            }
            case PRINT: {
                return new LexPrint( matchInfo, type );
            }
            case SEMICOLON: {
                return new LexSemicolon( matchInfo, type );
            }
            case DOT: {
                return new LexDot( matchInfo, type );
            }
            case COMMA: {
                return new LexComma( matchInfo, type );
            }
            default: {
                // Unkown. Not implemented yet. Throw error
                System.out.println( "--[[ Tokenization Error ]]--\nFound un-recognized token:\n\n " + matchInfo.toString() + "\nMaybe you forgot to implement this token?\n" );

                System.exit(-1);

            }         
        }
        System.out.println( "--[[ Tokenization Error ]]--\nShould never reach this... In TokenConstructor" );

        System.exit(-1);
        return null; // Should never reach here
    }
}
