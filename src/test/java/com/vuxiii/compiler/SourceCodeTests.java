package com.vuxiii.compiler;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.jupiter.api.Test;
 
public class SourceCodeTests {

    private List<String> run_and_get_output( String cmd ) throws IOException {
        ProcessBuilder pb = new ProcessBuilder( cmd );
        // pb.directory(new File("."));
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
        Process p = pb.start();
        InputStream stdout = p.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
        List<String> out = reader.lines().collect(Collectors.toList());
        return out;
    }

    @Test
    void nested_function_tests() {
        App.reset_compiler();
        String input = """
        let a: (c:int) -> int;
        a = (b: int) -> int {
            let c: (d:int) -> int;
            c = (d: int) -> int {
                return d + 4;
            };
            return c(b) + 5;
        };

        let first: int;
        first = 3;
        print( "%\\n", first + a(a(first)) );
        """;

        String asm = App.runWithInput( input );
        String testname = "test1";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        
        File utils = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s");
        assertEquals( utils.exists(), true );
        
        File gc = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c");
        assertEquals( gc.exists(), true );
        
        File ass = new File( "src/test/java/com/vuxiii/compiler/", testname + ".s" );
        assertEquals( ass.exists(), true );
        
        
        CommandLine compile = new CommandLine( "gcc" );
        compile.addArgument( "-no-pie" );
        compile.addArgument( utils.getPath() );
        compile.addArgument( gc.getPath() );
        compile.addArgument( ass.getPath() );
        compile.addArgument( "-o" );
        compile.addArgument( "src/test/java/com/vuxiii/compiler/" + testname );
        
        DefaultExecutor executor = new DefaultExecutor();

        try {
            executor.execute(compile );

            File outfile = new File( "src/test/java/com/vuxiii/compiler/" + testname );
            assertEquals( outfile.exists(), true );

            List<String> out = run_and_get_output( "src/test/java/com/vuxiii/compiler/" + testname );
            
            assertLinesMatch( List.of("24" ), out);
        } catch (ExecuteException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    void if_else_tests() {
        App.reset_compiler();
        String input = """
        let bas: boolean;
        bas = false;
        let a: int;

        if ( true ) {
            a = 1;
        } else if (false) {
            a = 2;
        } else {
            a = 3;
        }

        print( "%\\n", a );

        if ( false ) {
            a = 1;
        } else if (true) {
            a = 2;
        } else {
            a = 3;
        }

        print( "%\\n", a );

        if ( false ) {
            a = 1;
        } else if (false) {
            a = 2;
        } else {
            a = 3;
        }

        print( "%\\n", a );
        """;

        String asm = App.runWithInput( input );

        String testname = "test1";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        
        File utils = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s");
        assertEquals( utils.exists(), true );
        
        File gc = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c");
        assertEquals( gc.exists(), true );
        
        File ass = new File( "src/test/java/com/vuxiii/compiler/", testname + ".s" );
        assertEquals( ass.exists(), true );
        
        
        CommandLine compile = new CommandLine( "gcc" );
        compile.addArgument( "-no-pie" );
        compile.addArgument( utils.getPath() );
        compile.addArgument( gc.getPath() );
        compile.addArgument( ass.getPath() );
        compile.addArgument( "-o" );
        compile.addArgument( "src/test/java/com/vuxiii/compiler/" + testname );
        
        DefaultExecutor executor = new DefaultExecutor();


        try {
            executor.execute(compile );

            File outfile = new File( "src/test/java/com/vuxiii/compiler/" + testname );
            assertEquals( outfile.exists(), true );

            List<String> out = run_and_get_output( "src/test/java/com/vuxiii/compiler/" + testname );
            
            assertLinesMatch( List.of("1", "2", "3" ), out);

        } catch (ExecuteException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }  

    @Test
    void precedence_test() {
        App.reset_compiler();
        String input = """
        let first: int;
        first = 7;
        let second: int;
        
        print( "Number 1 is: %\\n", 4 + 2 * first );
        print( "Number 2 is: %\\n", 2 * first + 4 );
        print( "Number 3 is: %\\n", 2 + 3 * first );
        """;

        

        String asm = App.runWithInput( input );

        String testname = "test3";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        
        File utils = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s");
        assertEquals( utils.exists(), true );
        
        File gc = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c");
        assertEquals( gc.exists(), true );
        
        File ass = new File( "src/test/java/com/vuxiii/compiler/", testname + ".s" );
        assertEquals( ass.exists(), true );
        
        
        CommandLine compile = new CommandLine( "gcc" );
        compile.addArgument( "-no-pie" );
        compile.addArgument( utils.getPath() );
        compile.addArgument( gc.getPath() );
        compile.addArgument( ass.getPath() );
        compile.addArgument( "-o" );
        compile.addArgument( "src/test/java/com/vuxiii/compiler/" + testname );
        
        DefaultExecutor executor = new DefaultExecutor();


        try {
            executor.execute(compile );

            File outfile = new File( "src/test/java/com/vuxiii/compiler/" + testname );
            assertEquals( outfile.exists(), true );

            List<String> out = run_and_get_output( "src/test/java/com/vuxiii/compiler/" + testname );
            
            assertLinesMatch( List.of("Number 1 is: 18","Number 2 is: 18","Number 3 is: 23" ), out);

        } catch (ExecuteException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            System.out.println( "Cause: " + e.getCause().getMessage() );
            
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    void print_with_correct_substitutions_test() {
        App.reset_compiler();
        String input = """
        print( "Hej %, du bor i %. Du er % aar. I Januar var du % aar gammel!\\n", "William", "Odense", 23, 22 );
        """;

        String asm = App.runWithInput( input );

        String testname = "test4";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        
        File utils = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s");
        assertEquals( utils.exists(), true );
        
        File gc = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c");
        assertEquals( gc.exists(), true );
        
        File ass = new File( "src/test/java/com/vuxiii/compiler/", testname + ".s" );
        assertEquals( ass.exists(), true );
        
        
        CommandLine compile = new CommandLine( "gcc" );
        compile.addArgument( "-no-pie" );
        compile.addArgument( utils.getPath() );
        compile.addArgument( gc.getPath() );
        compile.addArgument( ass.getPath() );
        compile.addArgument( "-o" );
        compile.addArgument( "src/test/java/com/vuxiii/compiler/" + testname );
        
        DefaultExecutor executor = new DefaultExecutor();

        try {
            executor.execute(compile);

            List<String> out = run_and_get_output( "src/test/java/com/vuxiii/compiler/" + testname );
            
            assertLinesMatch( List.of("Hej William, du bor i Odense. Du er 23 aar. I Januar var du 22 aar gammel!" ), out);

        } catch (ExecuteException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    void if_else_test() {
        App.reset_compiler();
        String input = """
        if ( 1 ) {
            print( "If\\n" );
        }

        if ( 4 - 4 ) {
            print( "If\\n" );
        } else {
            print( "Else\\n" );
        }

        if ( 4 - 4 ) {
            print( "If\\n" );
        } else if ( 1 ) {
            print( "Else if\\n" );
        } else {
            print( "Else\\n" );
        }

        print("Fix ending...\\n");
        """;

        String asm = App.runWithInput( input );

        String testname = "test5";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        
        File utils = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s");
        assertEquals( utils.exists(), true );
        
        File gc = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c");
        assertEquals( gc.exists(), true );
        
        File ass = new File( "src/test/java/com/vuxiii/compiler/", testname + ".s" );
        assertEquals( ass.exists(), true );
        
        
        CommandLine compile = new CommandLine( "gcc" );
        compile.addArgument( "-no-pie" );
        compile.addArgument( utils.getPath() );
        compile.addArgument( gc.getPath() );
        compile.addArgument( ass.getPath() );
        compile.addArgument( "-o" );
        compile.addArgument( "src/test/java/com/vuxiii/compiler/" + testname );
        
        DefaultExecutor executor = new DefaultExecutor();


        try {
            executor.execute(compile);

            List<String> out = run_and_get_output( "src/test/java/com/vuxiii/compiler/" + testname );
            
            assertLinesMatch( List.of("If", "Else", "Else if", "Fix ending..." ), out);

        } catch (ExecuteException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    void if_else_custom_guards_test() {
        App.reset_compiler();
        String input = """
        if ( 2+2 == 5 ) 
            print("2 + 2 == 5 -> true\\n");
        else
            print("2 + 2 == 5 -> false\\n");
        
        if ( 2+2 != 5 ) 
            print("2 + 2 != 5 -> true\\n");
        else
            print("2 + 2 != 5 -> false\\n");
    
        if ( true == true ) 
            print( "true == true -> true\\n" );
        else
            print( "true == true -> false\\n" );
        
        if ( true != true ) 
            print( "true != true -> true\\n" );
        else
            print( "true != true -> false\\n" );
        
        if ( false == false ) 
            print( "false == false -> true\\n" );
        else
            print( "false == false -> false\\n" );
        
        if ( false != false ) 
            print( "false != false -> true\\n" );
        else
            print( "false != false -> false\\n" );
        
        print("end\\n");
        """;

        String asm = App.runWithInput( input );

        String testname = "test6";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        
        File utils = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s");
        assertEquals( utils.exists(), true );
        
        File gc = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c");
        assertEquals( gc.exists(), true );
        
        File ass = new File( "src/test/java/com/vuxiii/compiler/", testname + ".s" );
        assertEquals( ass.exists(), true );
        
        
        CommandLine compile = new CommandLine( "gcc" );
        compile.addArgument( "-no-pie" );
        compile.addArgument( utils.getPath() );
        compile.addArgument( gc.getPath() );
        compile.addArgument( ass.getPath() );
        compile.addArgument( "-o" );
        compile.addArgument( "src/test/java/com/vuxiii/compiler/" + testname );
        
        DefaultExecutor executor = new DefaultExecutor();


        try {
            executor.execute(compile);

            List<String> out = run_and_get_output( "src/test/java/com/vuxiii/compiler/" + testname );
            
            assertLinesMatch( List.of("2 + 2 == 5 -> false", "2 + 2 != 5 -> true", "true == true -> true", "true != true -> false", "false == false -> true", "false != false -> false", "end" ), out);

        } catch (ExecuteException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    void single_if_test() {
        App.reset_compiler();
        String input = """
        if ( 1 ) {
            print( "If\\n" );
        }
        """;

        String asm = App.runWithInput( input );

        String testname = "test7";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        
        File utils = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s");
        assertEquals( utils.exists(), true );
        
        File gc = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c");
        assertEquals( gc.exists(), true );
        
        File ass = new File( "src/test/java/com/vuxiii/compiler/", testname + ".s" );
        assertEquals( ass.exists(), true );
        
        
        CommandLine compile = new CommandLine( "gcc" );
        compile.addArgument( "-no-pie" );
        compile.addArgument( utils.getPath() );
        compile.addArgument( gc.getPath() );
        compile.addArgument( ass.getPath() );
        compile.addArgument( "-o" );
        compile.addArgument( "src/test/java/com/vuxiii/compiler/" + testname );
        
        DefaultExecutor executor = new DefaultExecutor();


        try {
            executor.execute(compile);

            List<String> out = run_and_get_output( "src/test/java/com/vuxiii/compiler/" + testname );
            
            assertLinesMatch( List.of("If" ), out);

        } catch (ExecuteException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    void record_test() {
        App.reset_compiler();
        String input = """
        type nested: {
            a: int;
            b: int;
            c: int;
        };
        
        let rec: nested;
        
        rec.a = 42;
        rec.b = 69;
        rec.c = 512;
        
        
        print( "Field a is: %\\n", rec.a );
        print( "Field b is: %\\n", rec.b );
        print( "Field c is: %\\n", rec.c );
        
        """;

        String asm = App.runWithInput( input );

        String testname = "test8";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        
        File utils = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s");
        assertEquals( utils.exists(), true );
        
        File gc = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c");
        assertEquals( gc.exists(), true );
        
        File ass = new File( "src/test/java/com/vuxiii/compiler/", testname + ".s" );
        assertEquals( ass.exists(), true );
        
        
        CommandLine compile = new CommandLine( "gcc" );
        compile.addArgument( "-no-pie" );
        compile.addArgument( utils.getPath() );
        compile.addArgument( gc.getPath() );
        compile.addArgument( ass.getPath() );
        compile.addArgument( "-o" );
        compile.addArgument( "src/test/java/com/vuxiii/compiler/" + testname );
        
        DefaultExecutor executor = new DefaultExecutor();


        try {
            executor.execute(compile);

            List<String> out = run_and_get_output( "src/test/java/com/vuxiii/compiler/" + testname );
            
            assertLinesMatch( List.of("Field a is: 42", "Field b is: 69", "Field c is: 512" ), out);

        } catch (ExecuteException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    void static_scoping_test() {
        App.reset_compiler();
        String input = """
        let fun: () -> void;
        let a: int;
        a = 0;
        fun = () -> void {
            print("%\\n", a);
            let inner = () -> void;
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

        String asm = App.runWithInput( input );

        String testname = "test9";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        
        File utils = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s");
        assertEquals( utils.exists(), true );
        
        File gc = new File("src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c");
        assertEquals( gc.exists(), true );
        
        File ass = new File( "src/test/java/com/vuxiii/compiler/", testname + ".s" );
        assertEquals( ass.exists(), true );
        
        
        CommandLine compile = new CommandLine( "gcc" );
        compile.addArgument( "-no-pie" );
        compile.addArgument( utils.getPath() );
        compile.addArgument( gc.getPath() );
        compile.addArgument( ass.getPath() );
        compile.addArgument( "-o" );
        compile.addArgument( "src/test/java/com/vuxiii/compiler/" + testname );
        
        DefaultExecutor executor = new DefaultExecutor();


        try {
            executor.execute(compile);

            List<String> out = run_and_get_output( "src/test/java/com/vuxiii/compiler/" + testname );
            
            assertLinesMatch( List.of("0", "1", "0", "2" ), out);

        } catch (ExecuteException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }
}
