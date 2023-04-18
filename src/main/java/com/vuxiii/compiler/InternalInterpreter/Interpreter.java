package com.vuxiii.compiler.InternalInterpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.ArgumentKind;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Operand;

public class Interpreter {
    public final List<Instruction> instructions;
    
    public final Stack<Integer> stack;

    public final int[] register;

    public final Map<String, Integer> memory;

    // public final Map<String, > functions;

    public final int rax = 0;
    public final int rbx = 0;
    public final int rcx = 0;
    public final int rbp = 0;
    public final int rsp = 0;

    public Interpreter( List<Instruction> instructions ) {
        this.instructions = instructions;
        stack = new Stack<>();

        register = new int[5];

        memory = new HashMap<>();
    }

    // public void run() {
    //     for ( Instruction instruction : instructions ) {
            
    //         switch (instruction.opcode) {
    //             case ADD: {
    //                 Operand target = instruction.args.get().target.get();
    //                 Operand src_1 = instruction.args.get().operand_1.get();
    //                 Operand src_2 = instruction.args.get().operand_2.get();
    //                 register[target.get_reg().i] = register[src_1.get_reg().i] + register[src_2.get_reg().i];
    //             } break;
    //             case MINUS: {
    //                 Operand target = instruction.args.get().target.get();
    //                 Operand src_1 = instruction.args.get().operand_1.get();
    //                 Operand src_2 = instruction.args.get().operand_2.get();
    //                 System.out.println( target );
    //                 System.out.println( src_1 );
    //                 System.out.println( src_2 ); //TODO: This doesn't check if some of the srcs might be integers or doubles.
    //                 register[target.get_reg().i] = register[src_1.get_reg().i] - register[src_2.get_reg().i];
    //             } break;
    //             case MULT: {
    //                 Operand target = instruction.args.get().target.get();
    //                 Operand src_1 = instruction.args.get().operand_1.get();
    //                 Operand src_2 = instruction.args.get().operand_2.get();
    //                 register[target.get_reg().i] = register[src_1.get_reg().i] * register[src_2.get_reg().i];
    //             } break;
    //             case DIV_INTEGER: {
    //                 Operand target = instruction.args.get().target.get();
    //                 Operand src_1 = instruction.args.get().operand_1.get();
    //                 Operand src_2 = instruction.args.get().operand_2.get();
    //                 register[target.get_reg().i] = register[src_1.get_reg().i] / register[src_2.get_reg().i];
    //             } break;


    //             case LOAD_VARIABLE: {
    //                 Operand target = instruction.args.get().target.get();
    //                 Operand src_1 = instruction.args.get().operand_1.get();
    //                 register[target.get_reg().i] = memory.get( src_1.get_string() );
    //             } break;

    //             case STORE_VARIABLE: {
    //                 Operand target = instruction.args.get().target.get();
    //                 Operand src_1 = instruction.args.get().operand_1.get();
    //                 memory.put( target.get_string(), register[src_1.get_reg().i] );
    //             } break;
    //             case LABEL: {

    //             } break;
                
    //             case CALL: {

    //             } break;
    //             // case MOVE: {

    //             // } break;
                
    //             case PUSH: {
                    
    //                 if ( instruction.args.get().kind == ArgumentKind.ONE_LITERAL )
    //                     stack.push( instruction.args.get().operand_1.get().get_int() );
    //                 else // ONE_REG
    //                     stack.push( register[instruction.args.get().operand_1.get().get_reg().i] );
    //             } break;
                
    //             case POP: {
    //                 register[instruction.args.get().operand_1.get().get_reg().i] = stack.pop();
    //             } break;
                
    //             case PRINT: {
    //                 System.out.println( "\u001B[35m" + register[instruction.args.get().operand_1.get().get_reg().i] + "\u001B[0m" );
    //             } break;
    //             default: {
    //                 System.out.println( "\u001B[41m\u001B[37m--[[ Interpreter Error ]]--\u001B[0m\nMissing implementation for opcode " + instruction.opcode + "\nExiting!");
    //                 System.exit(-1);
    //             } break;
    //         }
    //     }
    // }
}
