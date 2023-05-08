package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.List;

public class OffsetLogic {
    public List<Integer> offsets = new ArrayList<>();
    public List<Boolean> onHeap = new ArrayList<>();
   
    public OffsetLogic( ) {}

    public String toString() {
        return "Offsets: " + offsets.toString();
    }

    public void add( Integer offset, boolean onHeap ) {
        offsets.add(offset);
        this.onHeap.add(onHeap);
    }

    public int size() {
        return offsets.size();
    }
}
