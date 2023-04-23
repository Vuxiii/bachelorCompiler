package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vuxiii.compiler.Error.Error;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;

public class Arguments {
    
    public List<Operand> operands;

    // public Optional<Operand> operand_1 = Optional.empty();
    // public Optional<Operand> operand_2 = Optional.empty();
    // public Optional<Operand> target = Optional.empty();
    
    public ArgumentKind kind;
    
    public Arguments( List<Operand> operands ) {
        this.operands = operands;
    }

    private Arguments() {}

    public static Arguments from_label( String label ) {
        Arguments arg = new Arguments();
        arg.operands = new ArrayList<>();

        arg.operands.add( new Operand( label, AddressingMode.LABEL ) );
        arg.kind = ArgumentKind.LABEL;
        return arg;
    }


    public static Arguments from_int( int num_int ) {
        Arguments arg = new Arguments();
        arg.operands = new ArrayList<>();

        arg.operands.add( new Operand( num_int, AddressingMode.IMMEDIATE ) );
        arg.kind = ArgumentKind.ONE_LITERAL;
        return arg;
    }

    public static Arguments from_long( long num_int ) {
        Arguments arg = new Arguments();
        arg.operands = new ArrayList<>();

        arg.operands.add( new Operand( num_int, AddressingMode.IMMEDIATE ) );
        arg.kind = ArgumentKind.ONE_LITERAL;
        return arg;
    }

    public static Arguments from_bool( boolean bool ) {
        Arguments arg = new Arguments();
        arg.operands = new ArrayList<>();

        arg.operands.add( new Operand( bool, AddressingMode.IMMEDIATE ) );
        arg.kind = ArgumentKind.ONE_LITERAL;
        return arg;
    }

    public static Arguments from_double( double num_double ) {
        Arguments arg = new Arguments();
        arg.operands = new ArrayList<>();

        arg.operands.add( new Operand( num_double, AddressingMode.IMMEDIATE ) );
        arg.kind = ArgumentKind.ONE_LITERAL;
        return arg;
    }

    public static Arguments from_register( Register reg ) {
        Arguments arg = new Arguments();
        arg.operands = new ArrayList<>();

        arg.operands.add( new Operand( reg, AddressingMode.REGISER ) );
        arg.kind = ArgumentKind.ONE_REG;
        return arg;
    }

    public String toString() {
        String out = "";
        for ( Operand ope : operands )
            out += ope.toString() + ", ";
    
        return out.substring(0, out.length()-2);
    }

    public static Arguments from_literal(LexLiteral lit) {
        switch (lit.literal_type) {
            case INT: {
                return Arguments.from_int( Integer.parseInt( lit.val ) );
            }
            case DOUBLE: {
                return Arguments.from_double( Double.parseDouble( lit.val ) );
            } 
            case BOOL: {
                return Arguments.from_bool( Boolean.parseBoolean( lit.val ) );
            } 
            default: {
                System.out.println( new Error( "Type Error (Arguments.java)", "Unsupported type: " + lit.literal_type + ". Exiting!" ) );
                System.exit(-1);
            } break;
        }
        return null;
    }

    public static Arguments comment( String comment ) {
        Arguments arg = new Arguments( List.of( Operand.from_string(comment) ) );
        return arg;        
    }

    // public static Arguments compare( Register rax, int i ) {
    //     Arguments arg = new Arguments();

    //     arg.operand_1 = Optional.of( new Operand( rax, AddressingMode.REGISER ) );
    //     arg.operand_2 = Optional.of( new Operand( i, AddressingMode.IMMEDIATE ) );
    //     arg.kind = ArgumentKind.REG_LITERAL;
    //     return arg;
    // }
}
