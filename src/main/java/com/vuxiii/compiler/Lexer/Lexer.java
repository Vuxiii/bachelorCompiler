package com.vuxiii.compiler.Lexer;

import java.util.LinkedList;
import java.util.List;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.Regex.Regex;
import com.vuxiii.Regex.Token.TokenEOP;
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

        reg = new Regex<>("int", in -> new LexType( in ));

        // reg.addRegex( "int", in -> new LexType( in ) );
        reg.addRegex( "[:digit:][:digit:]*", in -> new LexInt( in ) );
        reg.addRegex( ";", in -> new LexSemicolon() );
        reg.addRegex( "=", in -> new LexEqual() );
        reg.addRegex( "+", in -> new LexPlus() );
        reg.addRegex( "print", in -> new LexPrint() );
        reg.addRegex( "\\(", in -> new LexLParen() );
        reg.addRegex( "\\)", in -> new LexRParen() );
        reg.addRegex( "[:alpha:]([:alpha:]|[:digit:])*", in -> new LexIdent( in ), 99999999 );

        reg.compile();
        return reg;
    }
}
