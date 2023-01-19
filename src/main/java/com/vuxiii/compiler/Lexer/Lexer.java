package com.vuxiii.compiler.Lexer;

import java.util.LinkedList;
import java.util.List;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.Regex.Regex;
import com.vuxiii.Regex.Token.TokenEOP;
import com.vuxiii.compiler.Lexer.Tokens.TokenConstructor;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexEqual;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexInt;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLParen;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexPlus;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexPrint;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexRParen;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexSemicolon;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexType;
import com.vuxiii.compiler.Parser.Symbol;

public class Lexer {
    private static Regex<ASTToken> reg = null;
    public static List<ASTToken> lex( String input ) {
        Regex<ASTToken> lexer = getRegex();

        List<ASTToken> li = lexer.match( input );
        li.add( new TokenEOP( Symbol.t_Dollar ) );

        return li;    
    }

    private static Regex<ASTToken> getRegex() {
        if ( reg != null )
            return reg;

        reg = new Regex<>("int", matchInfo -> TokenConstructor.construct( matchInfo, TokenType.TYPE_INT ) );

        // reg.addRegex( "int", in -> new LexType( in ) );
        reg.addRegex( "[:digit:][:digit:]*",    matchInfo -> TokenConstructor.construct( matchInfo, TokenType.INT ) );
        reg.addRegex( ";",                      matchInfo -> TokenConstructor.construct( matchInfo, TokenType.SEMICOLON ) );
        reg.addRegex( "=",                      matchInfo -> TokenConstructor.construct( matchInfo, TokenType.EQUAL ) );
        reg.addRegex( "+",                      matchInfo -> TokenConstructor.construct( matchInfo, TokenType.PLUS ) );
        reg.addRegex( "print",                  matchInfo -> TokenConstructor.construct( matchInfo, TokenType.PRINT ) );
        reg.addRegex( "\\(",                    matchInfo -> TokenConstructor.construct( matchInfo, TokenType.LEFT_PARENTHESIS ) );
        reg.addRegex( "\\)",                    matchInfo -> TokenConstructor.construct( matchInfo, TokenType.RIGHT_PARENTHESIS ) );
        // reg.addRegex( "[:alpha:]([:alpha:]|[:digit:])*", in -> new LexIdent( in ) );
        reg.addRegex( "[:alpha:]([:alpha:]|[:digit:])*", 
                                                matchInfo -> TokenConstructor.construct( matchInfo, TokenType.IDENTIFIER ), 99999999 );

        reg.compile();
        return reg;
    }
}
