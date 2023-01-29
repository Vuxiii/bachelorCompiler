package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

public class Instruction {

    public final Opcode opcode;
    public final Arguments args;

    public final Comment comment;

    public Instruction( Opcode opcode, Arguments arguments ) {
        this( opcode, arguments, new Comment("") );
    }

    public Instruction( Opcode opcode, Arguments arguments, Comment comment ) {
        this.opcode = opcode;
        this.args = arguments;
        this.comment = comment;
    }
    
    public String toString() {
        String start = opcode.toString();
        while ( start.length() < 20 ) start += " ";
        String middle = args.toString();
        while ( middle.length() < 20) middle += " ";
        
        return start + middle + comment.toString();
    }

}
