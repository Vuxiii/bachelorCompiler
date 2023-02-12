package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vuxiii.DFANFA.MatchInfo;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Parser.Nodes.Argument;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperation;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.FunctionCall;
import com.vuxiii.compiler.Parser.Nodes.Parameter;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.PrintKind;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Visitor;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection.StringNode;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Scope;

public class AST_StackMachine extends Visitor {
    
    private Map<String, Scope> scopes;

    public LinkedList<Instruction> code = new LinkedList<>();

    public Map<String, FunctionBlock> functions = new HashMap<>();

    public Map<String, StringNode> strings;

    private int function_depth = 0;

    Set<String> parameters = new HashSet<>();

    Scope current_scope = new Scope();

    public AST_StackMachine( List<Assignment> functions, Map<String, Scope> scopes, Map<String, StringNode> strings ) {
        this.strings = strings;
        this.scopes = scopes;
        
        for ( Assignment node : functions ) { 
            current_scope = scopes.get( node.id.name );
            function_assembler( node, scopes.get( node.id.name ) );
        }

        

        // Make room for our main function's variables!

        int total_variables_in_main = scopes.get("root").get_variables().size();
        int total_offset = total_variables_in_main*8;

        Operand left = new Operand( -total_offset, AddressingMode.IMMEDIATE );
        Operand right = new Operand( Register.RSP, AddressingMode.REGISER );
        Arguments args = new Arguments( left, right, right );
        Instruction ins = new Instruction( Opcode.MINUS, args, new Comment( "Making room for local variables in main scope!" ) );
        
        push( ins );
    }

    public AST_StackMachine( Scope scope, Map<String, StringNode> strings ) {
        this.parameters = scope.get_parameters();
        this.current_scope = scope;
    }

