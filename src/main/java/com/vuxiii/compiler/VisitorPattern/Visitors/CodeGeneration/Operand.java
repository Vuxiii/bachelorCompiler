package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.Optional;

public class Operand {
    private Optional<String> str = Optional.empty();
    private Optional<Register> reg = Optional.empty();
    private Optional<Integer> num_int = Optional.empty();
    private Optional<Double> num_double = Optional.empty();
    private Optional<Boolean> num_bool = Optional.empty();
    private Optional<Long> num_long = Optional.empty();

    public AddressingMode addressing_mode;
    public OperandKind kind;
    public int offset;

    public static Operand from_register( Register reg, AddressingMode mode ) {
        return new Operand( reg, mode );
    }

    public static Operand from_string( String str ) {
        return new Operand( str, AddressingMode.IMMEDIATE);
    }

    public static Operand from_int( int num_int ) {
        return new Operand( num_int, AddressingMode.IMMEDIATE );
    }

    public static Operand from_long( long num_long ) {
        return new Operand( num_long, AddressingMode.IMMEDIATE );
    }

    public static Operand from_double( double num_double ) {
        return new Operand( num_double, AddressingMode.IMMEDIATE );
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

    public Operand( long num_long, AddressingMode mode ) {
        this.num_long = Optional.of(num_long);
        this.kind = OperandKind.LONG;
        this.addressing_mode = mode;
    }

    public Operand( boolean bool, AddressingMode mode ) {
        this.num_bool = Optional.of(bool);
        this.kind = OperandKind.BOOL;
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

    public boolean get_bool() {
        return num_bool.get();
    }

    public long get_long() {
        return num_long.get();
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
        } else if ( num_bool.isPresent() ) {
            return "" + num_bool.get();
        }
        return "unknown operand (Not implemented!)";
    }

}
