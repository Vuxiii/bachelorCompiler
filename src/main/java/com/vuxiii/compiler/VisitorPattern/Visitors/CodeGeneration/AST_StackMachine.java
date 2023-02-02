package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperation;
import com.vuxiii.compiler.Parser.Nodes.FunctionCall;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.VisitorPattern.Visitor;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_StackMachine extends Visitor {
    

    public LinkedList<Instruction> code = new LinkedList<>();

    public Map<String, FunctionBlock> functions = new HashMap<>();

    private int function_depth = 0;

    public AST_StackMachine( List<Assignment> functions ) {
        for ( Assignment node : functions ) { 
            function_assembler(node);
        }
    }

    private void function_assembler( Assignment root ) {
        String label = root.id.name;
        FunctionBlock fb = new FunctionBlock( label );

        AST_StackMachine stackMachine = new AST_StackMachine( List.of() ); // Insert internal functions here.

        fb.push( new Instruction( Opcode.LABEL, new Arguments( label ) ) );

        fb.push( new Instruction( Opcode.SETUP_STACK ) );

        root.value.accept( stackMachine );

        fb.instructions.addAll( stackMachine.code );

        fb.push( new Instruction( Opcode.RESTORE_STACK ) );
        fb.push( new Instruction( Opcode.RETURN ) );

        functions.put( label, fb );

    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void literal_int( LexLiteral leaf_int ) {
        if ( function_depth != 0 ) return;
        push( new Instruction( Opcode.PUSH, new Arguments( leaf_int ), new Comment( "Pushing value " + leaf_int.val ) ) );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void function_call( FunctionCall call ) {
        if ( function_depth != 0 ) return;
        

        // Setup arguments here.
        push( new Instruction( Opcode.CALL, new Arguments( call.func_name.name ), new Comment( "Calling function " + call.func_name.name ) ) );

    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void binop_expression( BinaryOperation binop ) {
        if ( function_depth != 0 ) return;
        // System.out.println( binop );
        
        if ( binop.right.isLeaf() && binop.right instanceof LexIdent )
            push( new Instruction( Opcode.LOAD_VARIABLE, new Arguments( ((LexIdent)binop.right).name, Register.RCX ), new Comment( "Load variable " + ((LexIdent)binop.right).name ) ) );
        else
            push( new Instruction( Opcode.POP, new Arguments( Register.RCX ), new Comment( "Retrieving second argument" ) ) );
        
        if ( binop.left.isLeaf() && binop.left instanceof LexIdent )
            push( new Instruction( Opcode.LOAD_VARIABLE, new Arguments( ((LexIdent)binop.left).name, Register.RBX ), new Comment( "Load variable " + ((LexIdent)binop.left).name ) ) );
        else
            push( new Instruction( Opcode.POP, new Arguments( Register.RBX ), new Comment( "Retrieving first argument" ) ) );
        
        
        Opcode opcode = null;
        Register r1 = Register.RBX; 
        Register r2 = Register.RCX; 
        Register target = Register.RAX;
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
                                    new Arguments( target ), 
                                    new Comment( "Storing computed value" ) ) );
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void assign_value_to_var( Assignment assignment_node ) {
        if ( assignment_node.value instanceof FunctionType ) { function_depth--; return; }
        if ( function_depth != 0 ) return;

        push( new Instruction( Opcode.POP, 
                                new Arguments( Register.RAX ), 
                                new Comment( "Fetching value" ) ) );
        push( new Instruction( Opcode.STORE_VARIABLE, 
                                new Arguments( Register.RAX, assignment_node.id.name ), 
                                new Comment( "Store in variable " + assignment_node.id.name ) ) );
        
    }
    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void increase_function_depth( Assignment function_def ) {
        if ( function_def.value instanceof FunctionType ) 
            function_depth++;
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void print( Print print_node ) {
        if ( function_depth != 0 ) return;
        if ( print_node.value instanceof LexIdent )
            push( new Instruction( Opcode.LOAD_VARIABLE, 
                                    new Arguments( ((LexIdent)print_node.value).name, Register.RAX ), 
                                    new Comment( "Loading value of " + print_node.value ) ) );
        else 
            push( new Instruction( Opcode.POP, 
                                    new Arguments( Register.RAX ), 
                                    new Comment( "Fetching value " + print_node.value ) ) );
                
        push( new Instruction( Opcode.PRINT, 
                                new Arguments( Register.RAX ), 
                                new Comment( "Printing value of " + print_node.value ) ) );

    }

    private void push( Instruction instruction ) {
        code.add( instruction );
    }



}