    private void function_assembler( Assignment function, Scope current_scope ) {
        String label = function.id.name;
        FunctionBlock fb = new FunctionBlock( label );


        

        fb.push( new Instruction( Opcode.LABEL, Arguments.from_label( label ) ) );

        fb.push( new Instruction( Opcode.SETUP_STACK ) );

        parameters = current_scope.get_parameters();

        System.out.println( parameters );


        AST_StackMachine stackMachine = new AST_StackMachine( current_scope, strings ); // Insert internal functions here.

        function.value.accept( stackMachine );

        fb.instructions.addAll( stackMachine.code );

        fb.push( new Instruction( Opcode.RESTORE_STACK ) );
        fb.push( new Instruction( Opcode.RETURN ) );

        functions.put( label, fb );

    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void literal( LexLiteral leaf_literal ) {
        if ( function_depth != 0 ) return;
        switch (leaf_literal.literal_type) {
            case DOUBLE: {
                push( new Instruction( Opcode.PUSH, Arguments.from_double( Double.parseDouble(leaf_literal.val) ), new Comment( "Pushing value " + leaf_literal.val ) ) );
            } break;
            case INT: {
                push( new Instruction( Opcode.PUSH, Arguments.from_int( Integer.parseInt(leaf_literal.val) ), new Comment( "Pushing value " + leaf_literal.val ) ) );
            } break;
            case STRING: {
                // WHat to do.......
            } break;
        
            default:
                break;
        }
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void function_call( FunctionCall call ) {
        if ( function_depth != 0 ) return;
        

        // Setup arguments here.
        push( new Instruction( Opcode.CALL, Arguments.from_label( call.func_name.name ), new Comment( "Calling function " + call.func_name.name ) ) );

    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void binop_expression( BinaryOperation binop ) {
        if ( function_depth != 0 ) return;
        
        if ( binop.right.isLeaf() && binop.right instanceof LexIdent ) {
            String var_name = ((LexIdent)binop.right).name;
            Operand var = new Operand( var_name, AddressingMode.IMMEDIATE );
            Operand target = new Operand( Register.RCX, AddressingMode.REGISER );
            boolean target_is_parameter = current_scope.get_parameters().contains( var_name );
            push( new Instruction( Opcode.LOAD_VARIABLE, 
                                    new Arguments( var, target, target ), 
                                    new Comment( "Load variable " + var_name ),
                                    target_is_parameter ) );
        } else {
            push( new Instruction( Opcode.POP, Arguments.from_register( Register.RCX ), new Comment( "Retrieving second argument" ) ) );
        }
        if ( binop.left.isLeaf() && binop.left instanceof LexIdent ) {
            String var_name = ((LexIdent)binop.left).name;
            Operand var = new Operand( var_name, AddressingMode.IMMEDIATE );
            Operand target = new Operand( Register.RBX, AddressingMode.REGISER );
            boolean target_is_parameter = current_scope.get_parameters().contains( var_name );

            push( new Instruction( Opcode.LOAD_VARIABLE, 
                                    new Arguments( var, target, target ), 
                                    new Comment( "Load variable " + var_name ),
                                    target_is_parameter ) );
        } else
            push( new Instruction( Opcode.POP, Arguments.from_register( Register.RBX ), new Comment( "Retrieving first argument" ) ) );
        
        
        Opcode opcode = null;
        Operand r1 = new Operand( Register.RBX, AddressingMode.REGISER ); 
        Operand r2 = new Operand( Register.RCX, AddressingMode.REGISER ); 
        Operand target = new Operand( Register.RAX, AddressingMode.REGISER ); 
        switch (binop.kind) {
            case PLUS: {
                opcode = Opcode.ADD;
            } break;
            case MINUS: {
                opcode = Opcode.MINUS;
            } break;
            
            case TIMES: {
                opcode = Opcode.MULT;
            } break;
            
            case DIVISION: {
                opcode = Opcode.DIV_INTEGER;
            } break;
            
            case MODULO: {
                opcode = Opcode.MODULO;
            } break;
        
            default: {
                System.out.println( "Some unexpected binary operation happend." );
                System.exit(-1);    
            }break;
        }

        push( new Instruction( opcode, 
                                    new Arguments( r1, r2, target ), 
                                    new Comment( target + " = " + binop  ) ) );
        push( new Instruction( Opcode.PUSH, 
                                    Arguments.from_operand( target ), 
                                    new Comment( "Storing computed value" ) ) );
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void assign_value_to_var( Assignment assignment_node ) {
        if ( assignment_node.value instanceof FunctionType ) { function_depth--; return; }
        if ( function_depth != 0 ) return;



        if ( assignment_node.value instanceof LexIdent ) {
            String var_name = ((LexIdent)assignment_node.value).name;
            Operand var_to_load = new Operand( var_name, AddressingMode.IMMEDIATE );
            boolean target_is_parameter = current_scope.get_parameters().contains( var_name );

            push( new Instruction( Opcode.LOAD_VARIABLE, 
                                    new Arguments( var_to_load, new Operand( Register.RAX, AddressingMode.REGISER ) ), 
                                    new Comment( "Load variable " + var_name ),
                                    target_is_parameter ) );
        } else { // Literal
            push( new Instruction( Opcode.POP, 
                                Arguments.from_register( Register.RAX ), 
                                new Comment( "Fetching value" ) ) );
        }

        
        push( new Instruction( Opcode.STORE_VARIABLE, 
                                new Arguments( new Operand( assignment_node.id.name, AddressingMode.IMMEDIATE ), new Operand( Register.RAX, AddressingMode.REGISER ) ), 
                                new Comment( "Store in variable " + assignment_node.id.name ) ) );
        
    }
    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void increase_function_depth( Assignment function_def ) {
        if ( function_def.value instanceof FunctionType ) 
            function_depth++;
    }

    // @VisitorPattern( when = VisitOrder.ENTER_NODE )
    // public void evaluate_argument( Argument arg ) {

    // }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void print( Print print_node ) {
        if ( function_depth != 0 ) return;


                                // Make some fancy string stuff.
        if ( print_node.kind == PrintKind.NORMAL ) {
            push( new Instruction( Opcode.POP, 
                                    Arguments.from_register( Register.RDI ), 
                                    new Comment( "Fetching value " + print_node.value ) ) );
            push( new Instruction( Opcode.PRINT, Arguments.from_label( "%\n" ) ) ); 
        }
        else {
            // Do the fancy things
            // Fetch the String -> rdi
            String str_literal = ((LexIdent)print_node.value).matchInfo.str();
            StringNode str_node = strings.get( str_literal );
            Operand string_operand = new Operand( str_node.name, AddressingMode.IMMEDIATE );
            Operand string_target = new Operand( Register.RDI, AddressingMode.REGISER );
            push( new Instruction( Opcode.MOVE, 
                                    new Arguments( string_operand, string_target ),
                                    new Comment( "The input text" ) ) );

            // Create the stop indicators -> rsi

            Operand string_stopindicator_buffer = new Operand( str_node.stop_name, AddressingMode.IMMEDIATE );
            Operand string_stopindicator_target = new Operand( Register.RSI, AddressingMode.REGISER );
            push( new Instruction( Opcode.LEA, 
                                    new Arguments( string_stopindicator_buffer, string_stopindicator_target),
                                    new Comment( "Loading the stopindicator's buffer address" ) ) );

            // Create the indicators
            for ( int i = 0; i < str_node.stop_indicators.size(); ++i ) {
                Operand stop_indicator = new Operand( str_node.stop_indicators.get(i), AddressingMode.IMMEDIATE );
                Operand rsi_offset = new Operand( Register.RSI, AddressingMode.DIRECT_OFFSET );
                rsi_offset.offset = i;

                push( new Instruction( Opcode.MOVE,
                                        new Arguments( stop_indicator, rsi_offset ),
                                        new Comment( "Making indicator stop" ) ) );
            }
            // Substitutes -> rdx

            Operand string_substitute_buffer = new Operand( str_node.substitute_name, AddressingMode.IMMEDIATE );
            Operand string_substitute_target = new Operand( Register.RDX, AddressingMode.REGISER );
            push( new Instruction( Opcode.LEA, 
                                    new Arguments( string_substitute_buffer, string_substitute_target),
                                    new Comment( "Loading the substitute's buffer address" ) ) );
            
            // Move the substitutes into the buffer 
            // for ( int i = 0; i < str_node.substitutes.size(); ++i ) {
            //     Operand substitute = new Operand( str_node.substitutes.get(i), AddressingMode.IMMEDIATE );
            //     Operand rdx_offset = new Operand( Register.RDX, AddressingMode.DIRECT_OFFSET );
            //     rdx_offset.offset = i;

            //     push( new Instruction( Opcode.MOVE,
            //                             new Arguments( substitute, rdx_offset ),
            //                             new Comment( "Loading the substitute" ) ) ); // These should probably be computed on the stack. Just before. So we should do pop!
            // }
            // Amount of substitues -> rcx

            Operand size = new Operand( str_node.substitutes.size(), AddressingMode.IMMEDIATE );
            Operand rcx = new Operand( Register.RCX, AddressingMode.REGISER );
            push( new Instruction( Opcode.MOVE, 
                                    new Arguments( size, rcx ),
                                    new Comment( "Amount of substitutes" ) ) );
            

            push( new Instruction( Opcode.PRINT ) );
        }
        // push( new Instruction( Opcode.PRINT, 
        //                         Arguments.from_register( Register.RAX ), 
        //                         new Comment( "Printing value of " + print_node.value ) ) );

    }

    private void push( Instruction instruction ) {
        code.add( instruction );
    }



}
