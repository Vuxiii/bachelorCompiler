package com.vuxiii.compiler;

import com.vuxiii.compiler.InternalInterpreter.Interpreter;
import com.vuxiii.compiler.Lexer.Lexer;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Parser;
// import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.AST_CodeGenerator;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.AST_StackMachine;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_Printer;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_SimplePrinter;
import com.vuxiii.compiler.VisitorPattern.Visitors.Optimizers.ConstantPropagation;
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
            a = (3 + 1);
        """;
        input = """
            b = 1.0;
            c = 5.;
            a = 3 + b * (10 / 5);
            b = 10 + a;
            print( a );
            print( b );
            print( 45 );
        """;
        // input = """
        //     b = 1;
        //     a = 3 + 5 * (10 / 5);
        //     b = 10 + a;
        //     print( a );
        //     print( b );
        //     print( 45 );
        // """;
        
        // input = """
        //     a = 7 + 13 + 9 * 2 / 2;
        //     print( a );
        // """;
        
        // [[ Tokenizer ]]
        List<ASTToken> tokens = Lexer.lex( input );


        Settings.showGrammar = true;
        Settings.showParsingTable = true;
        // [[ Parser ]]
        
        ASTToken ast = Parser.getAST( tokens );

        System.out.println( ast );
        
        line_break();

        AST_Printer printer = new AST_Printer();
        ast.accept(printer);
        System.out.println( printer.get_ascii() );

        line_break();
        // --[[ Cleanup some of the boilerplate from the language ]]--

        AST_Shrinker cleaner = new AST_Shrinker();
        System.out.println( ast );
        ast.accept( cleaner );
        
        line_break();
        
        printer = new AST_Printer();
        ast.accept( printer );
        System.out.println( printer.get_ascii() );
    
        System.out.println( ast );

        line_break();

        // ConstantPropagation cp = new ConstantPropagation();
        // do {
        //     cp.run_again = false;
        //     ast.accept( cp );
        // } while ( cp.run_again );

        printer = new AST_Printer();
        ast.accept( printer );
        System.out.println( printer.get_ascii() );

        // [[ Symbol Collecting ]]
        AST_SymbolCollector symbolCollector = new AST_SymbolCollector();
        ast.accept( symbolCollector );

        List<LexIdent> variables = symbolCollector.get_variables();

        line_break();

        System.out.println( "Variables in program:" );
        variables.forEach( System.out::println );

        line_break();

        // [[ Type Checking ]]

        // Skip for now. We only have one type.

        // [[ Code Generation ]]

        AST_StackMachine generator = new AST_StackMachine();
        ast.accept(generator);

        System.out.println( "Internal Low-Level Representation");



        line_break();
        for ( Instruction instruction : generator.code ) {
            System.out.println( instruction );
        }

        line_break();
        // System.out.println( "Running interpreter on the above code" );

        // Interpreter interpreter = new Interpreter( generator.code );

        // interpreter.run();

        // line_break();

        // [[ Code Optimization ]]

        // [[ Code Emit ]]
        
    }

    private static void line_break() {
        System.out.println( "=".repeat(79) );
    }
}
