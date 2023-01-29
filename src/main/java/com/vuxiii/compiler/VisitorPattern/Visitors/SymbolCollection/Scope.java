package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;

public class Scope {
    
    private Map<String, LexIdent> vars = new HashMap<>();

    private Map<String, LexIdent> capture_vars = new HashMap<>();

    public void add( LexIdent variable ) {
        vars.put( variable.name, variable );
    }

    public void add_capture( LexIdent variable ) {
        capture_vars.put( variable.name, variable );
    }

    public LexIdent lookup_this_scope( String variable_name ) {
        LexIdent var = vars.get( variable_name );
        return var;
    }

    public LexIdent lookup_capture( String variable_name ) {
        if ( vars.containsKey( variable_name ) )
            return vars.get( variable_name );
        return capture_vars.get( variable_name );
    }

    public Set<String> get_variables() {
        return vars.keySet();
    }

    public Set<String> get_captures() {
        return capture_vars.keySet();
    }

    public boolean can_access( String variable ) {
        return vars.containsKey( variable ) || capture_vars.containsKey( variable );
    }
}
