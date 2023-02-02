package com.vuxiii.compiler.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Grammar;
import com.vuxiii.LR.LRParser;
import com.vuxiii.LR.ParseTable;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexOperator;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexType;
import com.vuxiii.compiler.Parser.Nodes.Argument;
import com.vuxiii.compiler.Parser.Nodes.ArgumentKind;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperation;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperationKind;
import com.vuxiii.compiler.Parser.Nodes.Capture;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;
import com.vuxiii.compiler.Parser.Nodes.Expression;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.ScopeNode;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.Parser.Nodes.StatementKind;
import com.vuxiii.compiler.Parser.Nodes.Types.AliasType;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.Parser.Nodes.Types.StandardType;
import com.vuxiii.compiler.Parser.Nodes.Types.Type;
import com.vuxiii.compiler.Parser.Nodes.Types.UnknownType;
import com.vuxiii.compiler.Parser.Nodes.Types.UserType;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.Parser.Nodes.FunctionCall;
import com.vuxiii.compiler.Parser.Nodes.Parameter;
import com.vuxiii.compiler.VisitorPattern.ASTNode;


public class Parser {
    
    private static Grammar g = null;

    private static Map<String, Type> stored_user_types = new HashMap<>();

    public static ASTNode getAST( List<ASTToken> tokens ) {
        if ( g == null )
            init();

        
        ParseTable table = LRParser.parse( g, Symbol.n_Start );
        
        // System.out.println( tokens );

        ASTNode ast = (ASTNode)LRParser.getAST( table, tokens );
        // return AST;
        return ast;
    }

