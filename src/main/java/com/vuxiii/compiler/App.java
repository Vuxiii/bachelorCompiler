package com.vuxiii.compiler;

import com.vuxiii.compiler.Lexer.Lexer;
import com.vuxiii.compiler.Parser.Parser;
import com.vuxiii.compiler.Visitors.ASTPrinter;

import java.util.List;

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
        //     print(2+4);
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
        ast.accept(printer);

        System.out.println( printer.get_AST() );

        // [[ Symbol Collecting ]]


        // [[ Type Checking ]]

        // [[ Code Generation ]]

        // [[ Code Optimization ]]

        // [[ Code Emit ]]
        
    }
}
