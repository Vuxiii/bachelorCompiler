package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vuxiii.compiler.Parser.Nodes.Declaration;

public class Layout {

    static List<Layout> all_layouts = new ArrayList<>();

    Map<String, Long> offsets = new HashMap<>();
    List<Long> bitfields = new ArrayList<>();

    List<Long> pointer_pos = new ArrayList<>();

    public long num_of_fields;
    public String name;


    public Layout( String name ) {
        this.name = name;
        num_of_fields = 696969696;

        all_layouts.add( this );
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

    public static List<Layout> get_all_layouts() {
        return all_layouts;
    }

    public List<Long> bitfields() {
        return bitfields;
    }

    public void pointer_at(long offset) {
        pointer_pos.add(offset);
    }
    
}
