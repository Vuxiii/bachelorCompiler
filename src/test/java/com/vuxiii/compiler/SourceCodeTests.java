package com.vuxiii.compiler;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.jupiter.api.Test;
 
public class SourceCodeTests {
    @Test
    void nestedFunctionTests() {
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
        print( " %\\n", first + a(a(first)) );
        """;

        String asm = App.runWithInput( input );
        String testname = "test1";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        CommandLine compile = new CommandLine("gcc -no-pie src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c src/test/java/com/vuxiii/compiler/" + testname + ".s -o src/test/java/com/vuxiii/compiler/" + testname);
        CommandLine run = new CommandLine("./" + testname);
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler( out ) );

        try {
            executor.execute(compile);
            executor.execute(run);
            String a = out.toString("UTF-8");
            assertEquals( " 24\n", a );
        } catch (ExecuteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

        String asm = App.runWithInput( input );

        String testname = "test1";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        CommandLine compile = new CommandLine("gcc -no-pie src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c src/test/java/com/vuxiii/compiler/" + testname + ".s -o src/test/java/com/vuxiii/compiler/" + testname);
        CommandLine run = new CommandLine("./" + testname);
        
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler( out ) );

        try {
            executor.execute(compile);
            executor.execute(run);
            String a = out.toString("UTF-8");
            assertEquals( " 1\n 2\n 3\n", a );

        } catch (ExecuteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  

    @Test
    void precedence_test() {
        App.reset_compiler();
        String input = """
        let first: int;
        first = 7;
        let second: int;
        
        print( " %\\n", 4 + 2 * first );
        print( " %\\n", 2 * first + 4 );
    
        print( " %\\n", 2 + 3 * first );
        """;

        String asm = App.runWithInput( input );

        String testname = "test3";
        App.save_to_file( "src/test/java/com/vuxiii/compiler/", testname + ".s", asm );
        CommandLine compile = new CommandLine("gcc -no-pie src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c src/test/java/com/vuxiii/compiler/" + testname + ".s -o src/test/java/com/vuxiii/compiler/" + testname);
        CommandLine run = new CommandLine("./" + testname);
        
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler( out ) );

        try {
            executor.execute(compile);
            executor.execute(run);
            String a = out.toString("UTF-8");
            assertEquals( " 18\n 18\n 23\n", a );

        } catch (ExecuteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}