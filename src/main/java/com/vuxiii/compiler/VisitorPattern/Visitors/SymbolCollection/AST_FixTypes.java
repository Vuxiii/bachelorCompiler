package com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection;

import java.util.HashMap;
import java.util.Map;

import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;
import com.vuxiii.compiler.Parser.Nodes.Types.AliasType;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.Parser.Nodes.Types.StandardType;
import com.vuxiii.compiler.Parser.Nodes.Types.Type;
import com.vuxiii.compiler.Parser.Nodes.Types.UnknownType;
import com.vuxiii.compiler.VisitorPattern.Visitor;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_FixTypes extends Visitor {
    
    public Map<String, FunctionType> function_names = new HashMap<>();

    public AST_FixTypes( ) {
        // System.exit(-1);
    }


    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void fix_unknown_declaration_type( Declaration decl ){
        if ( decl.kind != DeclarationKind.UNKNOWN ) return;

        
        if ( decl.type instanceof AliasType ) {
            // Figure out what kind of alias type it is
            // To another usertype
            // To a std type?
        } else if ( decl.type instanceof FunctionType ) {
            decl.kind = DeclarationKind.NEW_FUNCTION_TYPE;
        } else if ( decl.type instanceof StandardType ) {
            decl.kind = DeclarationKind.ALIAS_TO_STD_TYPE;
        }

    }


    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2 )
    public void register_function_name( Declaration func_decl ) {
        if ( func_decl.kind != DeclarationKind.NEW_FUNCTION_TYPE ) return;

        function_names.put( func_decl.id.name, (FunctionType)func_decl.type );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 3 )
    public void fix_UnknownType_class( Declaration decl ){
        if ( !(decl.type instanceof UnknownType ) ) return;

        if ( decl.kind == DeclarationKind.FUNCTION ) {
            // Look it up. If it doesn't exit. Error

            FunctionType ft = function_names.get( ((UnknownType)decl.type).unknown_type.name );
            if ( ft == null ) {
                // error
                System.out.println( "Error in fixtypes" );
                System.out.println( ((UnknownType)decl.type).unknown_type.name );
                System.exit(-1);
            }

            decl.type = ft;

        }
        
        // if ( ut.unknown_type. )


    }

}
