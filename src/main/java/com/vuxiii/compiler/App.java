package com.vuxiii.compiler;

import com.vuxiii.compiler.CodeEmit.X86Emitter;
import com.vuxiii.compiler.InternalInterpreter.Interpreter;
import com.vuxiii.compiler.Lexer.Lexer;
import com.vuxiii.compiler.Parser.Parser;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.Parser.Nodes.StatementKind;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.AST_StackMachine;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.FunctionBlock;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection.StringCollector;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection.StringNode;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_Printer;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.AST_FixTypes;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.AST_SymbolCollector;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Scope;
import com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps.AST_Shrinker;
import com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps.AST_Shrinker_Statement;
import com.vuxiii.compiler.VisitorPattern.Visitors.TreeCollaps.AST_Shrinker_Statement_Collector;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

            my_second_function( 2, 7 );
        """;
        input = """

            let b: int;
            b = 4;

            print( b * 2 + 4);

        """;
        input = """
        let first: int;
        first = 5;
        let third: int;
        third = 2 * first;

        let second: int;
        second = 20;
        print( first + second * third );

        

        """;
        input = """
        if ( 4 + 4 ) {
            print( "If" );
        } 

        if ( 4 + 4 ) {
            print( "If" );
        } else {
            print( "Else" );
        };

        if ( 4 + 4 ) {
            print( "If" );
        } else if ( 5 + 5 ) {
            print( "Else if " );
        } else {
            print( "Else" );
        };

        """;
        input = """

        if ( 4 + 4 ) {
            print( "If" );
        } else if ( 5 + 5 ) {
            print( "Else if 1" );
        } else if ( 5 + 50 ) {
            print( "Else if 2" );
        } else {
            print( "Else" );
        };

        """;
        input = """
        let first: int;
        first = 7;
        let second: int;
        
        print( " %\\n", 4 + 2 * first );
        print( " %\\n", 2 * first + 4 );

        print( " %\\n", 2 + 3 * first );
        """;
        input = """

        let bas: boolean;
        bas = false;
        let a: int;

        if ( true ) {
            a = 1;
        } else if (false) {
            a = 2;
        } else {
            a = 3;
        };

        print( " %\\n", a );

        if ( false ) {
            a = 1;
        } else if (true) {
            a = 2;
        } else {
            a = 3;
        };

        print( " %\\n", a );

        if ( false ) {
            a = 1;
        } else if (false) {
            a = 2;
        } else {
            a = 3;
        };

        print( " %\\n", a );
        """;

        

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

        // [[ Symbol Collecting ]]


        line_break();
        System.out.println( "Symbol collector" );
        line_break();
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

        Map<String, Scope> scope_map = symbolCollector.scope_map; 

        line_break();
        System.out.println( "String collector" );
        line_break();

        StringCollector str_collector = new StringCollector();
        ast.accept( str_collector );

        Map<Print, StringNode> strings = str_collector.strings;

        // for ( String s : strings.keySet() ) {
        //     System.out.println( s + " -> " + strings.get( s ) );
        // }



        // [[ Type Checking ]]

        // Skip for now. We only have one type.

        // [[ Code Generation ]]

        // [[ Create Functions ]]



        
        AST_StackMachine generator = new AST_StackMachine( symbolCollector.functions, scope_map, strings );
        ast.accept(generator);

        line_break();
        System.out.println( "Internal Low-Level Representation");
        line_break();


        List<Instruction> instructions = generator.code;
        Map<String, FunctionBlock> fbs = generator.functions;


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

        X86Emitter emitter = new X86Emitter( instructions, fbs, scope_map, strings );

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
