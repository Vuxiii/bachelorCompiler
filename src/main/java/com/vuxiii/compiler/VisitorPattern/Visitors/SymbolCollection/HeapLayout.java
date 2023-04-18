package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.Parser.Nodes.Types.RecordType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;

public class HeapLayout {
    public static Map<ASTNode, HeapLayout> heap_layouts = new HashMap<>();
    private static long id = 0;

    public List<Long> pointer_offsets = new ArrayList<>();
    public Map<String, Long> var_offset = new HashMap<>();


    private long current_offset = 0L;

    public final String name;


    public HeapLayout( RecordType record ) {

        for ( Field field : record.fields.fields ) {
            current_offset++;
            if ( field.field.kind == DeclarationKind.HEAP ) {
                pointer_offsets.add( current_offset );
            }
            var_offset.put( field.field.id.name, current_offset+1 );
        }

        if ( heap_layouts.containsKey( record ) ) {
            record.heap_layout = heap_layouts.get(record);
        } else {
            record.heap_layout = this;
            heap_layouts.put( record, this );
            id++;
        }
        System.out.println( var_offset );
        name = "heap" + id;
    }

    public HeapLayout( Declaration decl ) {
        current_offset++;

        pointer_offsets.add( current_offset );


        if ( heap_layouts.containsKey( decl ) ) {
            decl.heap_layout = heap_layouts.get(decl);
        } else {
            decl.heap_layout = this;
            heap_layouts.put( decl, this );
            id++;
        }
        System.out.println( var_offset );
        name = "heap" + id;

    }

    public long get_heap_offset( String field ) {
        return var_offset.getOrDefault(field, 2L);
    }

    @Override
    public int hashCode() {
        int out = 0;
        int i = 0;
        for ( Long l : pointer_offsets )
            out += l * Math.pow(37, i++);
        return out;
    }

    @Override
    public boolean equals( Object other ) {
        if ( other == null ) return false;
        if ( !(other instanceof HeapLayout) ) return false;
        HeapLayout o = (HeapLayout)other;
        if ( pointer_offsets.size() != o.pointer_offsets.size() ) return false;
        pointer_offsets.sort(Comparator.naturalOrder());
        o.pointer_offsets.sort(Comparator.naturalOrder());
        for ( int i = 0; i < pointer_offsets.size(); ++i ) {
            if ( pointer_offsets.get(i) != o.pointer_offsets.get(i) ) return false;
        }
        return true;
    }

    private Long set_bit( Long field, long offset ) {
        return field | (1 << offset);
    }

    public List<Long> bitfields() {
        List<Long> fields = new ArrayList<>();

        Long field = 0L;
        pointer_offsets.sort(Comparator.naturalOrder());

        for ( Long offset : pointer_offsets ) {
            field = set_bit(field, offset);

            if ( offset / 64 != 0 ) {
                field = set_bit(field, 0);
                fields.add( field );
                field = set_bit(0L, offset);
            }
        }
        if ( !fields.contains(field) )
            fields.add(field);
        
        return fields;
    }
}
