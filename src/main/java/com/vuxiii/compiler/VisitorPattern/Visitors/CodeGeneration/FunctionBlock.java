package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.LinkedList;
import java.util.List;

import com.vuxiii.compiler.Parser.Nodes.Assignment;

public class FunctionBlock {
    public String function_label;

    Assignment function;

    public List<Instruction> instructions;

    public FunctionBlock( String name, Assignment func ) {
        this.function_label = "function_" + name + ":";
        this.function = func;
        instructions = new LinkedList<>();
    }

    public void push( Instruction instruction ) {
        instructions.add( instruction );
    }

    public void push_comment( String comment ) {
        instructions.add( new Instruction( Opcode.COMMENT, Arguments.comment(comment)) );
    }

} 
