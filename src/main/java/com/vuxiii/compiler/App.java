package com.vuxiii.compiler;

import com.vuxiii.Regex.Regex;
import com.vuxiii.compiler.Lexer.Lexer;
import com.vuxiii.compiler.Parser.Parser;
import com.vuxiii.compiler.Visitors.ASTPrinter;

import java.util.List;

import com.vuxiii.LR.Grammar;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Settings;

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
        Settings.showParsingSteps = false;
        Settings.showGrammar = false;
        Settings.showParsingTable = false;
        String input = """
            a = 3;
            print(a);
            b=a+5;
            print(2);
        """;
        // input = """
        //     a = 3;
        //     a = a + 4;
        //     print(a);
        // """;
        // input = """
        //     a = 3;
        //     b = 5;
        // """;
        
        // [[ Tokenizer ]]
        List<ASTToken> tokens = Lexer.lex( input );


        Settings.showGrammar = true;
        Settings.showParsingTable = true;
        // [[ Parser ]]
        
        ASTToken ast = Parser.getAST( tokens );

        System.out.println( ast );

        // Debug...

        ASTPrinter printer = new ASTPrinter();
        System.out.println( "============".repeat(3));
        ast.accept(printer);
        System.out.println( "============".repeat(3));

        // [[ Symbol Collecting ]]


        // [[ Type Checking ]]

        // [[ Code Generation ]]

        // [[ Code Optimization ]]

        // [[ Code Emit ]]
        
    }
}
