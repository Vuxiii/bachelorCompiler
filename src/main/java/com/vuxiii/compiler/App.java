package com.vuxiii.compiler;

import com.vuxiii.compiler.Lexer.Lexer;
import com.vuxiii.compiler.Parser.Parser;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_Printer;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_SimplePrinter;
import com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps.AST_Shrinker;

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
            print(a);
        """;
        input = """
            a = 3;
            a = a + 4;
            a = 5 - a;
            print(a);
        """;
        
        input = """
            a = 1 + 2 + 3;
        """;
        input = """
            a = ((3 + b) * 2) / 5;
        """;
        input = """
            a = 3 + b * 2 / 5;
            b = 10;
        """;
        
        // [[ Tokenizer ]]
        List<ASTToken> tokens = Lexer.lex( input );


        Settings.showGrammar = true;
        Settings.showParsingTable = true;
        // [[ Parser ]]
        
        ASTToken ast = Parser.getAST( tokens );

        System.out.println( ast );

        // Debug...

        // SimplePrinter printer = new SimplePrinter();
        // ast.accept(printer);

        AST_Printer printer = new AST_Printer();
        ast.accept(printer);

        System.out.println( printer.get_ascii() );

        // --[[ Cleanup some of the boilerplate from the language ]]--

        AST_Shrinker cleaner = new AST_Shrinker();
        System.out.println( ast );
        ast.accept( cleaner );
        // ast.accept(printer);
        
        printer = new AST_Printer();
        ast.accept( printer );
        System.out.println( printer.get_ascii() );


        System.out.println( ast );

        // [[ Symbol Collecting ]]



        // [[ Type Checking ]]

        // [[ Code Generation ]]

        // [[ Code Optimization ]]

        // [[ Code Emit ]]
        
    }
}
