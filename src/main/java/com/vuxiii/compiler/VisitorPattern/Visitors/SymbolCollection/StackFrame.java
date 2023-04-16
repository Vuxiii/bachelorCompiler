package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;

public class StackFrame {
    
    public static Set<StackFrame> stack_frames = new HashSet<>();

    public List<Long> pointer_offsets = new ArrayList<>();

    private long current_offset = 0L;

    public void register_variable( Declaration decl ) {
        current_offset++;

        if ( decl.kind != DeclarationKind.HEAP ) return;

        pointer_offsets.add( current_offset );
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