    private static void init() {
        g = new Grammar();
        
        // for ( PrimitiveType type : PrimitiveType.values() ) {
        //     Type.types.put( type.name(), new StandardType( null, new LexType( new MatchInfo( type.name(), -1, -1 ), type.token ) ) );
        // }
        // n_Start -> n_StatementList t_Dollar
        g.addRuleWithReduceFunction( Symbol.n_Start, List.of( Symbol.n_StatementList, Symbol.t_Dollar ), t -> {
            return t.get(0);
        });
        

        // n_StatementList -> n_StatementList n_Statement
        g.addRuleWithReduceFunction( Symbol.n_StatementList, List.of( Symbol.n_Statement, Symbol.n_StatementList ), t -> {
            Statement stm1 = (Statement)t.get(0);
            Statement stm2 = (Statement)t.get(1);
            return new Statement( Symbol.n_StatementList, stm1.node, stm2, stm1.kind );
        });

        // n_StatementList -> n_Statement
        g.addRuleWithReduceFunction( Symbol.n_StatementList, List.of( Symbol.n_Statement ), t -> {
            Statement stm1 = (Statement)t.get(0);
            return new Statement( Symbol.n_StatementList, stm1.node, stm1.kind );
        });

        // n_Statement -> n_Declaration t_Semicolon
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Declaration, Symbol.t_Semicolon ), t -> {
            return new Statement( Symbol.n_Statement, (Declaration)t.get(0), StatementKind.DECLARATION );
        });

        // n_Declaration -> n_Declaration_Function 
        g.addRuleWithReduceFunction( Symbol.n_Declaration, List.of( Symbol.n_Declaration_Function ), t -> {
            Declaration decl = (Declaration)t.get(0);
            decl.term = Symbol.n_Declaration;
            return decl;
        });


        // n_Declaration -> n_Declaration_Variable
        g.addRuleWithReduceFunction( Symbol.n_Declaration, List.of( Symbol.n_Declaration_Variable ), t -> {
            Declaration decl = (Declaration)t.get(0);
            decl.term = Symbol.n_Declaration;
            return decl;
        });

        // n_Declaration -> n_Declaration_Type 
        g.addRuleWithReduceFunction( Symbol.n_Declaration, List.of( Symbol.n_Declaration_Type ), t -> {
            Declaration decl = (Declaration)t.get(0);
            decl.term = Symbol.n_Declaration;
            return decl;
        });

        
        // n_Statement -> n_Assignment t_Semicolon
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Assignment, Symbol.t_Semicolon ), t -> {
            return new Statement( Symbol.n_Statement, (ASTNode)t.get(0), StatementKind.ASSIGNMENT );
        });

        // n_Statement -> n_Print t_Semicolon
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Print, Symbol.t_Semicolon ), t -> {
            return new Statement( Symbol.n_Statement, (ASTNode)t.get(0), StatementKind.PRINT );
        });


        // --[[ Scopes ]]--

        // n_Statement -> n_Scope
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Scope ), t -> {
            // System.out.println( "yay");
            // System.exit(-1);
            return new Statement( Symbol.n_Statement, (ASTNode)t.get(0), StatementKind.SCOPE );
        });

        // n_Scope -> n_Capture n_Scope_Block
        g.addRuleWithReduceFunction( Symbol.n_Scope, List.of( Symbol.n_Capture, Symbol.n_Scope_Block ), t -> {
            return new ScopeNode( Symbol.n_Scope, (Statement)t.get(1), (Capture)t.get(0) );
        });

        // n_Scope -> n_Scope_Block
        g.addRuleWithReduceFunction( Symbol.n_Scope, List.of( Symbol.n_Scope_Block ), t -> {
            return new ScopeNode( Symbol.n_Scope, (Statement)t.get(0) );
        });

        // n_Scope_Block -> t_LCurly n_StatementList t_RCurly
        g.addRuleWithReduceFunction( Symbol.n_Scope_Block, List.of( Symbol.t_LCurly, Symbol.n_StatementList, Symbol.t_RCurly ), t -> {
            Statement stm = (Statement)t.get(1);
            if ( stm.next.isPresent() )
                return new Statement( Symbol.n_Scope_Block, stm.node, stm.next.get(), stm.kind );
            else
                return new Statement( Symbol.n_Scope_Block, stm.node, stm.kind );

        });


        // n_Capture -> t_LBracket n_Arg_List t_RBracket
        g.addRuleWithReduceFunction( Symbol.n_Capture, List.of( Symbol.t_LBracket, Symbol.n_Arg_List, Symbol.t_RBracket ), t -> {
            return new Capture( Symbol.n_Capture, (Argument)t.get(1) );
        });

        // n_Capture -> t_LBracket t_Dot t_Dot t_RBracket
        g.addRuleWithReduceFunction( Symbol.n_Capture, List.of( Symbol.t_LBracket, Symbol.t_Dot, Symbol.t_Dot, Symbol.t_RBracket ), t -> {
            return new Capture( Symbol.n_Capture );
        });






        // --[[ Functions ]]--
        
        // n_Statement -> t_Identifier t_LParen t_RParen t_Semicolon
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Function_Call, Symbol.t_Semicolon ), t -> {
            FunctionCall function_name = (FunctionCall)t.get(0);
            return new Statement( Symbol.n_Statement, function_name, StatementKind.EXPRESSION );
        });

        // n_Function_Call -> t_Identifier t_LParen t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Function_Call, List.of( Symbol.t_Identifier, Symbol.t_LParen, Symbol.t_RParen ), t -> {
            LexIdent function_name = (LexIdent)t.get(0);
            return new FunctionCall( Symbol.n_Function_Call, function_name );
        });

        // n_Function_Call -> t_Identifier t_LParen n_Arg_List t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Function_Call, List.of( Symbol.t_Identifier, Symbol.t_LParen, Symbol.n_Arg_List, Symbol.t_RParen ), t -> {
            LexIdent function_name = (LexIdent)t.get(0);
            Argument args = (Argument)t.get(2);
            return new FunctionCall( Symbol.n_Function_Call, function_name, args );
        });
        

        // n_Arg_List -> n_Arg t_Comma n_Arg_List
        g.addRuleWithReduceFunction( Symbol.n_Arg_List, List.of( Symbol.n_Arg, Symbol.t_Comma, Symbol.n_Arg_List ), t -> {
            Argument arg = (Argument)t.get(0);
            return new Argument( Symbol.n_Arg_List, arg.node, (Argument)t.get(2), arg.kind );
        });

        // n_Arg_List -> n_Arg
        g.addRuleWithReduceFunction( Symbol.n_Arg_List, List.of( Symbol.n_Arg ), t -> {
            Argument arg = (Argument)t.get(0);
            return new Argument( Symbol.n_Arg_List, arg.node, arg.kind );
        });

        // n_Arg -> t_Identifier
        g.addRuleWithReduceFunction( Symbol.n_Arg, List.of( Symbol.t_Identifier ), t -> {
            return new Argument( Symbol.n_Arg, (ASTNode)t.get(0), ArgumentKind.IDENTIFIER );
        });

        // n_Arg -> t_Literal
        g.addRuleWithReduceFunction( Symbol.n_Arg, List.of( Symbol.n_Literal ), t -> {
            return new Argument( Symbol.n_Arg, (ASTNode)t.get(0), ArgumentKind.LITERAL );
        });


        // --[[ Arithmetic ]]--

        // n_Expression -> n_Expression_Arithmetic
        g.addRuleWithReduceFunction( Symbol.n_Expression, List.of( Symbol.n_Expression_Arithmetic ), t -> {
            return new Expression( Symbol.n_Expression, (ASTNode)t.get(0) );
        });

        // --[[ PLUS, MINUS ]]--

        // n_Expression_Arithmetic -> n_Expression_Arithmetic t_Plus n_Term
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Expression_Arithmetic, Symbol.t_Plus, Symbol.n_Term ), t -> {
            return new BinaryOperation( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.PLUS );
        });

        // n_Expression_Arithmetic -> n_Expression_Arithmetic t_Minus n_Term
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Expression_Arithmetic, Symbol.t_Minus, Symbol.n_Term ), t -> {
            return new BinaryOperation( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.MINUS );
        });

        // n_Expression_Arithmetic -> n_Term
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Term ), t -> {
            return new Expression( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0) );
        });


        // --[[ TIMES, DIVISION ]]--

        // n_Term -> t_Minus n_Term t_Times n_Value
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Term, Symbol.t_Times, Symbol.n_Value ), t -> {
            return new BinaryOperation( Symbol.n_Term, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.TIMES );
        });

        // n_Term -> t_Minus n_Term t_Division n_Value
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Term, Symbol.t_Division, Symbol.n_Value ), t -> {
            return new BinaryOperation( Symbol.n_Term, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.DIVISION );
        });

        // n_Term -> t_Minus n_Value
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Value ), t -> {
            return new Expression( Symbol.n_Term, (ASTNode)t.get(0) );
        });

        // --[[ UNARY MINUS ]]-- do better. quick solution

        // n_Value -> t_Minus n_Factor
        g.addRuleWithReduceFunction( Symbol.n_Value, List.of( Symbol.t_Minus, Symbol.n_Factor ), t -> {
            return new BinaryOperation( Symbol.n_Value, new LexLiteral(new MatchInfo("0", -1, -1), TokenType.INT), (ASTNode)t.get(1), new LexOperator( new MatchInfo("-", -1, -1), TokenType.MINUS ), BinaryOperationKind.MINUS );
        });

        // n_Value -> n_Factor
        g.addRuleWithReduceFunction( Symbol.n_Value, List.of( Symbol.n_Factor ), t -> {
            return new Expression( Symbol.n_Value, (ASTNode)t.get(0) );
        });

        // --[[ (), int, variable ]]--

        // n_Factor -> t_LParen n_Expression_Arithmetic t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.t_LParen, Symbol.n_Expression_Arithmetic, Symbol.t_RParen ), t -> {
            return new Expression( Symbol.n_Factor, (ASTNode)t.get(1) );
        });

        // n_Factor -> n_Identifier
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.t_Identifier ), t -> {
            return new Expression( Symbol.n_Factor, (ASTNode)t.get(0) );
        });

        // n_Factor -> n_Literal
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.n_Literal ), t -> {
            return new Expression( Symbol.n_Factor, (ASTNode)t.get(0) );
        });

        // n_Literal -> t_Integer
        g.addRuleWithReduceFunction( Symbol.n_Literal, List.of( Symbol.t_Integer ), t -> {
            return new Expression( Symbol.n_Literal, (ASTNode)t.get(0) );
        } );

        // n_Literal -> t_Double
        g.addRuleWithReduceFunction( Symbol.n_Literal, List.of( Symbol.t_Double ), t -> {
            return new Expression( Symbol.n_Literal, (ASTNode)t.get(0) );
        } );

        // --[[ Function ]]--


        // n_Assignment_Function -> n_Function_Signature t_LCurly n_Statement_List t_RCurly
        g.addRuleWithReduceFunction( Symbol.n_Assignment_Function, List.of( Symbol.n_Function_Signature, Symbol.t_LCurly, Symbol.n_StatementList, Symbol.t_RCurly ), t -> {
            FunctionType func = (FunctionType)t.get(0);
            Statement body = (Statement)t.get(2);
            if ( func.parameters.isPresent() ) {
                if ( func.return_type.isPresent() )
                    return new FunctionType( Symbol.n_Assignment_Function, func.parameters.get(), func.return_type.get(), body );
                else
                   return new FunctionType( Symbol.n_Assignment_Function, func.parameters.get(), body );
            } else {
                if ( func.return_type.isPresent() )
                    return new FunctionType( Symbol.n_Assignment_Function, func.return_type.get(), body );
                else
                   return new FunctionType( Symbol.n_Assignment_Function, body );
            }
        });


        // n_Declaration_Function -> t_Let t_Identifier t_Colon n_Function_Signature
        g.addRuleWithReduceFunction( Symbol.n_Declaration_Function, List.of( Symbol.t_Let, Symbol.t_Identifier, Symbol.t_Colon, Symbol.n_Function_Signature ), t -> {
            LexIdent name = (LexIdent)t.get(1);
            FunctionType func = (FunctionType)t.get(3);
            return new Declaration( Symbol.n_Declaration_Function, name, func, DeclarationKind.FUNCTION );
        });

        // n_Function_Signature -> n_Function_Param_Signature n_Return_Type
        g.addRuleWithReduceFunction( Symbol.n_Function_Signature, List.of( Symbol.n_Function_Param_Signature, Symbol.n_Return_Type ), t -> {
            FunctionType func = (FunctionType)t.get(0);
            Type return_type = (Type)t.get(1);
            // System.out.println( func );
            // System.out.println( func.parameters );

            if ( func.parameters.isPresent() )
                return new FunctionType( Symbol.n_Function_Signature, func.parameters.get(), return_type );
            else
                return new FunctionType( Symbol.n_Function_Signature, return_type );
        });

        // Function with implicit return type of 'void'
        // n_Function_Signature -> n_Function_Param_Signature
        g.addRuleWithReduceFunction( Symbol.n_Function_Signature, List.of( Symbol.n_Function_Param_Signature ), t -> {
            FunctionType func = (FunctionType)t.get(0);
            
            if ( func.parameters.isPresent() )
                return new FunctionType( Symbol.n_Function_Signature, func.parameters.get() ); // Do std.void
            else
                return new FunctionType( Symbol.n_Function_Signature ); // Do std.void
        });

        // n_Function_Param_Signature -> t_LParen n_Parameter_List t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Function_Param_Signature, List.of( Symbol.t_LParen, Symbol.n_Parameter_List, Symbol.t_RParen ), t -> {
            Parameter param_list = (Parameter)t.get(1);
            return new FunctionType( Symbol.n_Function_Param_Signature, param_list );
        });

        // n_Function_Param_Signature -> t_LParen t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Function_Param_Signature, List.of( Symbol.t_LParen, Symbol.t_RParen ), t -> {
            return new FunctionType( Symbol.n_Function_Param_Signature );
        });

        // n_Return_Type -> t_Arrow_Right n_Any_Type
        g.addRuleWithReduceFunction( Symbol.n_Return_Type, List.of( Symbol.t_Arrow_Right, Symbol.n_Any_Type ), t -> {
            if ( t.get(1) instanceof StandardType ) {
                StandardType type = (StandardType)t.get(1);
                type.term = Symbol.n_Return_Type;
                return type;
            } else if ( t.get(1) instanceof UserType ) {
                UserType type = (UserType)t.get(1);
                type.term = Symbol.n_Return_Type;
                return type;
            } else if ( t.get(1) instanceof LexIdent ) {
                return new UnknownType( Symbol.n_Return_Type, (LexIdent)t.get(1) );
            } else if ( t.get(1) instanceof AliasType ) {
                AliasType type = (AliasType)t.get(1);
                type.term = Symbol.n_Return_Type;
                return type;
            } else if ( t.get(1) instanceof FunctionType ) {
                FunctionType type = (FunctionType)t.get(1);
                type.term = Symbol.n_Return_Type;
                return type;
            } else if ( t.get(1) instanceof UnknownType ) {
                UnknownType type = (UnknownType)t.get(1);
                type.term = Symbol.n_Return_Type;
                return type;
            } else {
                System.out.println( t.get(1) );
                System.out.println( "Found some unexpected token in n_Return_Type -> n_Any_Type" );
                System.exit(-1);
                return null;
            }
            
        });


        // n_Parameter_List -> n_Parameter t_Comma n_Parameter_List
        g.addRuleWithReduceFunction( Symbol.n_Parameter_List, List.of( Symbol.n_Parameter, Symbol.t_Comma, Symbol.n_Parameter_List ), t -> {
            Declaration decl = (Declaration)t.get(0);
            return new Parameter( Symbol.n_Parameter_List, decl, (Parameter)t.get(2) );
        });

        // n_Parameter_List -> n_Parameter
        g.addRuleWithReduceFunction( Symbol.n_Parameter_List, List.of( Symbol.n_Parameter ), t -> {
            Declaration decl = (Declaration)t.get(0);
            return new Parameter( Symbol.n_Parameter_List, decl );
        });


        // n_Parameter -> t_Identifier t_Colon n_Standard_Type
        g.addRuleWithReduceFunction( Symbol.n_Parameter, List.of( Symbol.t_Identifier, Symbol.t_Colon, Symbol.n_Any_Type ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            Type type = (Type)t.get(2);
            return new Declaration( Symbol.n_Parameter, id, type, DeclarationKind.PARAMETER );
        });

        // n_Declaration_Variable -> t_Let t_Identifier t_Colon n_User_Type
        g.addRuleWithReduceFunction( Symbol.n_Declaration_Variable, List.of( Symbol.t_Let, Symbol.t_Identifier, Symbol.t_Colon, Symbol.n_Any_Type ), t -> {
            LexIdent id = (LexIdent)t.get(1);

            if ( t.get(3) instanceof Field ) {
                Field fields = (Field)t.get(3);             
                UserType type = new UserType( Symbol.n_Declaration_Variable, id, fields );
                
                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.VARIABLE );
            } else if (t.get(3) instanceof StandardType ) { 
                StandardType type = (StandardType)t.get(3); 
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.VARIABLE );
            } else if ( t.get(3) instanceof LexIdent ) {
                LexIdent ident = (LexIdent)t.get(3);             
                Type type = stored_user_types.get( ident.name );
                
                if ( type == null )
                    return new Declaration( Symbol.n_Declaration_Variable, id, new UnknownType(Symbol.n_Declaration_Variable, ident ), DeclarationKind.VARIABLE );

                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.VARIABLE );
            } else if ( t.get(3) instanceof FunctionType ) {
                FunctionType type = (FunctionType)t.get(3);    
                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.FUNCTION );
            }else if ( t.get(3) instanceof UnknownType ) {
                UnknownType type = (UnknownType)t.get(3);    
                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.FUNCTION );
            }
            System.out.println("--[[ Parser Error ]]--\nSomething happend trying to parse a user type. It is not a UserType or an Identifier. So what is it?" );
            System.out.println( t.get(3) );
            
            System.exit(-1);
            return null; // error!
        });


        // Make an 'alias' for a standard type. Like person -> int.
        // n_Declaration_Type -> n_Declaration_Type_Body n_Standard_Type
        g.addRuleWithReduceFunction( Symbol.n_Declaration_Type, List.of( Symbol.n_Declaration_Type_Body, Symbol.n_Any_Type ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            Type type = (Type)t.get(1);
                        
            return new Declaration( Symbol.n_Declaration_Type, id, type, DeclarationKind.UNKNOWN );
        });
        
        
        // n_Declaration_Type_Body -> t_Type_Declare t_Identifer t_Colon
        g.addRuleWithReduceFunction( Symbol.n_Declaration_Type_Body, List.of( Symbol.t_Type_Declare, Symbol.t_Identifier, Symbol.t_Colon ), t -> {
            
            LexIdent body = (LexIdent)t.get(1);
            body.term = Symbol.n_Declaration_Type_Body;
            return body;
        });
        
        // n_User_Type -> t_LCurly n_Field_List t_RCurly
        g.addRuleWithReduceFunction( Symbol.n_User_Type, List.of( Symbol.t_LCurly, Symbol.n_Field_List, Symbol.t_RCurly ), t -> {
            return new Field( Symbol.n_User_Type, (ASTNode)t.get(1) );
        });
        
        
        // n_User_Type -> t_Identifier
        g.addRuleWithReduceFunction( Symbol.n_User_Type, List.of( Symbol.t_Identifier ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            // id.term = Symbol.n_User_Type;
            return new UnknownType( Symbol.n_User_Type, id );
        });

        // n_User_Type -> n_Function_Signature
        g.addRuleWithReduceFunction( Symbol.n_User_Type, List.of( Symbol.n_Function_Signature ), t -> {
            FunctionType id = (FunctionType)t.get(0);
            id.term = Symbol.n_User_Type;
            
            return id;
        });
        
        // --[[ FIELD IN STRUCT ]]--

        // n_Field_List -> n_Field t_Semicolon n_Field_List
        g.addRuleWithReduceFunction( Symbol.n_Field_List, List.of( Symbol.n_Field, Symbol.t_Semicolon, Symbol.n_Field_List ), t -> {
            Declaration decl = (Declaration)t.get(0);
            return new Statement( Symbol.n_Field_List, decl, (Statement)t.get(2), StatementKind.DECLARATION );
        });
        
        // n_Field_List -> n_Field t_Semicolon
        g.addRuleWithReduceFunction( Symbol.n_Field_List, List.of( Symbol.n_Field, Symbol.t_Semicolon ), t -> {
            return new Statement( Symbol.n_Field_List, (Declaration)t.get(0), StatementKind.DECLARATION );
        });
        
        // n_Field -> t_Identifier t_Colon n_Standard_Type
        g.addRuleWithReduceFunction( Symbol.n_Field, List.of( Symbol.t_Identifier, Symbol.t_Colon, Symbol.n_Standard_Type ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            StandardType type = (StandardType)t.get(2);
            
            return new Declaration( Symbol.n_Field, id, type, DeclarationKind.VARIABLE );
        });


        // n_Field -> t_Identifier t_Colon n_User_Type
        g.addRuleWithReduceFunction( Symbol.n_Field, List.of( Symbol.t_Identifier, Symbol.t_Colon, Symbol.n_User_Type ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            
            if ( t.get(2) instanceof LexIdent ) {
                
                Type type = stored_user_types.get(((LexIdent)t.get(2)).name);
                
                if ( type == null ) {
                    return new Declaration( Symbol.n_Field, id, new UnknownType(Symbol.n_Field, (LexIdent)t.get(2) ), DeclarationKind.ALIAS_TO_USER_TYPE );
                }

                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Field, id, type, DeclarationKind.ALIAS_TO_USER_TYPE );
                
            } else if ( t.get(2) instanceof Field ) {

                Field fields = (Field)t.get(2);
                UserType type = new UserType(Symbol.n_Declaration_Type, id, fields );
                
                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Field, id, type, DeclarationKind.USER_TYPE );

            }
            
            System.out.println( "Something bad happend in parsin n_Field -> t_Identifier t_Colon n_User_Type" );
            System.exit(-1);
            return null;

    
        });


        // --[[ TYPES ]]--
        
        // n_Any_Type -> t_Standard_type
        g.addRuleWithReduceFunction( Symbol.n_Any_Type, List.of( Symbol.n_Standard_Type ), t -> {
            StandardType st = (StandardType)t.get(0);
            st.term = Symbol.n_Any_Type;
            return st;
        });
        // n_Any_Type -> n_User_type
        g.addRuleWithReduceFunction( Symbol.n_Any_Type, List.of( Symbol.n_User_Type ), t -> {
            Type st = (Type)t.get(0);
            st.term = Symbol.n_Any_Type;
            return st;
        });

        // n_Standard_Type -> t_Type_Int
        g.addRuleWithReduceFunction( Symbol.n_Standard_Type, List.of( Symbol.t_Type_Int ), t -> {
            return new StandardType( Symbol.n_Standard_Type, (LexType)t.get(0) );
        });
        // n_Standard_Type -> t_Type_Double
        g.addRuleWithReduceFunction( Symbol.n_Standard_Type, List.of( Symbol.t_Type_Double ), t -> {
            return new StandardType( Symbol.n_Standard_Type, (LexType)t.get(0)  );
        });

        // n_Assignment -> t_Identifier t_Equals n_Expression
        g.addRuleWithReduceFunction( Symbol.n_Assignment, List.of( Symbol.t_Identifier, Symbol.t_Equals, Symbol.n_Expression ), t -> {
            return new Assignment( Symbol.n_Assignment, (LexIdent)t.get(0), (ASTNode)t.get(2)  );
        });

        // n_Assignment -> t_Identifier t_Equals n_Assignment_Function
        g.addRuleWithReduceFunction( Symbol.n_Assignment, List.of( Symbol.t_Identifier, Symbol.t_Equals, Symbol.n_Assignment_Function ), t -> {
            return new Assignment( Symbol.n_Assignment, (LexIdent)t.get(0), (ASTNode)t.get(2)  );
        });

        // n_Print -> t_Print t_Equals t_LParen n_Expression t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Print, List.of( Symbol.t_Print, Symbol.t_LParen, Symbol.n_Expression, Symbol.t_RParen ), t -> {
            return new Print( Symbol.n_Print, (ASTNode)t.get(2) );
        });
        
    }


}
