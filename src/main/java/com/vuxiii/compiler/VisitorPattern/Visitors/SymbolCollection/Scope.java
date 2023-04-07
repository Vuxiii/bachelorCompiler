package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.Parser.Nodes.Types.RecordType;
import com.vuxiii.compiler.Parser.Nodes.Types.Type;

public class Scope {
    
    private Map<String, LexIdent> local_vars = new HashMap<>();
    private Map<String, LexIdent> parameters = new HashMap<>();
    private Set<RecordType> records = new HashSet<>();
    private Map<String, Integer> parameter_offsets = new HashMap<>();
    private int current_parameter_offset = 2;
    
    private Map<String, Integer> variable_offsets = new HashMap<>();
    private int current_variable_offset = 1;

    private Set<String> is_var_heap_allocated = new HashSet<>();

    private Map<String, LexIdent> capture_vars = new HashMap<>();


    public void add_variable( LexIdent variable ) {
        if ( local_vars.containsKey(variable.name) ) return;
        local_vars.put( variable.name, variable );
        variable_offsets.put( variable.name, current_variable_offset++ );
    }

    public void add_parameter( LexIdent variable ) {
        if ( parameters.containsKey(variable.name) ) return;
        parameters.put( variable.name, variable );
        parameter_offsets.put( variable.name, current_parameter_offset++ );
    }

    public void add_fields( Declaration decl ) {
        RecordType type = (RecordType)decl.type;
        for ( Field f : type.fields.fields ) {
            String name = decl.id.name + "." + f.field.id.name;
            local_vars.put( name, f.field.id );
            variable_offsets.put( name, current_variable_offset++ );
        }
    }

    public void add_record( RecordType rec ) {
        records.add( rec );
    }

    public void identifier_is_heap_allocated( String identifier ) {
        is_var_heap_allocated.add( identifier );
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

    public boolean isHeapAllocated( String variable_name ) {
        return is_var_heap_allocated.contains( variable_name );
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
        return (current_parameter_offset+1) - parameter_offsets.getOrDefault(parameter, -1);
    }

    public int get_variable_offset( String variable ) {
        return variable_offsets.getOrDefault(variable, -1);
    }

    public boolean can_access( String variable ) {
        return local_vars.containsKey( variable ) || capture_vars.containsKey( variable ) || parameters.containsKey(variable);
    }

    public String toString() {
        return "Params" + parameters.keySet().toString() + " | Vars: " + local_vars.keySet().toString() ;
    }

    public boolean has_record(Type type) {
        return records.contains(type);
    }
}
