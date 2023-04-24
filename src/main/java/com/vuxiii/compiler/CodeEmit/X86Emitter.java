package com.vuxiii.compiler.CodeEmit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vuxiii.compiler.Lexer.Tokens.PrimitiveType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Parser.Nodes.Expression;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.Root;
import com.vuxiii.compiler.Parser.Nodes.SymbolNode;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.AddressingMode;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.FunctionBlock;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Operand;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Register;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection.StringNode;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.HeapLayout;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.OffsetLogic;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Symbols;

/**
 * Stack machine!
 */
public class X86Emitter {
    
    private String asm = "";

    private List<Instruction> instructions;
    // private List<Scope> scopes;
    private Map<String, Symbols> scopes;
    Map<String, FunctionBlock> functions;

    Map<String, OffsetLogic> var_offsets;

    Map<Print, StringNode> string_buffers;

    private static Operand rsp = Operand.from_register(Register.RSP, AddressingMode.REGISER);
    private static Operand rax = Operand.from_register(Register.RAX, AddressingMode.REGISER);
    private static Operand rbx = Operand.from_register(Register.RBX, AddressingMode.REGISER);
    private static Operand rcx = Operand.from_register(Register.RCX, AddressingMode.REGISER);
    private static Operand rdi = Operand.from_register(Register.RDI, AddressingMode.REGISER);
    private static Operand rsi = Operand.from_register(Register.RSI, AddressingMode.REGISER);
    private static Operand rdx = Operand.from_register(Register.RDX , AddressingMode.REGISER);
    private static Operand rbp = Operand.from_register(Register.RBP, AddressingMode.REGISER);

    public X86Emitter( List<Instruction> instructions, Map<String, FunctionBlock> functions, Root root ) {
        this.instructions = instructions;

        scopes = root.scope_map;
        string_buffers = root.strings;

        var_offsets = new HashMap<>();

        this.functions = functions;
    }

    private void push_no_offset( String s ) {
        asm += s + "\n";
    }

    private void push_code( String s ) {
        asm += "    " + s + "\n";
    }

