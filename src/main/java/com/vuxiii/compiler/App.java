package com.vuxiii.compiler;

import com.vuxiii.compiler.Lexer.Lexer;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Parser;
// import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.AST_CodeGenerator;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.AST_StackMachine;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_Printer;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_SimplePrinter;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.AST_SymbolCollector;
import com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps.AST_Shrinker;

import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

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
            a = ((3 + b) * 2) / 5;
        """;
        input = """
            a = 3 + b * 2 / 5;
            b = 10;
        """;
        
        input = """
            a = 7 + 13 + 9;
        """;
        // Above should give the following assembly code

        // move 1 rbx
        // move 2 rcx
        // add rbx rcx -> a

        
        // [[ Tokenizer ]]
        List<ASTToken> tokens = Lexer.lex( input );


        Settings.showGrammar = true;
        Settings.showParsingTable = true;
        // [[ Parser ]]
        
        ASTToken ast = Parser.getAST( tokens );

        System.out.println( ast );


        AST_Printer printer = new AST_Printer();
        ast.accept(printer);
        System.out.println( printer.get_ascii() );

        // --[[ Cleanup some of the boilerplate from the language ]]--

        AST_Shrinker cleaner = new AST_Shrinker();
        System.out.println( ast );
        ast.accept( cleaner );
        
        
        printer = new AST_Printer();
        ast.accept( printer );
        System.out.println( printer.get_ascii() );
    
        System.out.println( ast );

        // [[ Symbol Collecting ]]
        AST_SymbolCollector symbolCollector = new AST_SymbolCollector();
        ast.accept( symbolCollector );

        List<LexIdent> variables = symbolCollector.get_variables();

        System.out.println( "Variables in program:" );
        variables.forEach( System.out::println );

        

        // [[ Type Checking ]]

        // Skip for now. We only have one type.

        // [[ Code Generation ]]

        AST_StackMachine generator = new AST_StackMachine();
        ast.accept(generator);

        System.out.println();

        for ( Instruction instruction : generator.code ) {
            System.out.println( instruction );
        }


        // [[ Code Optimization ]]

        // [[ Code Emit ]]
        
    }
}
