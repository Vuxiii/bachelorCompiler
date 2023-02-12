package com.vuxiii.compiler.CodeEmit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.AddressingMode;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.ArgumentKind;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.FunctionBlock;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Operand;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Register;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Scope;

/**
 * Stack machine!
 */
public class X86Emitter {
    
    private String asm = "";

    private List<Instruction> instructions;
    // private List<Scope> scopes;
    private Map<String, Scope> scopes;
    Map<String, FunctionBlock> functions;

    Map<String, Integer> var_offsets;

    public X86Emitter( List<Instruction> instructions, Map<String, FunctionBlock> functions, Map<String, Scope> scopes ) {
        this.instructions = instructions;
        this.scopes = scopes;
        var_offsets = new HashMap<>();

        this.functions = functions;
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
        push_code( "\n# Exit call" );
        push_code( "movq $60, %rax" );
        push_code( "movq $0, %rdi" );
        push_code( "syscall" );
    }

    public String run() {

        push( ".section .data" );
        push( ".section .text" );
        

        // Scope current_scope = scopes.get(0);


        for ( String function : functions.keySet() ) {
            FunctionBlock fb = functions.get(function);
            
            
            _run( fb.instructions, scopes.get( function ) );
            
        }

        file_header();


        setup_stackpointer();

        _run( instructions, scopes.get( "root" ) );
        
        restore_stackpointer();


        file_footer();
        return asm;
    }

    
    private void _run( List<Instruction> instructions, Scope current_scope ) {
        
        for ( String var : current_scope.get_variables() ) {
            var_offsets.put( var, current_scope.get_variable_offset(var) ); // Capture missing
        }

        for ( String var : current_scope.get_parameters() ) {
            var_offsets.put( var, current_scope.get_parameter_offset(var) );
        }

        System.out.println();

        for ( Instruction instruction : instructions ) {

            switch (instruction.opcode) {
                case ADD: {
                    Operand r1 = instruction.args.get().operand_1.get();
                    Operand r2 = instruction.args.get().operand_2.get();
                    Operand target = instruction.args.get().target.get();
                    if ( r1.addressing_mode == AddressingMode.IMMEDIATE )
                        push_code( "addq $" + r1.get_int() +", " + getReg(r2.get_reg()) ); //TODO: Check for double also.
                    else
                        push_code( "addq " + getReg(r1.get_reg()) +", " + getReg(r2.get_reg()) );
                    push_code( "movq " + getReg(r2.get_reg()) +", " + getReg(target.get_reg()) );
                } break;
                case MINUS: {
                    Operand r1 = instruction.args.get().operand_1.get();
                    Operand r2 = instruction.args.get().operand_2.get();
                    Operand target = instruction.args.get().target.get();
                    if ( r1.addressing_mode == AddressingMode.IMMEDIATE )
                        push_code( "subq $" + r1.get_int() +", " + getReg(r2.get_reg()) ); //TODO: Check for double also.
                    else
                        push_code( "subq " + getReg(r1.get_reg()) +", " + getReg(r2.get_reg()) );
                    push_code( "movq " + getReg(r2.get_reg()) +", " + getReg(target.get_reg()) );
                } break;
                case MULT: { //TODO: Add support for register * literal
                    Operand r1 = instruction.args.get().operand_1.get();
                    Operand r2 = instruction.args.get().operand_2.get();
                    Operand target = instruction.args.get().target.get();
                
                    push_code( "movq " + getReg(r1.get_reg()) + ", " + getReg(Register.RAX) );
                    push_code( "imulq " + getReg(r2.get_reg()) );
                    push_code( "movq " + getReg(Register.RAX) +", " + getReg(target.get_reg()) );
                } break;
                case DIV_INTEGER: {
                    
                    
                } break;


                case LOAD_VARIABLE: {
                    Operand var = instruction.args.get().operand_1.get();
                    Operand target = instruction.args.get().target.get();
                    
                    int offset = var_offsets.get( var.get_string() );
                    int sign = instruction.target_is_parameter ? 1 : -1;

                    push_code ( "" );
                    push_code ("# [[ Loading variable " + var + " ]] " );
                    push_code ("# [[ offset is " + offset + " ]] " );
                    push_code( "movq " + (sign*offset*8) + "(%rbp), " + getReg(target.get_reg()) ); // What offset is the variable stored at
                    
                } break;

                case STORE_VARIABLE: {
                    String var = instruction.args.get().operand_1.get().get_string();
                    Operand src_1 = instruction.args.get().target.get();

                    int offset = var_offsets.get( var );
                    int sign = instruction.target_is_parameter ? 1 : -1;

                    push_code ( "" );
                    push_code ("# [[ Storing variable " + var + " ]] " );
                    push_code ("# [[ offset is " + offset + " ]] " );
                    push_code( "movq " + getReg(src_1.get_reg()) + ", " + (sign*offset*8) + "(" + getReg(Register.RBP) + ")" );
                    // push_code( "movq $" + offset + ", " + getReg( Register.RCX ) );
                    // push_code( "movq " + getReg(src_1) + ", -8(" + getReg(Register.RBP) + ", " + getReg(Register.RCX) + ", 8)" );
                } break;
                
                
                case PUSH: {
                    if ( instruction.args.get().kind == ArgumentKind.ONE_REG )
                        push_code( "push " + getReg(instruction.args.get().operand_1.get().get_reg() ) );
                    else //TODO: Check for double also.
                        push_code( "push $" + instruction.args.get().operand_1.get().get_int()); 

                } break;
                case POP: {
                    push_code( "pop " + getReg(instruction.args.get().operand_1.get().get_reg()) );
                } break;
                
                case LABEL: {
                    push( instruction.args.get().operand_1.get().get_string() + ":" );
                } break;

                case SETUP_STACK: {
                    setup_stackpointer();
                    push_code( "subq $" + (8*current_scope.get_variables().size()) + ", %rsp" );

                } break;

                case RESTORE_STACK: {
                    restore_stackpointer();
                } break;

                case RETURN: {
                    push_code( "ret" );
                    push_code( "" );
                } break;

                case CALL: {
                    // Handle argument passing.
                    push_code( "call " + instruction.args.get().operand_1.get().get_string() );
                } break;

                case PRINT: {                    
                    push_code( "call printStringWithReplace" );
                } break;
                default: {
                    System.out.println( "\u001B[41m\u001B[37m--[[ Emitter Error ]]--\u001B[0m\nMissing implementation for opcode " + instruction.opcode + "\nExiting!");
                    System.exit(-1);
                } break;
            }

        }

    }


    private void setup_stackpointer() {
        push_code( "push " + getReg( Register.RBP ) );
        push_code( "movq " + getReg( Register.RSP) + ", " + getReg( Register.RBP ) + " # Setup stackpointer" );
    }

    private void restore_stackpointer() {
        push_code( "movq " + getReg( Register.RBP) + ", " + getReg( Register.RSP ) + " # Restore stackpointer" );
        push_code( "pop %rbp" );
    }

    private String getReg( Register reg ) { 
        switch (reg) {
            case RAX: return "%rax";
            case RBX: return "%rbx";
            case RCX: return "%rcx";
            case RBP: return "%rbp";
            case RSP: return "%rsp";
            case RDI: return "%rdi";
            case RDX: return "%rdx";
            case RSI: return "%rsi";
            default: return "unknown " + reg;
        }
    }
}
