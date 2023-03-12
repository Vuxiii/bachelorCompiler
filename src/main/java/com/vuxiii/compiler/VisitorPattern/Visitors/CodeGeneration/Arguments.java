package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.Optional;

import com.vuxiii.compiler.Error.Error;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;

public class Arguments {
    
    public Optional<Operand> operand_1 = Optional.empty();
    public Optional<Operand> operand_2 = Optional.empty();
    public Optional<Operand> target = Optional.empty();
    
    public ArgumentKind kind;
    
    private Arguments() {}

    public static Arguments from_label( String label ) {
        Arguments arg = new Arguments();

        arg.operand_1 = Optional.of( new Operand( label, AddressingMode.IMMEDIATE ) );
        arg.kind = ArgumentKind.LABEL;
        return arg;
    }


    public static Arguments from_int( int num_int ) {
        Arguments arg = new Arguments();

        arg.operand_1 = Optional.of( new Operand( num_int, AddressingMode.IMMEDIATE ) );
        arg.kind = ArgumentKind.ONE_LITERAL;
        return arg;
    }

    public static Arguments from_bool( boolean bool ) {
        Arguments arg = new Arguments();

        arg.operand_1 = Optional.of( new Operand( bool, AddressingMode.IMMEDIATE ) );
        arg.kind = ArgumentKind.ONE_LITERAL;
        return arg;
    }

    public static Arguments from_double( double num_double ) {
        Arguments arg = new Arguments();

        arg.operand_1 = Optional.of( new Operand( num_double, AddressingMode.IMMEDIATE ) );
        arg.kind = ArgumentKind.ONE_LITERAL;
        return arg;
    }

    public static Arguments from_register( Register reg ) {
        Arguments arg = new Arguments();

        arg.operand_1 = Optional.of( new Operand( reg, AddressingMode.REGISER ) );
        arg.kind = ArgumentKind.ONE_REG;
        return arg;
    }

    public static Arguments from_operand( Operand operand ) {
        Arguments arg = new Arguments();
        arg.operand_1 = Optional.of( operand );
        arg.kind = ArgumentKind.ONE_REG;
        return arg;
    }

    public Arguments( Operand operand_1, Operand operand_2, Operand target ) {
        this.operand_1 = Optional.of(operand_1);
        this.operand_2 = Optional.of(operand_2);
        this.target = Optional.of(target);
        kind = ArgumentKind.REG_REG_TARGET;
    }

    public Arguments( Operand operand_1, Operand target ) {
        this.operand_1 = Optional.of(operand_1);
        this.target = Optional.of(target);
        kind = ArgumentKind.REG_TARGET;
    }


    public String toString() {
        String out = "";
        if ( target.isPresent() ) {
            out += target.get().toString() + " ";
            if (operand_1.isPresent()) {
                out += "= ";
            }
        }
        if ( operand_1.isPresent() )
            out += operand_1.get().toString() + " ";
        if ( operand_2.isPresent() )
            out += operand_2.get().toString();
    
        return out;
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

    public static Arguments compare( Register rax, int i ) {
        Arguments arg = new Arguments();

        arg.operand_1 = Optional.of( new Operand( rax, AddressingMode.REGISER ) );
        arg.operand_2 = Optional.of( new Operand( i, AddressingMode.IMMEDIATE ) );
        arg.kind = ArgumentKind.REG_LITERAL;
        return arg;
    }
}
