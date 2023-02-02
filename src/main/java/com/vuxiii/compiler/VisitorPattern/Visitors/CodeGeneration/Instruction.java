package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.Optional;

public class Instruction {

    public final Opcode opcode;
    public final Optional<Arguments> args;

    public final Optional<Comment> comment;

    public Instruction( Opcode opcode, Arguments arguments ) {
        this( opcode, arguments, new Comment("") );
    }

    public Instruction( Opcode opcode ) {
        this.opcode = opcode;
        args = Optional.empty();
        comment = Optional.empty();
    }

    public Instruction( Opcode opcode, Arguments arguments, Comment comment ) {
        this.opcode = opcode;
        this.args = Optional.of( arguments );
        this.comment = Optional.of( comment );
    }
    
    public String toString() {
        String start = opcode.toString();
        String middle = "";
        if ( args.isPresent() ) {
            while ( start.length() < 20 ) start += " ";
            middle = args.get().toString();
        }
        if ( comment.isPresent() ) {
            while ( middle.length() < 20) middle += " ";
            return start + middle + comment.get().toString();
        }
        return start + middle;
    }

}
