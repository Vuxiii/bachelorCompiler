package com.vuxiii.compiler.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.LR.Grammar;
import com.vuxiii.LR.LRParser;
import com.vuxiii.LR.ParseTable;
import com.vuxiii.LR.ParserException;
import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.compiler.Error.Error;
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
import com.vuxiii.compiler.Parser.Nodes.ElseNode;
import com.vuxiii.compiler.Parser.Nodes.Expression;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.Root;
import com.vuxiii.compiler.Parser.Nodes.ScopeNode;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.Parser.Nodes.StatementKind;
import com.vuxiii.compiler.Parser.Nodes.Types.AliasType;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.Parser.Nodes.Types.RecordType;
import com.vuxiii.compiler.Parser.Nodes.Types.StandardType;
import com.vuxiii.compiler.Parser.Nodes.Types.Type;
import com.vuxiii.compiler.Parser.Nodes.Types.UnknownType;
import com.vuxiii.compiler.Parser.Nodes.Types.UserType;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.Parser.Nodes.FieldList;
import com.vuxiii.compiler.Parser.Nodes.FunctionCall;
import com.vuxiii.compiler.Parser.Nodes.IfElseNode;
import com.vuxiii.compiler.Parser.Nodes.IfNode;
import com.vuxiii.compiler.Parser.Nodes.NestedField;
import com.vuxiii.compiler.Parser.Nodes.Parameter;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_Printer;


public class Parser {
    
    private static Grammar g = null;

    private static Map<String, Type> stored_user_types = new HashMap<>();

    public static ASTNode getAST( List<ASTToken> tokens ) {
        if ( g == null ) {
            long bf = System.currentTimeMillis();
            init();
            long after = System.currentTimeMillis();
            System.out.println( "Compiling the parser took: " + (after - bf) + " milliseconds");
        }

        
        long bf = System.currentTimeMillis();
        ParseTable table = LRParser.compile( g, Symbol.n_Start );
        long after = System.currentTimeMillis();
        System.out.println( "Parsing the input took: " + (after - bf) + " milliseconds");
        
        // System.out.println( tokens );

        try {
            ASTNode ast = (ASTNode)LRParser.parse( table, tokens );
            return new Root( Symbol.n_Root, ast );

        } catch (ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            System.out.println( new Error( "Parser Error!", "Something bad happend while trying to parse the input!").toString() );

            if ( e.ast != null ) {
                AST_Printer printer = new AST_Printer();
                e.ast.accept( printer );

                System.out.println( printer.get_ascii() );
            } else {
                AST_Printer printer = new AST_Printer();
                
                e.ast.accept( printer );

                System.out.println( printer.get_ascii() );

            }
            System.exit(-1);
        }


        return null; // Make compiler happy -.-
    }