    private void file_header() {
        
        // push_no_offset( ".section .data" );
        push_no_offset( ".section .text" );
        push_no_offset( ".global main" );
        push_no_offset( "main:" );
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

        push_no_offset("# [ String Buffers and Substitutes ]" );

        for ( Print print_node : string_buffers.keySet() ) {
            StringNode node = string_buffers.get( print_node );
            push_no_offset( node.name + ": .ascii " + node.str_literal );
            if (  node.substitutes.size() > 0 ) {
                String subs = "";
                for ( ASTNode n : node.substitutes ) {
                    Expression exp = (Expression)n;

                    if ( exp.node instanceof LexLiteral ) {
                        LexLiteral id = (LexLiteral)exp.node;
                        if ( id.literal_type == PrimitiveType.STRING ) {
                            System.out.println( id );
                            subs += id.val.substring(1, id.val.length()-1 ) + "\\0";
                        } else {
                                  
                        } 
                    } 
                }
                push_no_offset( node.substitute_name + ": .ascii \"" + subs + '"' );
            }
            // push_no_offset( node.stop_name + ": .space " +  node.stop_indicators.size() * 8 );
        }

        push_no_offset("" );
        push_no_offset("# [ Pointers to Record Layouts ]" );

        for ( HeapLayout l : HeapLayout.heap_layouts.values() ) {

            push_no_offset(l.name + ": .space 8" );

        }
        
        push_no_offset("" );
        
        push_no_offset( ".section .text" );
        

        // Scope current_scope = scopes.get(0);


        for ( String function : functions.keySet() ) {
            FunctionBlock fb = functions.get(function);
            System.out.println( function );
            System.out.println( scopes );
            if ( scopes.containsKey( "function " + function ) )
                _run( fb.instructions, scopes.get( "function " + function ) );
            
        }

        file_header();


        setup_stackpointer();
        _run( instructions, scopes.get( "root" ) );
        
        restore_stackpointer();


        file_footer();
        return asm;
    }

    
    private void _run( List<Instruction> instructions, Symbols current_scope ) {
        
        System.out.println( current_scope.get_variables() );
        System.out.println( current_scope.variable_offsets );
        
        var_offsets = current_scope.variable_offsets;

        // for ( String var : current_scope.get_variables() ) {
        //     var_offsets.put( var, -current_scope.get_variable_offset(var).offsets.get(0) );
        // }

        // for ( String var : current_scope.get_parameters() ) {
        //     var_offsets.put( var, current_scope.get_parameter_offset(var) );
        // }

        System.out.println();

        for ( Instruction instruction : instructions ) {

            switch (instruction.opcode) {
                case ADD: {
                    Operand r1 = instruction.args.get().operands.get(0);
                    Operand r2 = instruction.args.get().operands.get(1);
                    // Operand target = instruction.args.get().operands.get(2);
                    
                    push_code( "addq " + ope_string(r1) +", " + ope_string(r2) ); //TODO: Check for double also.
                    
                    // push_code( "movq " + ope_string(r2) +", " + ope_string(target) );
                } break;
                case MINUS: {
                    Operand r1 = instruction.args.get().operands.get(0);
                    Operand r2 = instruction.args.get().operands.get(1);
                    // Operand target = instruction.args.get().operands.get(2);
                    push_code( "subq " + ope_string(r1) +", " + ope_string(r2) ); //TODO: Check for double also.

                    // push_code( "movq " + ope_string(r2) +", " + ope_string(target) );
                } break;
                case MULT: { //TODO: Add support for register * literal
                    Operand r1 = instruction.args.get().operands.get(0);
                    Operand r2 = instruction.args.get().operands.get(1);
                    Operand target = instruction.args.get().operands.get(2);
                    push_code( "movq " + ope_string(r1) + ", " + ope_string( rax ) );
                    push_code( "imulq " + ope_string(r2) );
                    push_code( "movq " + ope_string(rax) +", " + ope_string(target) );
                } break;
                case DIV_INTEGER: {
                    
                    
                } break;


                case LOAD_VARIABLE: {
                    Operand var = instruction.args.get().operands.get(0);
                    Operand target = instruction.args.get().operands.get(1);
                    
                    System.out.println( "Trying to load " + var.get_string() );

                    OffsetLogic offsets = var_offsets.get( var.get_string() );

                    push_code ( "" );
                    push_code ("# [[ Loading variable " + var + " ]] " );
                    for ( int i = 0; i < offsets.size(); ++i ) {
                        push_code("# [[ offset is " + offsets.offsets.get(i) + " ]] " );
                        if ( i == 0 )
                            push_code( "movq " + (offsets.offsets.get(i)*8) + "(%rbp), " + ope_string(target) ); // What offset is the variable stored at
                        else
                            push_code( "movq " + (offsets.offsets.get(i)*8) + "(" + ope_string(target) + "), " + ope_string(target) ); // What offset is the variable stored at

                        push_code( "" );
                    }

                    
                    
                } break;

                case STORE_VARIABLE: {
                    String var = instruction.args.get().operands.get(0).get_string();
                    Operand src_1 = instruction.args.get().operands.get(1);
                    System.out.println( var );
                    System.out.println( var_offsets );
                    OffsetLogic offsets = var_offsets.get( var );

                    push_code ( "" );
                    push_code ("# [[ Storing variable " + var + " " + offsets.offsets  + " ]] " );
                    for ( int i = 0; i < offsets.size(); ++i ) {
                        push_code ("# [[ offset is " + offsets.offsets.get(i) + " ]] " );
                        if ( i == offsets.size()-1 && i == 0 ) 
                            push_code( "movq " + ope_string(src_1) + ", " + (offsets.offsets.get(i)*8) + "(" + ope_string(rbp) + ")" );
                        else if ( i == offsets.size()-1 )
                            push_code( "movq " + ope_string(src_1) + ", " + (offsets.offsets.get(i)*8) + "(" + ope_string(rbx) + ")" );
                        else if ( i == 0 )
                            push_code( "movq " + (offsets.offsets.get(i)*8) + "(" + ope_string(rbp) + "), " + ope_string(rbx) );
                        else
                            push_code( "movq " + (offsets.offsets.get(i)*8) + "(" + ope_string(rbx) + "), " + ope_string(rbx) );
                    }
                    
                    push_code ( "" );

                } break;

                case STORE_POINTER: {
                    String var = instruction.args.get().operands.get(0).get_string();
                    Operand src_1 = instruction.args.get().operands.get(1);
                    System.out.println( var );
                    System.out.println( var_offsets );
                    OffsetLogic offsets = var_offsets.get( var );

                    push_code ( "" );
                    push_code ("# [[ Storing pointer " + var + " " + offsets.offsets + " ]] " );
                    for ( int i = 0; i < offsets.size()-1; ++i ) {
                        push_code ("# [[ offset is " + offsets.offsets.get(i) + " ]] " );
                        if ( i == offsets.size()-2 && i == 0 ) 
                            push_code( "movq " + ope_string(src_1) + ", " + (offsets.offsets.get(i)*8) + "(" + ope_string(rbp) + ")" );
                        else if ( i == offsets.size()-2 )
                            push_code( "movq " + ope_string(src_1) + ", " + (offsets.offsets.get(i)*8) + "(" + ope_string(rbx) + ")" );
                        else if ( i == 0 )
                            push_code( "movq " + (offsets.offsets.get(i)*8) + "(" + ope_string(rbp) + "), " + ope_string(rbx) );
                        else
                            push_code( "movq " + (offsets.offsets.get(i)*8) + "(" + ope_string(rbx) + "), " + ope_string(rbx) );
                    }
                    
                    push_code ( "" );
                } break;
                
                
                case PUSH: {
                    push_code( "pushq " + ope_string(instruction.args.get().operands.get(0) ) );

                } break;
                case POP: {
                    push_code( "popq " + ope_string(instruction.args.get().operands.get(0)) );
                } break;
                
                case LABEL: {
                    push_no_offset( instruction.args.get().operands.get(0).get_string() + ":" );
                } break;

                case SETUP_STACK: {
                    setup_stackpointer();
                    push_code( "subq $" + (8*current_scope.get_variables().size()) + ", %rsp" );

                } break;

                case RESTORE_STACK: {
                    restore_stackpointer();
                } break;

                case RETURN: {
                    push_code( "movq %rbp, %rsp" );
                    push_code( "popq %rbp" );
                    push_code( "retq" );
                    push_code( "" );
                } break;

                case CALL: {
                    String op = ope_string(instruction.args.get().operands.get(0));
                    push_code( "callq " + op + " # " + instruction.args.get().operands.get(0).addressing_mode );
                } break;
                case MOVE: {
                    Operand ope_1 = instruction.args.get().operands.get(0);
                    Operand ope_2 = instruction.args.get().operands.get(1);
                    String fst = ope_string( ope_1 );
                    String snd = ope_string( ope_2 );

                    push_code( "movq " + fst + ", " + snd );
                } break;
                case LEA: {
                    Operand ope_1 = instruction.args.get().operands.get(0);
                    Operand ope_2 = instruction.args.get().operands.get(1);
                    String fst = ope_string( ope_1 );
                    String snd = ope_string( ope_2 );

                    push_code( "leaq " + fst + ", " + snd );
                } break;
                case PRINT: {                    
                    push_code( "callq print_subs" );
                } break;
                case PRINT_STRING: {
                    push_code( "callq print_string" );
                } break;
                case PRINT_NUM: {
                    push_code( "callq print_num" );
                } break;
                case COMMENT: {
                    push_no_offset( "" );
                    push_no_offset( "# " + instruction.args.get().operands.get(0).get_string() );
                    push_no_offset( "" );
                } break;
                case COMPARE: {
                    Operand left = instruction.args.get().operands.get(0);
                    Operand right = instruction.args.get().operands.get(1);

                    push_code( "cmpq " + ope_string( right ) + ", " + ope_string( left ) );

                } break;
                case JUMP_NOT_EQUAL: {
                    push_code( "jne " + instruction.args.get().operands.get(0).get_string() );
                } break;
                case JUMP_EQUAL: {
                    push_code( "je " + instruction.args.get().operands.get(0).get_string() );
                } break;
                case JUMP: {
                    push_code( "jmp " + instruction.args.get().operands.get(0).get_string() );
                } break;
                default: {
                    System.out.println( "\u001B[41m\u001B[37m--[[ Emitter Error ]]--\u001B[0m\nMissing implementation for opcode " + instruction.opcode + "\nExiting!");
                    System.exit(-1);
                } break;
            }

        }

    }


    private void setup_stackpointer() {
        push_code( "pushq " + ope_string( rbp ) );
        push_code( "movq " + ope_string( rsp ) + ", " 
                           + ope_string( rbp ) + " # Setup stackpointer" );
    }

    private void restore_stackpointer() {
        push_code( "movq " + ope_string( rbp ) + ", " 
                           + ope_string( rsp ) + " # Restore stackpointer" );
        push_code( "popq %rbp" );
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
            case FUNCTION_POINTER: {
                out += "*%" + getReg(ope.get_reg() );
            } break;
            case DIRECT_MEMORY: {
                System.out.println( ope );
                System.out.println( "Need implement" );
                System.exit(-1);
            } break;
            case DIRECT_OFFSET: {
                if ( ope.offset != 0 )
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
            case LABEL: {
                out += ope.get_string();
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
                    case BOOL: {
                        out += ope.get_bool() == true ? 1 : 0;
                    } break;
                    case LONG: {
                        out += ope.get_long();
                    } break;
                }
            } break;
        }
        return out;
        }
}
