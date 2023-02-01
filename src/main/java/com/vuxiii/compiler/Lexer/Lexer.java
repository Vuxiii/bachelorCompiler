package com.vuxiii.compiler.Lexer;

import java.util.List;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.Regex.Regex;
import com.vuxiii.Regex.Token.TokenEOP;
import com.vuxiii.compiler.Lexer.Tokens.PrimitiveType;
import com.vuxiii.compiler.Lexer.Tokens.TokenConstructor;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
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

        reg = new Regex<>(PrimitiveType.INT.name,         matchInfo -> TokenConstructor.construct( matchInfo, TokenType.TYPE_INT ) );

        // reg.addRegex( "int", in -> new LexType( in ) );
        reg.addRegex( PrimitiveType.DOUBLE.name,          matchInfo -> TokenConstructor.construct( matchInfo, TokenType.TYPE_DOUBLE ) );
        reg.addRegex( "[:digit:][:digit:]*",              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.INT ) );
        reg.addRegex( "[:digit:][:digit:]*\\.[:digit:]*", matchInfo -> TokenConstructor.construct( matchInfo, TokenType.DOUBLE ) );
        reg.addRegex( ";",                                matchInfo -> TokenConstructor.construct( matchInfo, TokenType.SEMICOLON ) );
        reg.addRegex( ":",                                matchInfo -> TokenConstructor.construct( matchInfo, TokenType.COLON ) );
        reg.addRegex( "=",                                matchInfo -> TokenConstructor.construct( matchInfo, TokenType.EQUAL ) );
        reg.addRegex( "+",                                matchInfo -> TokenConstructor.construct( matchInfo, TokenType.PLUS ) );
        reg.addRegex( "\\-",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.MINUS ) );
        reg.addRegex( "\\*",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.TIMES ) );
        reg.addRegex( "/",                                matchInfo -> TokenConstructor.construct( matchInfo, TokenType.DIVISION ) );
        reg.addRegex( "print",                            matchInfo -> TokenConstructor.construct( matchInfo, TokenType.PRINT ) );
        reg.addRegex( "let",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.LET ) );
        reg.addRegex( "\\->",                               matchInfo -> TokenConstructor.construct( matchInfo, TokenType.ARROW_RIGHT ) );
        reg.addRegex( "type",                             matchInfo -> TokenConstructor.construct( matchInfo, TokenType.TYPE_DECL ) );
        reg.addRegex( "\\(",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.LEFT_PARENTHESIS ) );
        reg.addRegex( "\\)",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.RIGHT_PARENTHESIS ) );
        reg.addRegex( "\\[",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.LEFT_BRACKET ) );
        reg.addRegex( "\\]",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.RIGHT_BRACKET ) );
        reg.addRegex( "\\{",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.LEFT_CURLY ) );
        reg.addRegex( "\\}",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.RIGHT_CURLY ) );
        reg.addRegex( "\\.",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.DOT ) );
        reg.addRegex( "\\,",                              matchInfo -> TokenConstructor.construct( matchInfo, TokenType.COMMA ) );
        
        reg.addRegex( "[:alpha:]([:alpha:]|[:digit:]|_)*",  matchInfo -> TokenConstructor.construct( matchInfo, TokenType.IDENTIFIER ), 99999999 );

        reg.compile();
        return reg;
    }
}
