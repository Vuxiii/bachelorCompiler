package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection;

import java.util.ArrayList;
import java.util.List;

import com.vuxiii.compiler.Parser.Nodes.Argument;
import com.vuxiii.compiler.VisitorPattern.ASTNode;

public class StringNode {
    private static int string_count = 0;


    public final int id;
    public final String name;
    public String str_literal;
    public List<Integer> stop_indicators = new ArrayList<>();
    public List<ASTNode> substitutes = new ArrayList<>();


    public final String stop_name;
    public final String substitute_name;

    public StringNode( String str_literal, Argument arg_list ) {
        this.str_literal = str_literal;
        id = string_count++;
        name = "string" + id;
        stop_name = name + "_stops";
        substitute_name = name + "_subs";
        System.out.println(str_literal + "#################");
        
        int actual_length = 0;
                
        for ( int i = 1; i < str_literal.length()-1; ++i ) { // To skip the quotes '"' '"'
            char c = str_literal.charAt( i );
            if ( c != '%' ) continue;

            // We want to be able to print the string '%' by escaping it.
            if ( i > 0 && str_literal.charAt(i-1) == '\\' ) continue;            

            stop_indicators.add( i-1 );
        }


        Argument current = arg_list;
        while ( current != null ) {
            
            substitutes.add( current.node );


            current = arg_list.next.isPresent() ? arg_list.next.get() : null;
        }

        

        stop_indicators.add( actual_length );
        System.out.println( stop_indicators );
    }

    public String toString() {
        String out  = "";

        out += "Name: " + name + "\n";
        out += "\tLiteral: " + str_literal + "\n\tStop_Indicators: " + stop_indicators.toString() + "\n\tSubstitutes: " + substitutes.toString();

        return out;
    }
}
