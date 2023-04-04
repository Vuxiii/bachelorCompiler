package com.vuxiii.compiler.Lexer.Tokens;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexArrowRight;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexColon;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexComma;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexDot;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexElse;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexEqual;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIf;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLBracket;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLCurly;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLParen;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLet;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexOperator;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexPrint;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexRBracket;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexRCurly;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexRParen;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexReturn;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexSemicolon;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexTypeDecl;

public class TokenConstructor {
    public static ASTToken construct( MatchInfo matchInfo, TokenType type ) {
        switch (type) {
            case EQUAL: {
                return new LexEqual( matchInfo, type );
            }
            case IDENTIFIER: {
                return new LexIdent( matchInfo, type );
            }
            case INT_LITERAL: case DOUBLE_LITERAL: case BOOL_LITERAL: case STRING_LITERAL: {
                return new LexLiteral( matchInfo, type );
            }
            case TYPE_INT: case TYPE_DOUBLE: case TYPE_BOOL: case TYPE_STRING: case TYPE_VOID: {
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
            case PLUS: case MINUS: case DIVISION: case TIMES: case CHECK_EQUAL: {
                return new LexOperator( matchInfo, type );
            }
            case PRINT: {
                return new LexPrint( matchInfo, type );
            }
            case TYPE_DECL: {
                return new LexTypeDecl( matchInfo, type );
            }
            case LET: {
                return new LexLet( matchInfo, type );
            }
            case SEMICOLON: {
                return new LexSemicolon( matchInfo, type );
            }
            case COLON: {
                return new LexColon( matchInfo, type );
            }
            case DOT: {
                return new LexDot( matchInfo, type );
            }
            case COMMA: {
                return new LexComma( matchInfo, type );
            } 
            case ARROW_RIGHT: {
                return new LexArrowRight( matchInfo, type );
            }
            case IF: {
                return new LexIf( matchInfo, type );
            }
            case ELSE: {
                return new LexElse( matchInfo, type );
            }
            case RETURN: {
                return new LexReturn( matchInfo, type );
            }
            default: {
                // Unkown. Not implemented yet. Throw error
                System.out.println( "\u001B[41m\u001B[37m--[[ Tokenization Error ]]--\u001B[0m\nFound un-recognized token:\n\n\t" + matchInfo.toString() + "\nMaybe you forgot to implement this token?\n" );

                System.exit(-1);

            }         
        }
        System.out.println( "\u001B[41m\u001B[37m--[[ Tokenization Error ]]--\u001B[0m\nShould never reach this... In TokenConstructor" );

        System.exit(-1);
        return null; // Should never reach here
    }
}
