package com.vuxiii.compiler.InternalInterpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexInt;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;

public class Interpreter {
    public final List<Instruction> instructions;
    
    public final Stack<Integer> stack;

    public final int[] register;

    public final Map<String, Integer> memory;

    public final int rax = 0;
    public final int rbx = 0;
    public final int rcx = 0;

    public Interpreter( List<Instruction> instructions ) {
        this.instructions = instructions;
        stack = new Stack<>();

        register = new int[3];

        memory = new HashMap<>();
    }

    public void run() {
        for ( Instruction instruction : instructions ) {
            
            switch (instruction.opcode) {
                case ADD: {
                    register[instruction.arguments.target.i] = register[instruction.arguments.left.i] + register[instruction.arguments.right.i];
                } break;
                case MINUS: {
                    register[instruction.arguments.target.i] = register[instruction.arguments.left.i] - register[instruction.arguments.right.i];
                    
                } break;
                case MULT: {
                    register[instruction.arguments.target.i] = register[instruction.arguments.left.i] * register[instruction.arguments.right.i];
                    
                } break;
                case DIV_INTEGER: {
                    register[instruction.arguments.target.i] = register[instruction.arguments.left.i] / register[instruction.arguments.right.i];
                    
                } break;


                case LOAD_VARIABLE: {
                    register[instruction.arguments.register.i] = memory.get( instruction.arguments.variable );
                } break;

                case STORE_VARIABLE: {
                    memory.put( instruction.arguments.variable, register[instruction.arguments.register.i] );
                } break;
                
                
                // case MOVE: {

                // } break;
                
                case PUSH: {
                    if ( instruction.arguments.kind == 2 )
                        stack.push( ((LexInt)instruction.arguments.value).val );
                    else
                        stack.push( register[instruction.arguments.register.i] );
                } break;
                
                case POP: {
                    register[instruction.arguments.register.i] = stack.pop();
                } break;
                
                case PRINT: {
                    System.out.println( register[instruction.arguments.register.i] );
                } break;
                default: {
                    System.out.println( "--[[ Interpreter Error ]]--\nMissing implementation for opcode " + instruction.opcode + "\nExiting!");
                    System.exit(-1);
                } break;
            }
        }
    }
}
