package com.vuxiii.compiler;

import com.vuxiii.Regex.Regex;
import com.vuxiii.compiler.Lexer.Lexer;
import com.vuxiii.compiler.Parser.Parser;

import java.util.List;

import com.vuxiii.LR.Grammar;
import com.vuxiii.LR.Records.ASTToken;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        

        String input = "a=3;print(a);b=a+5;print(2);";
        // String input = "a = 2;\nb= a + 3;\ny = b+a;print(a);";
        // String input = "a1 = 2;\nb= a2v + 3;\ny = b+a;print; a;";

        // [[ Tokenizer ]]
        List<ASTToken> tokens = Lexer.lex( input );

        for ( ASTToken tok : tokens ) {
            System.out.print( tok );
            System.out.println( "\t" + tok.getTerm() );
        }

        // [[ Parser ]]
        ASTToken ast = Parser.getAST( tokens );

        System.out.println( ast );


        // [[ Symbol Collecting ]]


        // [[ Type Checking ]]

        // [[ Code Generation ]]

        // [[ Code Optimization ]]

        // [[ Code Emit]]
    }
}
