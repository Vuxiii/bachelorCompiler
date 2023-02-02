package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.LinkedList;
import java.util.List;

public class FunctionBlock {
    public String function_label;

    public List<Instruction> instructions;

    public FunctionBlock( String name ) {
        this.function_label = "function_" + name + ":";
        instructions = new LinkedList<>();
    }

    public void push( Instruction instruction ) {
        instructions.add( instruction );
    }
} 
