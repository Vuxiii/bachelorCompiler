package com.vuxiii.compiler.CodeEmit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Register;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Scope;

/**
 * Stack machine!
 */
public class X86Emitter {
    
    private String asm = "";

    private List<Instruction> instructions;
    private List<Scope> scopes;

    Map<String, Integer> var_offsets;

    public X86Emitter( List<Instruction> instructions, List<Scope> scopes ) {
        this.instructions = instructions;
        this.scopes = scopes;
        var_offsets = new HashMap<>();
    }

    private void push( String s ) {
        asm += s + "\n";
    }

    private void push_code( String s ) {
        asm += "    " + s + "\n";
    }

    private void file_header() {
        
        push( ".section .data" );
        push( ".section .text" );
        push( ".global _start" );
        push( "_start:" );
    }

    private void file_footer() {
        // # Exit call
        // movq $60, %rax
        // movq $0, %rdi
        // syscall
        push_code( "# Exit call" );
        push_code( "movq $60, %rax" );
        push_code( "movq $0, %rdi" );
        push_code( "syscall" );
    }

    public String run() {


        // Setup headers
        // Setup variables

        Scope current_scope = scopes.get(0);

        file_header();

        setup_stackpointer();

        int i = 0;
        for ( String var : current_scope.get_variables()) {
            push_code( "push $0 # Making place for variable: " + var );
            var_offsets.put( var, i++ );
        }

        for ( Instruction instruction : instructions ) {

            switch (instruction.opcode) {
                case ADD: {
                    push_code( "addq " + getReg(instruction.args.src_1) +", " + getReg(instruction.args.src_2) );
                    push_code( "movq " + getReg(instruction.args.src_2) +", " + getReg(instruction.args.target) );
                } break;
                case MINUS: {
                    push_code( "subq " + getReg(instruction.args.src_1) +", " + getReg(instruction.args.src_2) );
                    push_code( "movq " + getReg(instruction.args.src_1) +", " + getReg(instruction.args.target) );
                } break;
                case MULT: {
                    if ( instruction.args.value == null ) {
                        Register src_1 = instruction.args.src_1;
                        Register src_2 = instruction.args.src_2;
                        Register target = instruction.args.target;
                        push_code( "movq " + getReg(src_1) + ", " + getReg(Register.RAX) );
                        push_code( "imulq " + getReg(src_2) );
                        push_code( "movq " + getReg(Register.RAX) +", " + getReg(target) );
                    } else {

                    }
                } break;
                case DIV_INTEGER: {
                    
                    
                } break;


                case LOAD_VARIABLE: {
                    String var = instruction.args.variable;
                    int offset = var_offsets.get( var );
                    Register target = instruction.args.target;
                    push_code ( "" );
                    push_code ("# [[ Loading variable " + var + " ]] " );
                    push_code ("# [[ offset is " + offset + " ]] " );
                    push_code( "movq $" + offset + ", " + getReg(target) ); // What offset is the variable stored at
                    push_code( "movq -8(" + getReg(Register.RBP) + ", " + getReg(target) + ", 8), " + getReg(target) );
                } break;

                case STORE_VARIABLE: {
                    String var = instruction.args.variable;
                    int offset = var_offsets.get( var );
                    Register src_1 = instruction.args.src_1;
                    push_code ( "" );
                    push_code ("# [[ Storing variable " + var + " ]] " );
                    push_code ("# [[ offset is " + offset + " ]] " );
                    push_code( "movq $" + offset + ", " + getReg( Register.RCX ) );
                    push_code( "movq " + getReg(src_1) + ", -8(" + getReg(Register.RBP) + ", " + getReg(Register.RCX) + ", 8)" );
                } break;
                
                
                case PUSH: {
                    if ( instruction.args.value == null )
                        push_code( "push " + getReg(instruction.args.src_1) );
                    else
                        push_code( "push $" + ((LexLiteral)instruction.args.value).val);

                } break;
                case POP: {
                    if ( instruction.args.src_1 != null )
                        push_code( "pop " + getReg(instruction.args.src_1) );
                    else
                        push_code( "pop %rax" );

                    
                } break;
                
                case PRINT: {
                    push_code( "movq " + getReg(instruction.args.src_1) + ", %rdi" );
                    push_code( "call printNum" );
                } break;
                default: {
                    System.out.println( "\u001B[41m\u001B[37m--[[ Interpreter Error ]]--\u001B[0m\nMissing implementation for opcode " + instruction.opcode + "\nExiting!");
                    System.exit(-1);
                } break;
            }

        }

        restore_stackpointer();
        file_footer();
        return asm;
    }

    private void setup_stackpointer() {
        push_code( "push " + getReg( Register.RBP ) );
        push_code( "movq " + getReg( Register.RSP) + ", " + getReg( Register.RBP ) + " # Setup stackpointer" );
    }

    private void restore_stackpointer() {
        push_code( "movq " + getReg( Register.RBP) + ", " + getReg( Register.RSP ) + " # Restore stackpointer" );
        push_code( "pop %rax" );
    }

    private String getReg( Register reg ) { 
        switch (reg) {
            case RAX: return "%rax";
            case RBX: return "%rbx";
            case RCX: return "%rcx";
            case RBP: return "%rbp";
            case RSP: return "%rsp";
            default: return "unknown " + reg;
        }
    }
}
