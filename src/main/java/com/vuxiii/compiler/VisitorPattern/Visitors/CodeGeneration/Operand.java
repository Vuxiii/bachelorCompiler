package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.Optional;

public class Operand {
    private Optional<String> str = Optional.empty();
    private Optional<Register> reg = Optional.empty();
    private Optional<Integer> num_int = Optional.empty();
    private Optional<Double> num_double = Optional.empty();

    public AddressingMode addressing_mode;
    public OperandKind kind;
    public int offset;

    public static Operand from_register( Register reg, AddressingMode mode ) {
        return new Operand( reg, mode );
    }

    public static Operand from_string( String str, AddressingMode mode ) {
        return new Operand( str, mode );
    }

    public static Operand from_int( int num_int, AddressingMode mode ) {
        return new Operand( num_int, mode );
    }

    public static Operand from_double( double num_double, AddressingMode mode ) {
        return new Operand( num_double, mode );
    }

    public Operand( String str, AddressingMode mode ) {
        this.str = Optional.of( str );
        this.kind = OperandKind.STRING;
        this.addressing_mode = mode;
    }

    public Operand( Register reg, AddressingMode mode ) {
        this.reg = Optional.of( reg );
        // this.kind = OperandKind.REGISTER;
        this.addressing_mode = mode;
    }

    public Operand( int num_integer, AddressingMode mode ) {
        this.num_int = Optional.of(num_integer);
        this.kind = OperandKind.INT;
        this.addressing_mode = mode;
    }

    public Operand( double num_double, AddressingMode mode ) {
        this.num_double = Optional.of(num_double);
        this.kind = OperandKind.DOUBLE;
        this.addressing_mode = mode;
    }

    public Register get_reg() {
        return reg.get();
    }

    public String get_string() {
        return str.get();
    }

    public int get_int() {
        return num_int.get();
    }

    public double get_double() {
        return num_double.get();
    }

    public String toString() {
        if ( str.isPresent() ) {
            return str.get();
        } else if ( reg.isPresent() ) {
            return "" + reg.get();
        } else if ( num_int.isPresent() ) {
            return "" + num_int.get();
        } else if ( num_double.isPresent() ) {
            return "" + num_double.get();
        }
        return "unknown operand";
    }

}
