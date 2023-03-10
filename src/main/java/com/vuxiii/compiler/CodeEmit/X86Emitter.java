package com.vuxiii.compiler.CodeEmit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.AddressingMode;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.ArgumentKind;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.FunctionBlock;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Operand;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Register;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection.StringNode;
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

    Map<Print, StringNode> string_buffers;

    private static Operand rsp = Operand.from_register(Register.RSP, AddressingMode.REGISER);
    private static Operand rax = Operand.from_register(Register.RAX, AddressingMode.REGISER);
    private static Operand rbx = Operand.from_register(Register.RBX, AddressingMode.REGISER);
    private static Operand rcx = Operand.from_register(Register.RCX, AddressingMode.REGISER);
    private static Operand rdi = Operand.from_register(Register.RDI, AddressingMode.REGISER);
    private static Operand rsi = Operand.from_register(Register.RSI, AddressingMode.REGISER);
    private static Operand rdx = Operand.from_register(Register.RDX , AddressingMode.REGISER);
    private static Operand rbp = Operand.from_register(Register.RBP, AddressingMode.REGISER);

    public X86Emitter( List<Instruction> instructions, Map<String, FunctionBlock> functions, Map<String, Scope> scopes, Map<Print, StringNode> buffers ) {
        this.instructions = instructions;
        this.scopes = scopes;
        var_offsets = new HashMap<>();

        this.functions = functions;
        this.string_buffers = buffers;
    }

    private void push_no_offset( String s ) {
        asm += s + "\n";
    }

    private void push_code( String s ) {
        asm += "    " + s + "\n";
    }

    private void file_header() {
        
        push_no_offset( ".section .data" );
        push_no_offset( ".section .text" );
        push_no_offset( ".global _start" );
        push_no_offset( "_start:" );
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

        push_no_offset( ".section .data" );
        
        // Insert the substitute buffers.

        for ( Print print_node : string_buffers.keySet() ) {
            StringNode node = string_buffers.get( print_node );
            push_no_offset( node.name + ": .ascii " + node.str_literal );
            push_no_offset( node.stop_name + ": .space " +  node.stop_indicators.size() * 8 );
            push_no_offset( node.substitute_name + ": .space " +  node.substitutes.size() * 8 );
        }
        
        push_no_offset( ".section .text" );
        

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
                    
                    push_code( "addq " + ope_string(r1) +", " + ope_string(r2) ); //TODO: Check for double also.
                    
                    push_code( "movq " + ope_string(r2) +", " + ope_string(target) );
                } break;
                case MINUS: {
                    Operand r1 = instruction.args.get().operand_1.get();
                    Operand r2 = instruction.args.get().operand_2.get();
                    Operand target = instruction.args.get().target.get();
                    push_code( "subq " + ope_string(r1) +", " + ope_string(r2) ); //TODO: Check for double also.

                    push_code( "movq " + ope_string(r2) +", " + ope_string(target) );
                } break;
                case MULT: { //TODO: Add support for register * literal
                    Operand r1 = instruction.args.get().operand_1.get();
                    Operand r2 = instruction.args.get().operand_2.get();
                    Operand target = instruction.args.get().target.get();
                    push_code( "movq " + ope_string(r1) + ", " + ope_string( rax ) );
                    push_code( "imulq " + ope_string(r2) );
                    push_code( "movq " + ope_string(rax) +", " + ope_string(target) );
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
                    push_code( "movq " + (sign*offset*8) + "(%rbp), " + ope_string(target) ); // What offset is the variable stored at
                    
                } break;

                case STORE_VARIABLE: {
                    String var = instruction.args.get().operand_1.get().get_string();
                    Operand src_1 = instruction.args.get().target.get();

                    int offset = var_offsets.get( var );
                    int sign = instruction.target_is_parameter ? 1 : -1;

                    push_code ( "" );
                    push_code ("# [[ Storing variable " + var + " ]] " );
                    push_code ("# [[ offset is " + offset + " ]] " );
                    push_code( "movq " + ope_string(src_1) + ", " + (sign*offset*8) + "(" + ope_string(rbp) + ")" );
                    // push_code( "movq $" + offset + ", " + getReg( Register.RCX ) );
                    // push_code( "movq " + getReg(src_1) + ", -8(" + getReg(Register.RBP) + ", " + getReg(Register.RCX) + ", 8)" );
                } break;
                
                
                case PUSH: {
                    // if ( instruction.args.get().kind == ArgumentKind.ONE_REG )
                        push_code( "push " + ope_string(instruction.args.get().operand_1.get() ) );

                } break;
                case POP: {
                    push_code( "pop " + ope_string(instruction.args.get().operand_1.get()) );
                } break;
                
                case LABEL: {
                    push_no_offset( instruction.args.get().operand_1.get().get_string() + ":" );
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
                case MOVE: {
                    Operand ope_1 = instruction.args.get().operand_1.get();
                    Operand ope_2 = instruction.args.get().target.get();
                    String fst = ope_string( ope_1 );
                    String snd = ope_string( ope_2 );

                    push_code( "movq " + fst + ", " + snd );
                } break;
                case LEA: {
                    Operand ope_1 = instruction.args.get().operand_1.get();
                    Operand ope_2 = instruction.args.get().target.get();
                    String fst = ope_1.get_string();
                    String snd = ope_string( ope_2 );

                    push_code( "leaq " + fst + ", " + snd );
                } break;
                case PRINT: {                    
                    push_code( "call printStringWithReplace" );
                } break;
                case COMMENT: {
                    push_no_offset( "" );
                    push_no_offset( "# " + instruction.args.get().operand_1.get().get_string() );
                    push_no_offset( "" );
                } break;
                default: {
                    System.out.println( "\u001B[41m\u001B[37m--[[ Emitter Error ]]--\u001B[0m\nMissing implementation for opcode " + instruction.opcode + "\nExiting!");
                    System.exit(-1);
                } break;
            }

        }

    }


    private void setup_stackpointer() {
        push_code( "push " + ope_string( rbp ) );
        push_code( "movq " + ope_string( rsp ) + ", " 
                           + ope_string( rbp ) + " # Setup stackpointer" );
    }

    private void restore_stackpointer() {
        push_code( "movq " + ope_string( rbp ) + ", " 
                           + ope_string( rsp ) + " # Restore stackpointer" );
        push_code( "pop %rbp" );
    }

    private String getReg( Register reg ) { 
        switch (reg) {
            case RAX: return "rax";
            case RBX: return "rbx";
            case RCX: return "rcx";
            case RBP: return "rbp";
            case RSP: return "rsp";
            case RDI: return "rdi";
            case RDX: return "rdx";
            case RSI: return "rsi";
            default: return "unknown " + reg;
        }
    }

    private String ope_string( Operand ope ) {
        String out = "";
        switch ( ope.addressing_mode ) {
            case DIRECT_MEMORY: {
                System.out.println( ope );
                System.out.println( "Need implement" );
                System.exit(-1);
            } break;
            case DIRECT_OFFSET: {
                if ( ope.offset > 0 )
                    out += ope.offset * 8;
                out += "(%" + getReg( ope.get_reg() ) + ")";
            } break;
            case INDIRECT_MEMORY: {
                System.out.println( ope );
                System.out.println( "Need implement" );
                System.exit(-1);
            } break;
            case REGISER: {
                out += "%" + getReg(ope.get_reg() );
            } break;
            case IMMEDIATE: {
                out += "$";
                switch (ope.kind) {
                    case BUFFER: {
                        out += ope.get_string();
                    } break;
                    case DOUBLE: {
                        out += ope.get_double();
                    } break;
                    case INT: {
                        out += ope.get_int();
                    } break;
                    case STRING: {
                        out += ope.get_string();
                    } break;
                }
            } break;
        }
        return out;
    }
}
