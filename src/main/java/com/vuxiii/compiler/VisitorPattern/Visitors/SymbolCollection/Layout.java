package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vuxiii.compiler.Parser.Nodes.Types.RecordType;

public class Layout {

    public static Map<RecordType, Layout> all_heap_layouts = new HashMap<>();
    public static Map<RecordType, Layout> all_stack_layouts = new HashMap<>();

    Map<String, Long> offsets = new HashMap<>();
    List<Long> bitfields = new ArrayList<>();

    List<Long> pointer_pos = new ArrayList<>();

    public long num_of_fields;
    public String name;


    private Layout() {}

    public static Layout heap( RecordType rec ) {
        Layout h = new Layout();
        h.name = rec.identifier.name;
        h.num_of_fields = 696969696;

        all_heap_layouts.put( rec, h );
        return h;
    }

    public static Layout stack( RecordType rec ) {
        Layout s = new Layout();
        s.name = rec.identifier.name;
        s.num_of_fields = 696969696;

        all_stack_layouts.put( rec, s );
        return s;
    }

    public void register( String field, long offset ) {
        offsets.put( field, offset );
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
