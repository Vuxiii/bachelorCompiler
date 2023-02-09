package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.Optional;

public class Operand {
    private Optional<String> str = Optional.empty();
    private Optional<Register> reg = Optional.empty();
    private Optional<Integer> num_int = Optional.empty();
    private Optional<Double> num_double = Optional.empty();

    public AddressingMode addressing_mode;


    public Operand( String str, AddressingMode mode ) {
        this.str = Optional.of( str );
        this.addressing_mode = mode;
    }

    public Operand( Register reg, AddressingMode mode ) {
        this.reg = Optional.of( reg );
        this.addressing_mode = mode;
    }

    public Operand( int num_integer, AddressingMode mode ) {
        this.num_int = Optional.of(num_integer);
        this.addressing_mode = mode;
    }

    public Operand( double num_double, AddressingMode mode ) {
        this.num_double = Optional.of(num_double);
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
