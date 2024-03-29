package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.Parser.Nodes.Types.RecordType;
import com.vuxiii.compiler.Parser.Nodes.Types.StandardType;
import com.vuxiii.compiler.Parser.Nodes.Types.Type;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_Printer;

public class Symbols {
    public static Set<Symbols> all_layouts = new HashSet<>();
    private static long counter = 0;
    private Map<String, LexIdent> local_vars = new HashMap<>();
    private Map<String, LexIdent> parameters = new HashMap<>();
    private Map<String, Integer> parameter_offsets = new HashMap<>();
    private int current_parameter_offset = 2;
    
    public Map<String, OffsetLogic> variable_offsets = new HashMap<>();
    private int current_variable_offset = 2;

    private Set<String> is_var_heap_allocated = new HashSet<>();

    private Map<String, LexIdent> capture_vars = new HashMap<>();

    public final long id;

    public Symbols() {
        all_layouts.add( this);



        this.id = counter++;
    }

    public void add_variable( LexIdent variable, boolean is_pointer ) {
        if ( local_vars.containsKey(variable.name) ) return;
        local_vars.put( variable.name, variable );

        // if ( is_pointer ) pointer_pos.add( current_variable_offset );
        OffsetLogic offlogic = new OffsetLogic();
        int off = current_variable_offset++;
        offlogic.add( -off, false );
        variable_offsets.put( variable.name, offlogic );
    }

    //TODO! Figure out how parameters should be regeistered as pointers
    public void add_parameter( LexIdent variable, boolean is_pointer ) {
        if ( parameters.containsKey(variable.name) ) return;
        OffsetLogic offlogic = new OffsetLogic();
        offlogic.add( current_parameter_offset++, false );
        
        variable_offsets.put( variable.name, offlogic );
        
        // parameters.put( variable.name, variable );
        // parameter_offsets.put( variable.name, current_parameter_offset++ );
    }

    public void add_fields( Declaration decl ) {
        RecordType type = (RecordType)decl.type;
        // System.out.println( "=============REGISTER=============" );
        AST_Printer prin = new AST_Printer();
        decl.accept(prin);
        // System.out.println( prin.get_ascii() );
        local_vars.put( decl.id.name, decl.id );
        OffsetLogic off = new OffsetLogic();
        off.add( -current_variable_offset, false );
        off.add( 0, false );
        
        variable_offsets.put( decl.id.name, off );

        int fieldOffset = 2;

        for ( Field f : type.fields.fields ) {
            String name = decl.id.name + "." + f.field.id.name;
            local_vars.put( name, f.field.id );
            if ( decl.kind != DeclarationKind.POINTER ) {
                // Check if the field is a pointer
                OffsetLogic offlogic = new OffsetLogic();
                offlogic.add( -current_variable_offset, false );
                current_variable_offset++;

                if ( f.field.kind == DeclarationKind.POINTER ) {
                    if ( f.field.type instanceof StandardType ) {
                        offlogic.add( 2, true );
                    } else if ( f.field.type instanceof RecordType ) {
                        add_record_layout_on_heap((RecordType)f.field.type, offlogic);
                    } else {
                        System.out.println( "Unknown type in Symbols.java symbolcollection" );
                        AST_Printer print = new AST_Printer();
                        decl.accept( print );
                        System.out.println( print.get_ascii() );
                        System.exit(-1);
                    }
                    // System.out.println( "ADDED " + name + ", on the stack. It is a pointer.");

                } else {
                    // System.out.println( "ADDED " + name + ", on the stack. It is not a pointer.");
                }
                
                variable_offsets.put( name, offlogic );
            } else {
                OffsetLogic offlogic = new OffsetLogic();
                offlogic.add( off.offsets.get(0), false );
                offlogic.add( fieldOffset++, true );
                
                if ( f.field.kind == DeclarationKind.POINTER ) {
                    offlogic.add( 2, true );
                } 
                // System.out.println( "It is on heap.... " + name );
                variable_offsets.put( name, offlogic );
                
            }
        }
        // System.out.println( variable_offsets );
    }

    private void add_record_layout_on_heap( RecordType rec, OffsetLogic logic ) {

    }

    public void add_capture( LexIdent variable ) {
        capture_vars.put( variable.name, variable );
    }

    public LexIdent lookup_parameter( String variable_name ) {
        LexIdent var = parameters.get( variable_name );
        return var;
    }

    public LexIdent lookup_this_scope( String variable_name ) {
        LexIdent var = local_vars.get( variable_name );
        return var;
    }


    public LexIdent lookup_capture( String variable_name ) {
        if ( local_vars.containsKey( variable_name ) )
            return local_vars.get( variable_name );
        return capture_vars.get( variable_name );
    }

    public Set<String> get_variables() {
        return local_vars.keySet();
    }

    public Set<String> get_captures() {
        return capture_vars.keySet();
    }

    public Set<String> get_parameters() {
        return parameters.keySet();
    }

    public int get_parameter_offset( String parameter ) { 
        // Becaue I'm not pushing the variables in reverse order, 
        // I have to return the variables in reverse order.
        return (current_parameter_offset+1) - parameter_offsets.getOrDefault(parameter, -69);
    }

    // This function needs to check if it is on the stack or on the heap
    // On stack: If record: Lookup the name. Find the offset for the field
    // On heap: Find the pointer on the stack, find the offset for the field on the heap.
    public OffsetLogic get_variable_offset( String variable ) {
        // if ( variable.contains(".") )
        //     return variable_offsets.getOrDefault(variable.substring(0, variable.indexOf(".")), -420);
        // else
        // System.out.println("============================");
        // System.out.println( variable_offsets );
        // System.out.println("============================");
        return variable_offsets.get(variable);
    }

    public boolean can_access( String variable ) {
        return local_vars.containsKey( variable ) || capture_vars.containsKey( variable ) || parameters.containsKey(variable);
    }

    public String toString() {
        return "Params" + parameters.keySet().toString() + " | Vars: " + local_vars.keySet().toString() ;
    }

    public void register_static_scope( Symbols parent ) {

        // Add for static_scope
        for ( String name : parent.get_variables() ) {
            // System.out.println( name );
            // System.out.println( parent.variable_offsets.get(name) );

            OffsetLogic parent_logic = parent.variable_offsets.get(name);
            
            OffsetLogic logic = new OffsetLogic();

            logic.add( 0, false );

            for ( int i = 0; i < parent_logic.size(); ++i ) {
                Integer offset = parent_logic.offsets.get( i );
                logic.add( offset, parent_logic.onHeap.get( i ) );
            }

            variable_offsets.put( name, logic );
        }
        
        // System.out.println( variable_offsets );
    }
}
