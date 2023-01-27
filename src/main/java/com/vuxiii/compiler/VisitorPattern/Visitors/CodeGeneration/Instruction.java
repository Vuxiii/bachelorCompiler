package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

public class Instruction {

    public final Opcode opcode;
    public final OpcodeArgument arguements;

    public final Comment comment;

    public Instruction( Opcode opcode, OpcodeArgument arguments ) {
        this( opcode, arguments, new Comment("") );
    }

    public Instruction( Opcode opcode, OpcodeArgument arguments, Comment comment ) {
        this.opcode = opcode;
        this.arguements = arguments;
        this.comment = comment;
    }
    
    public String toString() {
        return opcode + "\t" + arguements + " \t" + comment;
    }

}