    private static void init() {
        g = new Grammar();
        
        // n_Start -> n_StatementList t_Dollar
        g.addRuleWithReduceFunction( Symbol.n_Start, List.of( Symbol.n_StatementList, Symbol.t_Dollar ), t -> {
            return t.get(0);
        });
        
        // n_StatementList -> n_Statement n_StatementList
        g.addRuleWithReduceFunction( Symbol.n_StatementList, List.of( Symbol.n_Statement, Symbol.n_StatementList ), t -> {
            System.out.println( "IM IN LIST" );
            Statement stm1 = (Statement)t.get(0);
            Statement stm2 = (Statement)t.get(1);
            return new Statement( Symbol.n_StatementList, stm1.node, stm2, stm1.kind );
        });

        // n_StatementList -> n_Statement
        g.addRuleWithReduceFunction( Symbol.n_StatementList, List.of( Symbol.n_Statement ), t -> {
            System.out.println( "IM IN SINGLE" );
            Statement stm1 = (Statement)t.get(0);
            return new Statement( Symbol.n_StatementList, stm1.node, stm1.kind );
        });

        init_if_statements();

        // n_Statement -> n_Declaration t_Semicolon
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_Declaration, Symbol.t_Semicolon ), t -> {
            return new Statement( Symbol.n_Statement, (ASTNode)t.get(0), StatementKind.DECLARATION );
        });

        // n_Declaration -> n_Declaration_Variable
        g.addRuleWithReduceFunction( Symbol.n_Declaration, List.of( Symbol.n_Declaration_Variable ), t -> {
            Declaration decl = (Declaration)t.get(0);
            decl.term = Symbol.n_Declaration;
            return decl;
        });

        // n_Declaration -> n_Declaration_Type 
        g.addRuleWithReduceFunction( Symbol.n_Declaration, List.of( Symbol.n_Declaration_Type ), t -> {
            RecordType decl = (RecordType)t.get(0);
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


        init_scope_capture();

        init_functions();

        init_arithmetic();

        init_guards();

        init_types();

        // n_Declaration_Variable -> t_Let t_Identifier t_Colon n_Any_Type
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
            } else if ( t.get(3) instanceof UnknownType ) {
                UnknownType type = (UnknownType)t.get(3);    
                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.UNKNOWN );
            } else if ( t.get(3) instanceof RecordType ) {
                RecordType type = (RecordType)t.get(3);
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.VARIABLE );
            }
            System.out.println("--[[ Parser Error ]]--\nSomething happend trying to parse a user type. So what is it?" );
            System.out.println( t.get(3) );
            
            System.exit(-1);
            return null; // error!
        });

        // n_Declaration_Variable -> t_Let t_Identifier t_Colon t_Times n_Any_Type
        g.addRuleWithReduceFunction( Symbol.n_Declaration_Variable, List.of( Symbol.t_Let, Symbol.t_Identifier, Symbol.t_Colon, Symbol.t_Times, Symbol.n_Any_Type ), t -> {
            LexIdent id = (LexIdent)t.get(1);

            if ( t.get(4) instanceof Field ) {
                Field fields = (Field)t.get(4);             
                UserType type = new UserType( Symbol.n_Declaration_Variable, id, fields );
                
                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.HEAP );
            } else if (t.get(4) instanceof StandardType ) { 
                StandardType type = (StandardType)t.get(4); 
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.HEAP );
            } else if ( t.get(4) instanceof LexIdent ) {
                LexIdent ident = (LexIdent)t.get(4);             
                Type type = stored_user_types.get( ident.name );
                
                if ( type == null )
                    return new Declaration( Symbol.n_Declaration_Variable, id, new UnknownType(Symbol.n_Declaration_Variable, ident ), DeclarationKind.HEAP );

                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.HEAP );
            } else if ( t.get(4) instanceof FunctionType ) {
                FunctionType type = (FunctionType)t.get(4);    
                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.FUNCTION );
            }else if ( t.get(4) instanceof UnknownType ) {
                UnknownType type = (UnknownType)t.get(4);    
                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.UNKNOWN );
            } else if ( t.get(3) instanceof UserType ) {
                UserType type = (UserType)t.get(3);
                return new Declaration( Symbol.n_Declaration_Variable, id, type, DeclarationKind.VARIABLE );
            }
            System.out.println("--[[ Parser Error ]]--\nSomething happend trying to parse a user type. It is not a UserType or an Identifier. So what is it?" );
            System.out.println( t.get(4) );
            
            System.exit(-1);
            return null; // error!
        });


        // n_Declaration_Type -> t_Type_Declare Symbol.t_Identifier t_Colon n_Any_Type
        // g.addRuleWithReduceFunction( Symbol.n_Declaration_Type, List.of( Symbol.t_Type_Declare, Symbol.t_Identifier, Symbol.t_Colon, Symbol.n_Any_Type ), t -> {
        //     LexIdent id = (LexIdent)t.get(1);
        //     Type type = (Type)t.get(3);
        //     DeclarationKind kind = DeclarationKind.UNKNOWN;
        //     if ( type instanceof AliasType ) {
        //         kind = DeclarationKind.ALIAS_TO_USER_TYPE; // Or maybe not lmao
        //     } else if ( type instanceof FunctionType ) {
        //         kind = DeclarationKind.FUNCTION;
        //     } else if ( type instanceof StandardType ) {
        //         kind = DeclarationKind.ALIAS_TO_STD_TYPE;
        //     } else if ( type instanceof UserType ) {
        //         stored_user_types.put( id.name, type );
        //         kind = DeclarationKind.USER_TYPE;
        //     } else if ( type instanceof UnknownType ) {
        //         kind = DeclarationKind.UNKNOWN;
        //     } else {
        //         System.out.println( new Error("Parser error!", "Unexpected unknowntype!" ) );
        //         AST_Printer p = new AST_Printer();
        //         type.accept(p);
        //         System.out.println( p.get_ascii() );
        //         System.exit(-1);
        //     }

        //     return new Declaration( Symbol.n_Declaration_Type, id, type, kind );
        // });

        // n_Declaration_Type -> t_Type_Declare Symbol.t_Identifier t_Colon t_LCurly n_Field_List t_RCurly
        g.addRuleWithReduceFunction( Symbol.n_Declaration_Type, List.of( Symbol.t_Type_Declare, Symbol.t_Identifier, Symbol.t_Colon, Symbol.t_LCurly, Symbol.n_Field_List, Symbol.t_RCurly ), t -> {
            LexIdent id = (LexIdent)t.get(1);
            FieldList field = (FieldList)t.get(4);
            RecordType type = new RecordType( Symbol.n_Declaration_Type, id, field );
            stored_user_types.put( id.name, type );
            return type;
        });

        // n_Assignment -> n_Nested_Field t_Equals n_Expression
        g.addRuleWithReduceFunction( Symbol.n_Assignment, List.of( Symbol.n_Nested_Field, Symbol.t_Equals, Symbol.n_Expression ), t -> {
            if ( t.get(0) instanceof LexIdent ) {
                return new Assignment( Symbol.n_Assignment, (LexIdent)t.get(0), (ASTNode)t.get(2) );
            } else {
                return new Assignment( Symbol.n_Assignment, (NestedField)t.get(0), (ASTNode)t.get(2) );
            }
        });

        // n_Assignment -> n_Nested_Field t_Equals n_Assignment_Function
        g.addRuleWithReduceFunction( Symbol.n_Assignment, List.of( Symbol.n_Nested_Field, Symbol.t_Equals, Symbol.n_Assignment_Function ), t -> {
            if ( t.get(0) instanceof LexIdent ) {
                return new Assignment( Symbol.n_Assignment, (LexIdent)t.get(0), (ASTNode)t.get(2)  );                 
            } else {
                return new Assignment( Symbol.n_Assignment, (NestedField)t.get(0), (ASTNode)t.get(2) );
            }
        });

        // n_Nested_Field -> t_Identifer t_Dot n_Nested_Field
        g.addRuleWithReduceFunction( Symbol.n_Nested_Field, List.of( Symbol.t_Identifier, Symbol.t_Dot, Symbol.n_Nested_Field ), t -> {
            if ( t.get(2) instanceof LexIdent ) {
                NestedField fields = new NestedField( Symbol.n_Nested_Field, (LexIdent)t.get(0) );
                fields.push( (LexIdent)t.get(2) );
                return fields;
            } else {
                NestedField fields = (NestedField)t.get(2);
                fields.addFront( (LexIdent)t.get(0) );
                return fields;
            }
        });


        // n_Nested_Field -> t_Identifier
        g.addRuleWithReduceFunction( Symbol.n_Nested_Field, List.of( Symbol.t_Identifier ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            id.term = Symbol.n_Nested_Field;
            return id;
        });


        // n_Print -> t_Print t_LParen n_Expression t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Print, List.of( Symbol.t_Print, Symbol.t_LParen, Symbol.n_Expression, Symbol.t_RParen ), t -> {
            return new Print( Symbol.n_Print, (ASTNode)t.get(2) );
        });

        // n_Print -> t_Print t_LParen t_StringLiteral t_Comma n_Arg_List t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Print, List.of( Symbol.t_Print, Symbol.t_LParen, Symbol.t_StringLiteral, Symbol.t_Comma, Symbol.n_Arg_List, Symbol.t_RParen ), t -> {
            return new Print( Symbol.n_Print, (ASTNode)t.get(2), (Argument)t.get(4) );
        });

        
    }

    private static void init_if_statements() {

        // n_Statement -> n_If_Blocks t_Semicolon
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.n_If_Blocks ), t -> {
            Statement iff = (Statement)t.get(0);
            iff.term = Symbol.n_Statement;
            return iff;
        });


        // n_If_Blocks -> n_If
        g.addRuleWithReduceFunction( Symbol.n_If_Blocks, List.of( Symbol.n_If ), t -> {
            IfNode iff = (IfNode)t.get(0);
            return new Statement( Symbol.n_If_Blocks, iff, StatementKind.IF );
        });

        // n_If_Blocks -> n_If n_Elif
        g.addRuleWithReduceFunction( Symbol.n_If_Blocks, List.of( Symbol.n_If, Symbol.n_Elif ), t -> {
            IfNode if_block = (IfNode)t.get(0);
            ASTNode elseif_block = (ASTNode)t.get(1);

            ElseNode else_block = new ElseNode( Symbol.n_Else, new Statement( Symbol.n_Statement, elseif_block, StatementKind.ELSE ) );

            IfElseNode if_else_node = new IfElseNode( Symbol.n_If, if_block, else_block );

            return new Statement( Symbol.n_If_Blocks, if_else_node, StatementKind.IF_ELSE_IF );
        });


        // n_Elif -> n_Else
        g.addRuleWithReduceFunction( Symbol.n_Elif, List.of( Symbol.n_Else ), t -> {
            ElseNode else_block = (ElseNode)t.get(0);
            else_block.term = Symbol.n_Elif;
            return else_block;
        });

        // n_If -> t_If t_LParen n_Expression t_RParen n_Statement
        g.addRuleWithReduceFunction( Symbol.n_If, List.of( Symbol.t_If, Symbol.t_LParen, Symbol.n_Expression, Symbol.t_RParen, Symbol.n_Statement ), t -> {
            Expression guard = (Expression)t.get(2);
            Statement body = (Statement)t.get(4);
            return new IfNode( Symbol.n_If, guard, body );
        });

        // n_Else -> t_Else n_Statement
        g.addRuleWithReduceFunction( Symbol.n_Else, List.of( Symbol.t_Else, Symbol.n_Statement ), t -> {
            Statement body = (Statement)t.get(1);
            return new ElseNode( Symbol.n_Else, body );
        });
    }

    private static void init_scope_capture() {

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
    }

    private static void init_types() {
        // n_User_Type -> t_LCurly n_Field_List t_RCurly
        // g.addRuleWithReduceFunction( Symbol.n_User_Type, List.of( Symbol.t_LCurly, Symbol.n_Field_List, Symbol.t_RCurly ), t -> {
        //     Field f = (Field)t.get(1);
        //     return new UserType(Symbol.n_User_Type, f);
        // });
        
        // n_User_Type -> t_Identifier
        g.addRuleWithReduceFunction( Symbol.n_User_Type, List.of( Symbol.t_Identifier ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            // id.term = Symbol.n_User_Type;
            Type typ = stored_user_types.get(id.name);
            if ( typ == null ) 
                return new AliasType( Symbol.n_User_Type, id.matchInfo );
            else {
                typ.term = Symbol.n_User_Type;
                return typ;
                // return typ.clone( Symbol.n_User_Type );
            }
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
            if ( t.get(2) instanceof Field ) {
                FieldList list = new FieldList( Symbol.n_Field_List );

                Declaration decl1 = (Declaration)t.get(0);
                Field f1 = new Field( Symbol.n_Field_List, decl1 );

                list.push( f1 );

                Field f2 = (Field)t.get(2);
                
                list.push( f2 );

                return list;
            } else { // FieldList
                FieldList list = (FieldList)t.get(2);

                Declaration decl = (Declaration)t.get(0);
                Field f = new Field( Symbol.n_Field_List, decl );

                list.push_front( f );
                
                return list;
            }
            
        });
        
        // n_Field_List -> n_Field t_Semicolon
        g.addRuleWithReduceFunction( Symbol.n_Field_List, List.of( Symbol.n_Field, Symbol.t_Semicolon ), t -> {
            return new Field( Symbol.n_Field_List, (Declaration)t.get(0) );
        });
        
        // n_Field -> t_Identifier t_Colon n_Standard_Type
        g.addRuleWithReduceFunction( Symbol.n_Field, List.of( Symbol.t_Identifier, Symbol.t_Colon, Symbol.n_Standard_Type ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            StandardType type = (StandardType)t.get(2);
            
            return new Declaration( Symbol.n_Field, id, type, DeclarationKind.FIELD );
        });

        // n_Field -> t_Identifier t_Colon t_Times n_Standard_Type
        g.addRuleWithReduceFunction( Symbol.n_Field, List.of( Symbol.t_Identifier, Symbol.t_Colon, Symbol.t_Times, Symbol.n_Standard_Type ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            StandardType type = (StandardType)t.get(3);
            
            return new Declaration( Symbol.n_Field, id, type, DeclarationKind.HEAP );
        });


        // n_Field -> t_Identifier t_Colon n_User_Type
        g.addRuleWithReduceFunction( Symbol.n_Field, List.of( Symbol.t_Identifier, Symbol.t_Colon, Symbol.n_User_Type ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            
            if ( t.get(2) instanceof LexIdent ) {
                
                Type type = stored_user_types.get(((LexIdent)t.get(2)).name);
                
                if ( type == null ) {
                    return new Declaration( Symbol.n_Field, id, new UnknownType(Symbol.n_Field, (LexIdent)t.get(2) ), DeclarationKind.FIELD );
                }

                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Field, id, type, DeclarationKind.FIELD );
                
            } else if ( t.get(2) instanceof Field ) {

                Field fields = (Field)t.get(2);
                UserType type = new UserType(Symbol.n_Declaration_Type, id, fields );
                
                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Field, id, type, DeclarationKind.FIELD );

            } else if ( t.get(2) instanceof UserType ) {
                UserType type = (UserType)t.get(2);
                return new Declaration( Symbol.n_Field, id, type, DeclarationKind.FIELD );
            } else {
                System.out.println( t.get(2));
                AST_Printer printer = new AST_Printer();
                Type typ = (Type)t.get(2);
                typ.accept(printer);
                System.out.println(printer.get_ascii());
            }
            
            System.out.println( "Something bad happend in parsin n_Field -> t_Identifier t_Colon n_User_Type" );
            System.exit(-1);
            return null;
        });

        // n_Field -> t_Identifier t_Colon t_Times n_User_Type
        g.addRuleWithReduceFunction( Symbol.n_Field, List.of( Symbol.t_Identifier, Symbol.t_Colon, Symbol.t_Times, Symbol.n_User_Type ), t -> {
            LexIdent id = (LexIdent)t.get(0);
            
            if ( t.get(3) instanceof LexIdent ) {
                
                Type type = stored_user_types.get(((LexIdent)t.get(3)).name);
                
                if ( type == null ) {
                    return new Declaration( Symbol.n_Field, id, new UnknownType(Symbol.n_Field, (LexIdent)t.get(3) ), DeclarationKind.FIELD );
                }

                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Field, id, type, DeclarationKind.HEAP );
                
            } else if ( t.get(3) instanceof Field ) {

                Field fields = (Field)t.get(3);
                UserType type = new UserType(Symbol.n_Declaration_Type, id, fields );
                
                stored_user_types.putIfAbsent( id.matchInfo.str(), type );
                
                return new Declaration( Symbol.n_Field, id, type, DeclarationKind.HEAP );

            } else if ( t.get(3) instanceof UserType ) {
                UserType type = (UserType)t.get(3);
                return new Declaration( Symbol.n_Field, id, type, DeclarationKind.HEAP );
            } else {
                System.out.println( t.get(3));
                AST_Printer printer = new AST_Printer();
                Type typ = (Type)t.get(3);
                typ.accept(printer);
                System.out.println(printer.get_ascii());
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
            if ( t.get(0) instanceof Field ) {
                Field f = (Field)t.get(0);
                return new UserType( Symbol.n_Any_Type, f );
            } else {
                Type st = (Type)t.get(0);
                st.term = Symbol.n_Any_Type;
                return st;
            }
        });

        // n_Standard_Type -> t_Type_Int
        g.addRuleWithReduceFunction( Symbol.n_Standard_Type, List.of( Symbol.t_Type_Int ), t -> {
            return new StandardType( Symbol.n_Standard_Type, (LexType)t.get(0) );
        });
        // n_Standard_Type -> t_Type_Double
        g.addRuleWithReduceFunction( Symbol.n_Standard_Type, List.of( Symbol.t_Type_Double ), t -> {
            return new StandardType( Symbol.n_Standard_Type, (LexType)t.get(0)  );
        });
        // n_Standard_Type -> t_Type_Bool
        g.addRuleWithReduceFunction( Symbol.n_Standard_Type, List.of( Symbol.t_Type_Bool ), t -> {
            return new StandardType( Symbol.n_Standard_Type, (LexType)t.get(0)  );
        });
        // n_Standard_Type -> t_Type_String
        g.addRuleWithReduceFunction( Symbol.n_Standard_Type, List.of( Symbol.t_Type_String ), t -> {
            return new StandardType( Symbol.n_Standard_Type, (LexType)t.get(0)  );
        });
        // n_Standard_Type -> t_Type_Void
        g.addRuleWithReduceFunction( Symbol.n_Standard_Type, List.of( Symbol.t_Type_Void ), t -> {
            return new StandardType( Symbol.n_Standard_Type, (LexType)t.get(0)  );
        });
    }

    private static void init_guards() {

        // n_Expression -> n_Equals
        // g.addRuleWithReduceFunction( Symbol.n_Expression, List.of( Symbol.n_Equals ), t -> {
        //     return new Expression( Symbol.n_Expression, (ASTNode)t.get(0) );
        // });

        // n_Equals -> n_Expression t_Check_Equal n_Expression 
        g.addRuleWithReduceFunction( Symbol.n_Expression, List.of( Symbol.n_Expression_Arithmetic, Symbol.t_Check_Equal, Symbol.n_Expression_Arithmetic ), t -> {
            return new Expression( Symbol.n_Expression, new BinaryOperation( Symbol.n_Equals, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.EQUALS ) );
        });

        // n_Equals -> n_Expression t_Check_Not_Equal n_Expression 
        g.addRuleWithReduceFunction( Symbol.n_Expression, List.of( Symbol.n_Expression_Arithmetic, Symbol.t_Check_Not_Equal, Symbol.n_Expression_Arithmetic ), t -> {
            return new Expression( Symbol.n_Expression, new BinaryOperation( Symbol.n_Equals, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.NOT_EQUALS ) );
        });

    }

    private static void init_arithmetic() {
        // --[[ Arithmetic ]]--

        // n_Expression -> n_Expression_Arithmetic
        g.addRuleWithReduceFunction( Symbol.n_Expression, List.of( Symbol.n_Expression_Arithmetic ), t -> {
            return new Expression( Symbol.n_Expression, (ASTNode)t.get(0) );
        });

        // --[[ PLUS, MINUS ]]--

        // n_Expression_Arithmetic -> n_Term t_Plus n_Expression_Arithmetic
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Term, Symbol.t_Plus, Symbol.n_Expression_Arithmetic ), t -> {
            return new BinaryOperation( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.PLUS );
        });

        // n_Expression_Arithmetic -> n_Term _Minus n_Expression_Arithmetic
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Term, Symbol.t_Minus, Symbol.n_Expression_Arithmetic ), t -> {
            return new BinaryOperation( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.MINUS );
        });

        // n_Expression_Arithmetic -> n_Term
        g.addRuleWithReduceFunction( Symbol.n_Expression_Arithmetic, List.of( Symbol.n_Term ), t -> {
            return new Expression( Symbol.n_Expression_Arithmetic, (ASTNode)t.get(0) );
        });


        // --[[ TIMES, DIVISION ]]--

        // n_Term -> n_Value n_Term t_Times t_Term
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Value, Symbol.t_Times, Symbol.n_Term ), t -> {
            return new BinaryOperation( Symbol.n_Term, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.TIMES );
        });

        // n_Term -> n_Value t_Division n_Term
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Value, Symbol.t_Division, Symbol.n_Term ), t -> {
            return new BinaryOperation( Symbol.n_Term, (ASTNode)t.get(0), (ASTNode)t.get(2), (ASTNode)t.get(1), BinaryOperationKind.DIVISION );
        });

        // n_Term -> n_Value
        g.addRuleWithReduceFunction( Symbol.n_Term, List.of( Symbol.n_Value ), t -> {
            return new Expression( Symbol.n_Term, (ASTNode)t.get(0) );
        });

        // --[[ UNARY MINUS ]]-- do better. quick solution

        // n_Value -> t_Minus n_Factor
        g.addRuleWithReduceFunction( Symbol.n_Value, List.of( Symbol.t_Minus, Symbol.n_Factor ), t -> {
            return new BinaryOperation( Symbol.n_Value, new LexLiteral(new MatchInfo("0", -1, -1), TokenType.INT_LITERAL), (ASTNode)t.get(1), new LexOperator( new MatchInfo("-", -1, -1), TokenType.MINUS ), BinaryOperationKind.MINUS );
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

        // n_Factor -> n_NestedField
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.n_Nested_Field ), t -> {
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

        // n_Literal -> t_BoolLiteral
        g.addRuleWithReduceFunction( Symbol.n_Literal, List.of( Symbol.t_BoolLiteral ), t -> {
            return new Expression( Symbol.n_Literal, (ASTNode)t.get(0) );
        } );

        // n_Literal -> t_StringLiteral
        g.addRuleWithReduceFunction( Symbol.n_Literal, List.of( Symbol.t_StringLiteral ), t -> {
            return new Expression( Symbol.n_Literal, (ASTNode)t.get(0) );
        } );
    }

    private static void init_functions() {
        // --[[ Functions ]]--
        
        // n_Statement -> t_Return n_Expression t_Semicolon
        g.addRuleWithReduceFunction( Symbol.n_Statement, List.of( Symbol.t_Return, Symbol.n_Expression, Symbol.t_Semicolon ), t -> {
            Expression ret = (Expression)t.get(1);
            return new Statement( Symbol.n_Statement, ret, StatementKind.RETURN );
        });

        // n_Factor -> n_Function_Call
        g.addRuleWithReduceFunction( Symbol.n_Factor, List.of( Symbol.n_Function_Call ), t -> {
            FunctionCall function_name = (FunctionCall)t.get(0);
            return new Expression( Symbol.n_Factor, function_name );
        });

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
        // n_Arg -> n_Expression
        g.addRuleWithReduceFunction( Symbol.n_Arg, List.of( Symbol.n_Expression ), t -> {
            return new Argument( Symbol.n_Arg, (ASTNode)t.get(0), ArgumentKind.EXPRESSION );
        });

        // n_Assignment_Function -> n_Function_Signature t_LCurly n_Statement_List t_RCurly
        g.addRuleWithReduceFunction( Symbol.n_Assignment_Function, List.of( Symbol.n_Function_Signature, Symbol.t_LCurly, Symbol.n_StatementList, Symbol.t_RCurly ), t -> {
            FunctionType func = (FunctionType)t.get(0);
            Statement body = (Statement)t.get(2);
            if ( func.parameters.isPresent() ) {
                if ( func.return_type.isPresent() )
                    return new FunctionType( Symbol.n_Assignment_Function, func.parameters, func.return_type, Optional.of(body) );
                else
                   return new FunctionType( Symbol.n_Assignment_Function, func.parameters, Optional.empty(), Optional.of(body) );
            } else {
                if ( func.return_type.isPresent() )
                    return new FunctionType( Symbol.n_Assignment_Function, Optional.empty(), func.return_type, Optional.of(body) );
                else
                   return new FunctionType( Symbol.n_Assignment_Function, Optional.empty(), Optional.empty(), Optional.of(body) );
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
                return new FunctionType( Symbol.n_Function_Signature, func.parameters, Optional.of(return_type), Optional.empty() );
            else
                return new FunctionType( Symbol.n_Function_Signature, Optional.empty(), Optional.of(return_type), Optional.empty() );
        });

        // Function with implicit return type of 'void'
        // n_Function_Signature -> n_Function_Param_Signature
        g.addRuleWithReduceFunction( Symbol.n_Function_Signature, List.of( Symbol.n_Function_Param_Signature ), t -> {
            FunctionType func = (FunctionType)t.get(0);
            
            if ( func.parameters.isPresent() )
                return new FunctionType( Symbol.n_Function_Signature, func.parameters, Optional.empty(), Optional.empty() ); // Do std.void
            else
                return new FunctionType( Symbol.n_Function_Signature, Optional.empty(), Optional.empty(), Optional.empty() ); // Do std.void
        });

        // n_Function_Param_Signature -> t_LParen n_Parameter_List t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Function_Param_Signature, List.of( Symbol.t_LParen, Symbol.n_Parameter_List, Symbol.t_RParen ), t -> {
            Parameter param_list = (Parameter)t.get(1);
            return new FunctionType( Symbol.n_Function_Param_Signature, Optional.of(param_list), Optional.empty(), Optional.empty() );
        });

        // n_Function_Param_Signature -> t_LParen t_RParen
        g.addRuleWithReduceFunction( Symbol.n_Function_Param_Signature, List.of( Symbol.t_LParen, Symbol.t_RParen ), t -> {
            return new FunctionType( Symbol.n_Function_Param_Signature, Optional.empty(), Optional.empty(), Optional.empty() );
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
    }

}
