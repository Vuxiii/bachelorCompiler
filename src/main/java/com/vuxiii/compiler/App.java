package com.vuxiii.compiler;

import com.vuxiii.compiler.CodeEmit.X86Emitter;
import com.vuxiii.compiler.InternalInterpreter.Interpreter;
import com.vuxiii.compiler.Lexer.Lexer;
import com.vuxiii.compiler.Parser.Parser;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.AST_StackMachine;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.FunctionBlock;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_Printer;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.AST_FixTypes;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.AST_SymbolCollector;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Scope;
import com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps.AST_Shrinker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vuxiii.LR.Records.ASTToken;
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
            {
                c = 43;
                print( c - 1 );
            }
            [..]{
                b = 3;
                print(b);
            }
            [ a ] {
                print(a);
            }
            [ a, b ] {
                print(a);
            }
            print(a);
            """;
        // input = """
        //     b = 1.0;
        //     c = 5.;
        //     a = 3 + b * (10 / 5);
        //     b = 10 + a;
        //     print( a );
        //     print( b );
        //     print( 45 );
        //     """;
        // input = """
        //     b = 1;
        //     a = 3 + 5 * (10 / 5);
        //     b = 10 + a;
        //     print( a );
        //     print( b );
        //     print( 45 );
        //     """;
        // Capture example.
        input = """
            let a: int;
            a = 3;
            [ a ] {

                print( a + 2 );
                let n: int;

                [a, n ] {
                    let c: int;
                    print( a + n );

                    [..]{
                        print( a * n + c );
                    }
                }
            }
            print( a );
            """;
        input = """
            let type string: int;
            let type Person: {
                name: string,
                age: int
            };
        """;
        input = """
            let type string: int;
        """;
        // input = """
        //     let type char: int;
        //     let type string: char;
        //     let type Person: {
        //         name: string;
        //         sure_name: char;
        //     };
        //     let type Dansker: Person;
        //     let name: string;
        //     let letter: char;
        //     let william: Person;

        //     let age: int;
        //     age = 3;

        // """;
        // input = """
        //     let type char: int;
        //     let type string: char;
        // """;
        input = """
            let type Person: {
                name: int;
                sur_name: int;
                father: Person;
            };
        """;
        input = """
            let type integer: int;
            let a: integer;
            a = 4;
            let b: integer;
            b = 4 * a;
            let c: int;
            c = 42;
            [a, b] {
                print(a);
                b = b + 5;
                print( b );
            }
            print( c + b );
        """;
        input = """
            type string: int;
            type my_func: ( name: string, age: int );
            let a: my_func;

            a = ( name: string, age: int ) {
                print( age );
            };

        """;
        input = """
            type string: int;
            type my_func: ();
            type second: ( z: int ) -> int;
            let a: my_func;
            let b: second;

            a = () {
                print( 2 );
            };

            b = ( z: int ) -> int {
                print( z + 3 );
            };

            a();
            b(3);

        """;
        input = """
            type functype: (x: int) -> int;
            let a: ( z: int ) -> int;
            let b: functype;

            a = ( z: int ) -> int {
                let inner_var: int;
                inner_var = 2;
                print( inner_var + z );
            };
        """;
        input = """
            let a: int;
            a = 3;
            let my_function: ( x: int ) -> int;
            let b: int;

            b = 2;

            my_function = ( x: int ) -> int {
                let inner_var: int;
                inner_var = 3;
                print( x );
            };

            let my_second_function: ( x: int, b: int ) -> int;
            my_second_function = (x: int, b: int) -> int {
                let inner_fn: ( z: int ) -> int;
                inner_fn = ( z: int ) -> int {
                    let inner_fn_var: int;
                    print( z );
                };
                print(x+b);
            };

            my_second_function( 2, 2 );
        """;
    
        
        // [[ Tokenizer ]]
        List<ASTToken> tokens = Lexer.lex( input );

        
        System.out.println( "Tokens:" );
        
        tokens.forEach( System.out::println );

        line_break();

        Settings.showGrammar = true;
        Settings.showParsingTable = false;
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


        // ConstantPropagation cp = new ConstantPropagation();
        // do {
        //     cp.run_again = false;
        //     ast.accept( cp );
        // } while ( cp.run_again );

        // printer = new AST_Printer();
        // ast.accept( printer );
        // System.out.println( printer.get_ascii() );
        
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
        line_break();

        // [[ Symbol Collecting ]]
        AST_SymbolCollector symbolCollector = new AST_SymbolCollector();
        ast.accept( symbolCollector );

        List<Scope> scopes = symbolCollector.scopes;
        
        int i = 0;
        for ( Scope scope : scopes ) {
            System.out.println( "Scope [" + (i++) + "]" );
            System.out.println( "local vars:" );
            for ( String var : scope.get_variables() )
                System.out.println( "  " + var );
            System.out.println( "captured vars:" );
            for ( String capture : scope.get_captures() )
                System.out.println( "  " + capture );
                
            System.out.println();
        }

        System.out.println( symbolCollector.scope_map.keySet() );
        symbolCollector.scope_map.keySet().forEach( k -> { System.out.println( "Key: " + k + " -> " + symbolCollector.scope_map.get(k) ); } );

        line_break();

        // [[ Type Checking ]]

        // Skip for now. We only have one type.

        // [[ Code Generation ]]

        // [[ Create Functions ]]



        
        AST_StackMachine generator = new AST_StackMachine( symbolCollector.functions );
        ast.accept(generator);

        System.out.println( "Internal Low-Level Representation");


        List<Instruction> instructions = generator.code;
        Map<String, FunctionBlock> fbs = generator.functions;

        line_break();

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

        Interpreter interpreter = new Interpreter( generator.code );

        interpreter.run();

        // [[ Code Optimization ]]

        // [[ Code Emit ]]
        
        line_break();

        System.out.println( "Passing instruction to CodeEmitter");
        line_break();

        X86Emitter emitter = new X86Emitter( instructions, fbs, scopes );

        String asm_code = emitter.run();
        System.out.println( asm_code );

        line_break();

        System.out.println( "Input was:" );
        line_break();

        System.out.println( input );

        save_to_file( asm_location, "asm.s", asm_code );

    }

    private static void line_break() {
        System.out.println( "=".repeat(79) );
    }

    private static void save_to_file( String asm_location, String filename, String body ) {
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
