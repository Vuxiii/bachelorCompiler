package com.vuxiii.compiler.CodeEmit;

import java.util.List;

import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.Instruction;

/**
 * Stack machine!
 */
public class X86Emitter {
    
    private String asm = "";

    private List<Instruction> instructions;

    public X86Emitter( List<Instruction> instructions ) {
        this.instructions = instructions;
    }

    private void _push( String s ) {
        asm += s;
    }

    public String run() {


        for ( Instruction instruction : instructions ) {

            switch (instruction.opcode) {
                case ADD: {
                    _add( "addq" );
                } break;
                case MINUS: {
                    
                    
                } break;
                case MULT: {
                    
                    
                } break;
                case DIV_INTEGER: {
                    
                    
                } break;


                case LOAD_VARIABLE: {
                    
                } break;

                case STORE_VARIABLE: {
                    
                } break;
                
                
                case PUSH: {
                    
                } break;
                
                case POP: {
                    
                } break;
                
                case PRINT: {
                    
                } break;
                default: {
                    System.out.println( "\u001B[41m\u001B[37m--[[ Interpreter Error ]]--\u001B[0m\nMissing implementation for opcode " + instruction.opcode + "\nExiting!");
                    System.exit(-1);
                } break;
            }

        }

        return asm;
    }
}
