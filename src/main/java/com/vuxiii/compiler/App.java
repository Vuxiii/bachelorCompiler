package com.vuxiii.compiler;

import com.vuxiii.compiler.CodeEmit.X86Emitter;
import com.vuxiii.compiler.InternalInterpreter.Interpreter;
import com.vuxiii.compiler.Lexer.Lexer;
import com.vuxiii.compiler.Parser.Parser;
import com.vuxiii.compiler.Parser.Nodes.Root;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Visitors.AST_Setup_Parents;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.AST_StackMachine;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.FunctionBlock;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection.StringCollector;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_Printer;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.AST_FixTypes;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.AST_RegisterHeapLayout;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.AST_RegisterStackFrames;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.AST_SymbolCollector;
import com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps.AST_Shrinker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.LRParser;
import com.vuxiii.LR.Settings;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    public static String asm_location = "src/main/java/com/vuxiii/compiler/CodeEmit/Output/";

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Settings.showParsingSteps = false;
        Settings.showGrammar = false;
        Settings.showParsingTable = false;
        String input =  """   
                        type a: { f: *int; };
                        let b: a;
                        b = 42;
                        print("%\\n", b);
                        """;
        input = """
                type rec: {
                    f1: int;
                    f2: int;
                    f3: int;
                };

                let a: *rec;
                let b: rec;
                
                a.f1 = 1;
                a.f2 = 2;
                a.f3 = 3;

                b.f1 = 6;
                b.f2 = 7;
                b.f3 = 8;


                print( "a.f1: %\\n", a.f1 );
                print( "a.f2: %\\n", a.f2 );
                print( "a.f3: %\\n", a.f3 );

                print( "b.f1: %\\n", b.f1 );
                print( "b.f2: %\\n", b.f2 );
                print( "b.f3: %\\n", b.f3 );

                """;
        input = """
                type rec: {
                    f1: int;
                    f2: *int;
                    f3: int;
                };

                let b: rec;

                b.f1 = 6;
                b.f2 = 7;
                b.f3 = 8;

                print( "b.f1: %\\n", b.f1 );
                print( "b.f2: %\\n", b.f2 );
                print( "b.f3: %\\n", b.f3 );
                """;
        input = """ 
                let fun: () -> void;
                let a: int;
                a = 0;
                fun = () -> void {
                    print("%\\n", a);
                    let inner: () -> void;
                    inner = () -> void {
                        let a: int;
                        a = 1;
                        print("%\\n", a);
                    };
                    print("%\\n", a);
                    a = 2;
                };

                fun();
                print("%\\n", a);
                """;
        input = """
                let fun1: () -> int;
                let fun2: () -> int;
                let a: int;
                a = 69;
                fun1 = () -> int {
                    print("a is %\\n", a);
                    return 3;
                };

                fun2 = fun1;

                print("%\\n", fun2());
                fun1();
                """;

        input = """
                let fib: (a: int) -> int;

                fib = (a: int) -> int {
                    print( "a is %\\n", a );
                    if ( a == 1 ) {
                        return 1;
                    } else if ( a == 2 ) {
                        return 1;
                    }
                    return fib(a-1) + fib(a-2); 
                };
                let n: int;
                n = 5;

                print( "The %th fib number is: %\\n", n, fib(n) );
                """;
        
        // input = """
        //         let counter: (a: int) -> int;

        //         counter = (a: int) -> int {
        //             print( "a is %\\n", a );
        //             if ( a == 0 ) {
        //                 return 1;
        //             }
        //             return counter(a-1);
        //         };
        //         let n: int;
        //         n = 3;

        //         print( "The %th fib number is: %\\n", n, counter(n) );
        //         """;
        System.out.println( input );

        // [[ Tokenizer ]]
        List<ASTToken> tokens = Lexer.lex( input );

        
        System.out.println( "Tokens:" );
        
        tokens.forEach( System.out::println );

        line_break();

        Settings.showGrammar = true;
        Settings.showParsingTable = false;
        // [[ Parser ]]
        
        ASTNode ast = Parser.getAST( tokens );

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
        line_break();

        AST_Setup_Parents setup_parents = new AST_Setup_Parents();
        ast.accept(setup_parents);
        
        
        printer = new AST_Printer();
        ast.accept( printer );
        System.out.println( printer.get_ascii() );
    
        System.out.println( ast );

        line_break();
        System.out.println( "TYPE_FIXER BEFORE" );
        line_break();
        printer = new AST_Printer();
        ast.accept( printer );
        System.out.println( printer.get_ascii() );

        AST_FixTypes type_fixer = new AST_FixTypes();
        ast.accept( type_fixer );
        
        line_break();
        System.out.println( "TYPE_FIXER AFTER" );
        line_break();

        
        printer = new AST_Printer();
        ast.accept( printer );
        System.out.println( printer.get_ascii() );

        // [[ Symbol Collecting ]]

        line_break();
        System.out.println( "Symbol collector" );
        line_break();
        
        AST_SymbolCollector v2 = new AST_SymbolCollector();
        ast.accept(v2);
        
        AST_RegisterStackFrames frames = new AST_RegisterStackFrames();
        ast.accept( frames );

        AST_RegisterHeapLayout heaps = new AST_RegisterHeapLayout();
        ast.accept( heaps );

        printer = new AST_Printer();
        ast.accept( printer );
        System.out.println( printer.get_ascii() );

        line_break();
        System.out.println( "String collector" );
        line_break();

        StringCollector str_collector = new StringCollector();
        ast.accept( str_collector );
        
        // [[ Type Checking ]]

        // Skip for now. We only have one type.

        // [[ Code Generation ]]

        // [[ Create Functions ]]
        
        AST_StackMachine generator = new AST_StackMachine();
        ast.accept(generator);

        line_break();
        System.out.println( "Internal Low-Level Representation");
        line_break();


        List<Instruction> instructions = generator.code;
        Map<String, FunctionBlock> fbs = AST_StackMachine.functions;


        System.out.println( "[[ Functions ]]" );

        for ( String function : fbs.keySet() ) {
            FunctionBlock fb = fbs.get(function);
            System.out.println( fb.function_label );
            for ( Instruction instruction : fb.instructions ) {
                System.out.println( instruction );
            }
        }

        System.out.println( "[[ Main Code ]]" );


        for ( Instruction instruction : instructions ) {
            System.out.println( instruction );
        }

        line_break();
        System.out.println( "Running interpreter on the above code" );
        line_break();

        Interpreter interpreter = new Interpreter( generator.code );

        // interpreter.run();

        // [[ Code Optimization ]]

        // [[ Code Emit ]]
        
        line_break();
        System.out.println( "Passing instruction to CodeEmitter");
        line_break();

        X86Emitter emitter = new X86Emitter( instructions, fbs, (Root)ast );

        String asm_code = emitter.run();
        System.out.println( asm_code );

        line_break();

        System.out.println( "Input was:" );
        line_break();

        System.out.println( input );

        save_to_file( asm_location, "asm.s", asm_code );

    }

    public static void reset_compiler() {
        LRParser.reset();
    }

    private static void line_break() {
        System.out.println( "=".repeat(79) );
    }

    public static String runWithInput( String input ) {
        Settings.showParsingSteps = false;
        Settings.showGrammar = false;
        Settings.showParsingTable = false;

        List<ASTToken> tokens = Lexer.lex( input );

        ASTNode ast = Parser.getAST( tokens );

        AST_Shrinker cleaner = new AST_Shrinker();
        ast.accept( cleaner );
        
        AST_Setup_Parents setup_parents = new AST_Setup_Parents();
        ast.accept(setup_parents);
        
        AST_FixTypes type_fixer = new AST_FixTypes();
        ast.accept( type_fixer );
        
        AST_SymbolCollector v2 = new AST_SymbolCollector();
        ast.accept(v2);

        AST_RegisterStackFrames frames = new AST_RegisterStackFrames();
        ast.accept( frames );

        AST_RegisterHeapLayout heaps = new AST_RegisterHeapLayout();
        ast.accept( heaps );

        StringCollector str_collector = new StringCollector();
        ast.accept( str_collector );
        
        AST_StackMachine generator = new AST_StackMachine();
        ast.accept(generator);

        List<Instruction> instructions = generator.code;
        Map<String, FunctionBlock> fbs = AST_StackMachine.functions;

        X86Emitter emitter = new X86Emitter( instructions, fbs, (Root)ast );

        String asm_code = emitter.run();

        return asm_code;
    } 

    public static void save_to_file( String asm_location, String filename, String body ) {
        try {
            FileWriter fw = new FileWriter( asm_location + filename );
            fw.write( body );
            fw.close();
            System.out.println( "To compile and run the generated assembly code, run './comp.sh'\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
