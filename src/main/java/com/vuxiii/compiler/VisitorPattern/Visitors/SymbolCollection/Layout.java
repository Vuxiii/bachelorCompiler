package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vuxiii.compiler.Parser.Nodes.Declaration;

public class Layout {

    static List<Layout> all_heap_layouts = new ArrayList<>();
    static List<Layout> all_stack_layouts = new ArrayList<>();

    Map<String, Long> offsets = new HashMap<>();
    List<Long> bitfields = new ArrayList<>();

    List<Long> pointer_pos = new ArrayList<>();

    public long num_of_fields;
    public String name;


    private Layout() {}

    public static Layout heap( String name ) {
        Layout h = new Layout();
        h.name = name;
        h.num_of_fields = 696969696;

        all_heap_layouts.add( h );
        return h;
    }

    public static Layout stack( String name ) {
        Layout s = new Layout();
        s.name = name;
        s.num_of_fields = 696969696;

        all_stack_layouts.add( s );
        return s;
    }

    public void register( String field, long offset ) {
        offsets.put( field, offset );
    }









    public static String get_layout(Declaration decl) {
        return ""; //TODO! Implement me
    }

    public static List<Long> getLayout( String string ) {
        
        return List.of( 2l );
    }

    public static List<Layout> all_stack_layouts() {
        return all_stack_layouts;
    }

    public static List<Layout> all_heap_layouts() {
        return all_heap_layouts;
    }

    public List<Long> bitfields() {
        return bitfields;
    }

    public void pointer_at(long offset) {
        int i = 0;
        while ( offset > 64 ) {
            i++;
            offset -= 64;
        } //TODO! The first bit needs to be set correctly! 1 if there is a next field.

        while ( i >= bitfields.size() )
            bitfields.add( 0L );


        long field = bitfields.get(i);

        field |= 1 << offset;
        bitfields.set( i, field );
        pointer_pos.add(offset);
        System.out.println( bitfields );
    }

    public String toString() {
        String s = "";
        s += "Layout for: " + name;

        s += "\nNum of Fields: " + num_of_fields;

        s += "\nBitfields:\n";

        for ( long l : bitfields ) {
            s += "\t" + l + "\n";
        }
        s += "\nPointerPos:\n";

        for ( long l : pointer_pos ) {
            s += "\t" + l + "\n";
        }


        return s;

    }
    
}
