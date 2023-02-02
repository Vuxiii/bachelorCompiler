package com.vuxiii.compiler.InternalInterpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;

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

    public void run() {
        for ( Instruction instruction : instructions ) {
            
            switch (instruction.opcode) {
                case ADD: {
                    register[instruction.args.get().target.i] = register[instruction.args.get().src_1.i] + register[instruction.args.get().src_2.i];
                } break;
                case MINUS: {
                    register[instruction.args.get().target.i] = register[instruction.args.get().src_1.i] - register[instruction.args.get().src_2.i];
                    
                } break;
                case MULT: {
                    register[instruction.args.get().target.i] = register[instruction.args.get().src_1.i] * register[instruction.args.get().src_2.i];
                    
                } break;
                case DIV_INTEGER: {
                    register[instruction.args.get().target.i] = register[instruction.args.get().src_1.i] / register[instruction.args.get().src_2.i];
                    
                } break;


                case LOAD_VARIABLE: {
                    register[instruction.args.get().target.i] = memory.get( instruction.args.get().variable );
                } break;

                case STORE_VARIABLE: {
                    memory.put( instruction.args.get().variable, register[instruction.args.get().src_1.i] );
                } break;
                case LABEL: {

                } break;
                
                case CALL: {

                } break;
                // case MOVE: {

                // } break;
                
                case PUSH: {
                    if ( instruction.args.get().kind == 2 )
                        stack.push( Integer.parseInt(((LexLiteral)instruction.args.get().value).val) );
                    else
                        stack.push( register[instruction.args.get().src_1.i] );
                } break;
                
                case POP: {
                    register[instruction.args.get().src_1.i] = stack.pop();
                } break;
                
                case PRINT: {
                    System.out.println( "\u001B[35m" + register[instruction.args.get().src_1.i] + "\u001B[0m" );
                } break;
                default: {
                    System.out.println( "\u001B[41m\u001B[37m--[[ Interpreter Error ]]--\u001B[0m\nMissing implementation for opcode " + instruction.opcode + "\nExiting!");
                    System.exit(-1);
                } break;
            }
        }
    }
}
