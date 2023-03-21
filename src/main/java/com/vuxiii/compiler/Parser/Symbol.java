package com.vuxiii.compiler.Parser;

import com.vuxiii.LR.Records.NonTerminal;
import com.vuxiii.LR.Records.Terminal;

public class Symbol {
    
    // Used under parsing.
    public static final NonTerminal n_Root = new NonTerminal( "ROOT" );

    public static final NonTerminal n_Start = new NonTerminal( "START", true );
    
    public static final NonTerminal n_Expression = new NonTerminal( "EXPRESSION" );
    public static final NonTerminal n_ExpressionList = new NonTerminal( "EXPRESSION_LIST" );
    
    public static final NonTerminal n_Statement = new NonTerminal( "STATEMENT" );
    public static final NonTerminal n_StatementList = new NonTerminal( "STATEMENT_LIST" );
    
    public static final NonTerminal n_Declaration = new NonTerminal( "DECLARATION" );
    public static final NonTerminal n_Assignment = new NonTerminal( "ASSIGNMENT" );
    
    public static final NonTerminal n_Declaration_Function = new NonTerminal( "DECLARATION_FUNCTION" );
    public static final NonTerminal n_Assignment_Function = new NonTerminal( "ASSIGNMENT_FUNCTION" );

    public static final NonTerminal n_Function_Signature = new NonTerminal( "FUNCTION_SIGNATURE" );

    public static final NonTerminal n_Function_Call = new NonTerminal( "FUNCTION_CALL" );

    public static final NonTerminal n_Function_Param_Signature = new NonTerminal( "FUNCTION_PARAM_SIGNATURE" );
    public static final NonTerminal n_Parameter_List = new NonTerminal( "PARAMETER_LIST" );
    public static final NonTerminal n_Parameter = new NonTerminal( "PARAMETER" );


    public static final NonTerminal n_Declaration_Variable = new NonTerminal( "VARIABLE_DECLARATION" );
    public static final NonTerminal n_Declaration_Type = new NonTerminal( "TYPE_DECLARATION" );
    public static final NonTerminal n_Declaration_Type_Body = new NonTerminal( "TYPE_BODY_DECLARATION" );


    public static final NonTerminal n_Return_Type = new NonTerminal( "RETURN_TYPE" );

    public static final NonTerminal n_Any_Type = new NonTerminal( "ANY_TYPE" );

    public static final NonTerminal n_User_Type = new NonTerminal( "TYPE_USER" );
    public static final NonTerminal n_Field_List = new NonTerminal( "FIELD_LIST" );
    public static final NonTerminal n_Field = new NonTerminal( "FIELD" );

    public static final NonTerminal n_If = new NonTerminal( "IF" );
    public static final NonTerminal n_Else = new NonTerminal( "ELSE" );
    public static final NonTerminal n_Elif = new NonTerminal( "ELIF_BLOCK" );
    public static final NonTerminal n_If_Blocks = new NonTerminal( "IF_ELSE_BLOCK" );

    public static final NonTerminal n_Print = new NonTerminal( "PRINT" );
    public static final NonTerminal n_Expression_Arithmetic = new NonTerminal( "ARITHMETIC_EXPRESSION" );

    // Arithmetic
    public static final NonTerminal n_Term = new NonTerminal( "TERM" );
    public static final NonTerminal n_Value = new NonTerminal( "VALUE" );
    public static final NonTerminal n_Factor = new NonTerminal( "FACTOR" );

    public static final NonTerminal n_Literal = new NonTerminal( "LITERAL" );
    public static final NonTerminal n_Standard_Type = new NonTerminal( "TYPE_STD" );


    // Scopes
    public static final NonTerminal n_Scope = new NonTerminal( "SCOPE" );
    public static final NonTerminal n_Scope_Block = new NonTerminal( "SCOPE_BLOCK" );
    public static final NonTerminal n_Capture = new NonTerminal( "CAPTURE" );

    // Function

    public static final NonTerminal n_Arg_List = new NonTerminal( "ARG_LIST" );
    public static final NonTerminal n_Arg = new NonTerminal( "ARG" );
    

    // Used under lexical analysis. Tokenization.
    public static final Terminal t_Plus = new Terminal( "'+'" );
    public static final Terminal t_Minus = new Terminal( "'-'" );
    public static final Terminal t_Times = new Terminal( "'*'" );
    public static final Terminal t_Division = new Terminal( "'/'" );
    public static final Terminal t_Equals = new Terminal( "'='" );
    public static final Terminal t_Identifier = new Terminal( "id" );
    public static final Terminal t_Integer = new Terminal( "integer" );
    public static final Terminal t_Double = new Terminal( "double" );
    public static final Terminal t_BoolLiteral = new Terminal( "bool" );

    public static final Terminal t_Type_Declare = new Terminal( "type_decl" );
    public static final Terminal t_Type_Int = new Terminal( "type_int" );
    public static final Terminal t_Type_Double = new Terminal( "type_double" );
    public static final Terminal t_Type_Bool = new Terminal( "type_bool" );
    public static final Terminal t_Type_String = new Terminal( "type_string" );

    public static final Terminal t_Semicolon = new Terminal( "';'" );
    public static final Terminal t_Colon = new Terminal( "':'" );
    public static final Terminal t_Dollar = new Terminal( "$", true );
    public static final Terminal t_Print = new Terminal( "'print'" );
    public static final Terminal t_LParen = new Terminal( "'('" );
    public static final Terminal t_RParen = new Terminal( "')'" );
    public static final Terminal t_LBracket = new Terminal( "'['" );
    public static final Terminal t_RBracket = new Terminal( "']'" );
    public static final Terminal t_LCurly = new Terminal( "'{'" );
    public static final Terminal t_RCurly = new Terminal( "'}'" );
    public static final Terminal t_Comma = new Terminal( "','" );
    public static final Terminal t_Dot = new Terminal( "'.'" );
    public static final Terminal t_If = new Terminal( "if" );
    public static final Terminal t_Else = new Terminal( "else" );

    public static final Terminal t_StringLiteral = new Terminal( "\"string_literal\"" );

    public static final Terminal t_Let = new Terminal( "let" );
    public static final Terminal t_Arrow_Right = new Terminal( "'->'" );
















}
