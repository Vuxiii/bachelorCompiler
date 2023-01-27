package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexInt;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperation;
import com.vuxiii.compiler.VisitorPattern.Visitor;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;
import com.vuxiii.compiler.VisitorPattern.Visitors.ASTNode;

public class AST_StackMachine extends Visitor {
    

    public LinkedList<Instruction> code = new LinkedList<>();

    @VisitorPattern( when = VisitOrder.ENTER_NODE )
    public void literal_int( LexInt leaf_int ) {
        System.out.println( "Entering leaf: " + leaf_int );
        code.add( new Instruction( Opcode.PUSH, new OpcodeArgument( leaf_int ), new Comment( "Pushing value " + leaf_int.val ) ) );
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE )
    public void binop_expression( BinaryOperation binop ) {
        System.out.println( binop );
        
        switch (binop.kind) {
            case PLUS: {
                code.add( new Instruction( Opcode.POP, new OpcodeArgument( Register.RBX ) ) );
                code.add( new Instruction( Opcode.POP, new OpcodeArgument( Register.RCX ) ) );
                code.add( new Instruction( Opcode.ADD, new OpcodeArgument( Register.RBX, Register.RCX, Register.RAX ), new Comment( "RAX" + " = " + binop  ) ) );
                code.add( new Instruction( Opcode.PUSH, new OpcodeArgument( Register.RAX ) ) );
            } break;
            
            case MINUS: {
                code.add( new Instruction( Opcode.POP, new OpcodeArgument( Register.RBX ) ) );
                code.add( new Instruction( Opcode.POP, new OpcodeArgument( Register.RCX ) ) );
                code.add( new Instruction( Opcode.MINUS, new OpcodeArgument( Register.RBX, Register.RCX, Register.RAX ), new Comment( "RAX" + " = " + binop ) ) );
                code.add( new Instruction( Opcode.PUSH, new OpcodeArgument( Register.RAX ) ) );
            } break;
            
            case TIMES: {
                // code.add( new CodeSection( Operation. ) );

            } break;
            
            case DIVISION: {
                // code.add( new CodeSection( Operation.DIVISION ) );

            } break;
            
            case MODULO: {
                // code.add( new CodeSection( Operation.MODULO ) );

            } break;
        
            default: {
                System.out.println( "Some unexpected binary operation happend." );
                System.exit(-1);    
            }break;
        }

    }
}
