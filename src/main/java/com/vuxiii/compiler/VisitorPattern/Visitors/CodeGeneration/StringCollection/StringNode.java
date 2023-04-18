package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection;

import java.util.ArrayList;
import java.util.List;

import com.vuxiii.compiler.Lexer.Tokens.PrimitiveType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Parser.Nodes.Argument;
import com.vuxiii.compiler.Parser.Nodes.ArgumentList;
import com.vuxiii.compiler.Parser.Nodes.Expression;
import com.vuxiii.compiler.VisitorPattern.ASTNode;

public class StringNode {
    private static int string_count = 0;


    public final int id;
    public final String name;
    public String str_literal;
    public List<Integer> stop_indicators = new ArrayList<>();
    public List<ASTNode> substitutes = new ArrayList<>();

    public final int num_of_values_on_stack;

    public final String stop_name;
    public final String substitute_name;

    public StringNode( String str_literal, Argument arg_list ) {
        this.str_literal = str_literal;
        id = string_count++;
        name = "string" + id;
        stop_name = name + "stops";
        substitute_name = name + "subs";
        
        for ( int i = 1; i < str_literal.length()-1; ++i ) { // To skip the quotes '"' '"'
            char c = str_literal.charAt( i );
            if ( c != '%' ) continue;

            // We want to be able to print the string '%' by escaping it.
            if ( i > 0 && str_literal.charAt(i-1) == '\\' ) continue;            

            stop_indicators.add( i-1 );
        }

        Argument current = arg_list;
        substitutes.add( current.node );

        if ( ((Expression)current.node).node instanceof LexLiteral ) {
            LexLiteral lit = (LexLiteral) ((Expression)current.node).node;
            if ( lit.literal_type == PrimitiveType.STRING )
                num_of_values_on_stack = 0;
            else
                num_of_values_on_stack = 1;
        } else {
            num_of_values_on_stack = 1;
        }

        stop_indicators.add( count_length(str_literal.substring( 1, str_literal.length()-1 ) ) );
        System.out.println( stop_indicators );
    }

    public StringNode( String str_literal, ArgumentList arg_list ) {
        this.str_literal = str_literal;
        id = string_count++;
        name = "string" + id;
        stop_name = name + "stops";
        substitute_name = name + "subs";
                
        for ( int i = 1; i < str_literal.length()-1; ++i ) { // To skip the quotes '"' '"'
            char c = str_literal.charAt( i );
            if ( c != '%' ) continue;

            // We want to be able to print the string '%' by escaping it.
            if ( i > 0 && str_literal.charAt(i-1) == '\\' ) continue;            

            stop_indicators.add( i-1 );
        }

        int s = arg_list.args.size();
        for ( Argument current : arg_list.args ) {
            substitutes.add( current.node );
            if ( ((Expression)current.node).node instanceof LexLiteral ) {
                LexLiteral lit = (LexLiteral) ((Expression)current.node).node;
                if ( lit.literal_type == PrimitiveType.STRING ) s--;
            }
        }

        num_of_values_on_stack = s;

        stop_indicators.add( count_length(str_literal.substring( 1, str_literal.length()-1 ) ) );
        System.out.println( stop_indicators );
    }

    public StringNode( String str_literal ) {
        this.str_literal = str_literal;
        id = string_count++;
        name = "string" + id;
        stop_name = name + "stops";
        substitute_name = name + "subs";
        
        num_of_values_on_stack = 0;


        stop_indicators.add( count_length(str_literal.substring( 1, str_literal.length()-1 ) ) );
        System.out.println( stop_indicators );
    }

    public int count_length( String s ) {
        int len = 0;

        for ( int i = 0; i < s.length(); ++i ) {
            char curr = s.charAt( i );
            if ( curr == '\\' )
                if ( i+1 < s.length() )
                    if ( s.charAt(i+1) != '\\' ) continue;
            len++;
        }

        return len;
    }

    public String toString() {
        String out  = "";

        out += "Name: " + name + "\n";
        out += "\tLiteral: " + str_literal + "\n\tStop_Indicators: " + stop_indicators.toString() + "\n\tSubstitutes: " + substitutes.toString() + "\n";

        return out;
    }
}
