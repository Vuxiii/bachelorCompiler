package com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.vuxiii.compiler.Error.Error;
import com.vuxiii.compiler.Lexer.Tokens.PrimitiveType;
import com.vuxiii.compiler.Lexer.Tokens.TokenType;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexLiteral;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexOperator;
import com.vuxiii.compiler.Parser.Nodes.Argument;
import com.vuxiii.compiler.Parser.Nodes.ArgumentList;
import com.vuxiii.compiler.Parser.Nodes.Assignment;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperation;
import com.vuxiii.compiler.Parser.Nodes.BinaryOperationKind;
import com.vuxiii.compiler.Parser.Nodes.Declaration;
import com.vuxiii.compiler.Parser.Nodes.DeclarationKind;
import com.vuxiii.compiler.Parser.Nodes.Expression;
import com.vuxiii.compiler.Parser.Nodes.Field;
import com.vuxiii.compiler.Parser.Nodes.FunctionCall;
import com.vuxiii.compiler.Parser.Nodes.IfElseNode;
import com.vuxiii.compiler.Parser.Nodes.IfList;
import com.vuxiii.compiler.Parser.Nodes.IfNode;
import com.vuxiii.compiler.Parser.Nodes.NestedField;
import com.vuxiii.compiler.Parser.Nodes.Print;
import com.vuxiii.compiler.Parser.Nodes.PrintKind;
import com.vuxiii.compiler.Parser.Nodes.Root;
import com.vuxiii.compiler.Parser.Nodes.ScopeNode;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.Parser.Nodes.StatementKind;
import com.vuxiii.compiler.Parser.Nodes.SymbolNode;
import com.vuxiii.compiler.Parser.Nodes.Types.FunctionType;
import com.vuxiii.compiler.Parser.Nodes.Types.RecordType;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Visitor;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;
import com.vuxiii.compiler.VisitorPattern.Visitors.CodeGeneration.StringCollection.StringNode;
import com.vuxiii.compiler.VisitorPattern.Visitors.Debug.AST_Printer;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.AST_SymbolCollector;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.HeapLayout;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Symbols;

public class AST_StackMachine extends Visitor {
    
    boolean has_been_computed = false;


    public LinkedList<Instruction> code = new LinkedList<>();

    static public Map<String, FunctionBlock> functions = new HashMap<>();

    static public Map<Print, StringNode> strings;

    private int function_depth = 0;

    private IfState if_state = IfState.NONE;

    Set<String> parameters = new HashSet<>();

    Symbols current_scope;

    private String end_of_body_label = "";

    public AST_StackMachine() {}

    private AST_StackMachine( Symbols scope  ) {
        this.parameters = scope.get_parameters();
        this.current_scope = scope;
    }

    private void function_assembler( Assignment function ) {
        String label = function.name();
        FunctionBlock fb = functions.get( label );

        current_scope = ((SymbolNode)function.parent.get()).scope;


        fb.push( new Instruction( Opcode.LABEL, Arguments.from_label( label ) ) );

        fb.push( new Instruction( Opcode.SETUP_STACK ) );

        fb.push( initialize_scope( (SymbolNode)function.parent.get() ) );


        parameters = current_scope.get_parameters();

        // System.out.println( parameters );

        if ( strings == null ) {
            System.out.println( "WTYARTYSF" );
            System.exit(-1);
        }
        AST_StackMachine stackMachine = new AST_StackMachine( current_scope ); // Insert internal functions here.

        function.value.accept( stackMachine );

        fb.instructions.addAll( stackMachine.code );

        if ( ((FunctionType)function.value).return_type.isEmpty() )
            fb.push( new Instruction( Opcode.CALL, Arguments.from_label( "release_scope_header" ) ) );

        fb.push( new Instruction( Opcode.RESTORE_STACK ) );
        fb.push( new Instruction( Opcode.RETURN ) );


    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void init( Root root ) {
        strings = root.strings;
        if ( strings == null ) {
            System.out.println( "WTF" );
            System.exit(-1);
        }
        // Initialize all the functions

        for ( Assignment func : root.functions ) { 
            String label = func.name();
            FunctionBlock fb = new FunctionBlock( label, func );
            functions.put( label, fb );
        }

        for ( Assignment func : root.functions ) { 
            function_assembler( func );
        }

        // Make room for our main function's variables!
        // 1 is for the stackpointer layout
        int total_variables_in_main = 1 + ((SymbolNode)root.node).scope.get_variables().size();
        int total_offset = total_variables_in_main*8;

        Operand left = new Operand( total_offset, AddressingMode.IMMEDIATE );
        Operand right = new Operand( Register.RSP, AddressingMode.REGISER );
        Arguments args = new Arguments( List.of(left, right, right) );
        Instruction ins = new Instruction( Opcode.MINUS, args, new Comment( "Making room for local variables in main scope!" ) );
        
        push( ins );

        push( new Instruction( Opcode.CALL, Arguments.from_label( "initialize_heap" ) ) );

        push( initialize_scope( (SymbolNode)root.node) );
        push( initialize_heap_layouts() );
        
    }   

    private List<Instruction> initialize_heap_layouts() {
        // TODO! Ensure that this also works for functions!!!!!
        List<Instruction> li = new ArrayList<>();
        for ( HeapLayout layout : HeapLayout.heap_layouts.values() ) {
            String layout_name = layout.name;
            // Insert size -> rdi
            
            push( Instruction.comment("Number of Bitfields"));
            push( new Instruction( Opcode.MOVE, new Arguments( List.of(
                Operand.from_long( layout.bitfields().size() ),
                Operand.from_register( Register.RDI, AddressingMode.REGISER )
            ) )));

            // Push the bitfields on the stack

            // System.out.println( layout );
            push( Instruction.comment("Bitfields"));
            for ( long field : layout.bitfields() ) {
                push( new Instruction( Opcode.PUSH, Arguments.from_long( field ) ));
            }

            // Load rsp -> rsi
            Operand rsp = Operand.from_register( Register.RSP, AddressingMode.DIRECT_OFFSET );
            rsp.offset = 0;
            push( new Instruction( Opcode.LEA, new Arguments( List.of(
                rsp,
                Operand.from_register( Register.RSI, AddressingMode.REGISER )
            ))));

            // call new_layout
            push( new Instruction( Opcode.CALL, Arguments.from_label( "new_layout" ) ) );
            
            // add size -> rsp
            push( new Instruction( Opcode.ADD, new Arguments( List.of( 
                Operand.from_long( layout.bitfields().size()),
                Operand.from_register( Register.RSP, AddressingMode.REGISER )
            ))));
            
            // move rax -> layoutspace.
            push( new Instruction( Opcode.MOVE, new Arguments( List.of(
                Operand.from_register(Register.RAX, AddressingMode.REGISER),
                Operand.from_label( layout_name )
            ))));
        }
        return li;
    }

    private List<Instruction> initialize_scope( SymbolNode symbol ) {
        List<Instruction> li = new ArrayList<>();
        int total_pointers = symbol.stack_frame.pointer_offsets.size();

        li.add( new Instruction( Opcode.MOVE, new Arguments( List.of(
            Operand.from_int( total_pointers ),
            Operand.from_register( Register.RDI, AddressingMode.REGISER )
        ))));

        Operand scope_header = Operand.from_register( Register.RBP, AddressingMode.DIRECT_OFFSET );
        scope_header.offset = -1;
        li.add( new Instruction( Opcode.LEA, new Arguments( List.of( 
            scope_header,
            Operand.from_register( Register.RSI, AddressingMode.REGISER )
        ))));

        // Push the bitfield
        int total_fields = 0;

        for ( long field : symbol.stack_frame.bitfields() ) {
            li.add( new Instruction( Opcode.PUSH, Arguments.from_long( field ) ) );
            total_fields++;
        }
        
        Operand bitfield = Operand.from_register( Register.RSP, AddressingMode.DIRECT_OFFSET );
        bitfield.offset = 0;
        li.add( new Instruction( Opcode.LEA, new Arguments( List.of( 
            bitfield,
            Operand.from_register( Register.RDX, AddressingMode.REGISER )
        ))));

        
        li.add( new Instruction( Opcode.CALL, Arguments.from_label( "new_scope_header" ) ) );

        li.add( new Instruction( Opcode.ADD, new Arguments( List.of (
            Operand.from_int( total_fields ),
            Operand.from_register( Register.RSP, AddressingMode.REGISER )
        ))));
        return li;
    }


    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void add_release_scope_header( Statement ret ) {
        if ( ret.kind.equals(StatementKind.RETURN) == false ) return;
        // System.out.println( "depth " + function_depth );
        if ( function_depth != 0 ) return;

        AST_Printer prins = new AST_Printer();
        ret.accept( prins );
        // System.out.println( prins.get_ascii() ); 

        push( new Instruction( Opcode.CALL, Arguments.from_label( "release_scope_header" ) ) );
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void add_return( Statement ret ) {
        if ( ret.kind.equals(StatementKind.RETURN) == false ) return;
        // System.out.println( "depth " + function_depth );
        if ( function_depth != 0 ) return;

        AST_Printer prins = new AST_Printer();
        ret.accept( prins );
        // System.out.println( prins.get_ascii() ); 

        push( new Instruction( Opcode.POP, Arguments.from_register( Register.RAX ) ) );
        push( new Instruction( Opcode.RETURN ) );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void literal( LexLiteral leaf_literal ) {
        if ( function_depth != 0 ) return;
        if ( leaf_literal.literal_type == PrimitiveType.STRING ) return;

        push( new Instruction( Opcode.PUSH, Arguments.from_literal( leaf_literal ), new Comment( "Pushing value " + leaf_literal.val ) ) );

    }

    @VisitorPattern( when = VisitOrder.AFTER_CHILD, order = 1 )
    public void if_list_insert_jumps( IfList if_list ) {
        // System.out.println( "IN IF LIST AFTER CHILD!!!!!!");
        if ( function_depth != 0 ) return;
        push( new Instruction( Opcode.JUMP, Arguments.from_label( if_list.end_of_ifs ) ) );
        if ( end_of_body_label.length() == 0 ) return;
        push( new Instruction( Opcode.LABEL, Arguments.from_label( end_of_body_label ) ) );
        end_of_body_label = "";
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void if_list_insert_label( IfList if_list ) {
        if ( function_depth != 0 ) return;
        push( new Instruction( Opcode.LABEL, Arguments.from_label( if_list.end_of_ifs ) ) );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void if_statement( IfNode if_node ) {
        if ( function_depth != 0 ) return;
        if_state = IfState.ENTER_GUARD;
    }
    
    @VisitorPattern( when = VisitOrder.BEFORE_CHILD, order = 1 )
    public void if_statement_before_child( IfNode if_node ) {
        if ( function_depth != 0 ) return;
        switch (if_state) {
            case ENTER_BODY: {

            } break;
            case EXIT_BODY: {

            } break;
            case ENTER_GUARD: {

            } break;
            case EXIT_GUARD: {
                if ( if_node.guard.node instanceof BinaryOperation && 
                    (((BinaryOperation)if_node.guard.node).kind == BinaryOperationKind.EQUALS || ((BinaryOperation)if_node.guard.node).kind == BinaryOperationKind.NOT_EQUALS ) ) {
                    BinaryOperation binop = (BinaryOperation)if_node.guard.node;
                    LexOperator ope = (LexOperator)binop.operator;
                    if ( ope.operator == TokenType.CHECK_EQUAL ) {
                        push( new Instruction( Opcode.JUMP_NOT_EQUAL, Arguments.from_label( if_node.end_of_body ) ) );
                    } else if ( ope.operator == TokenType.CHECK_NOT_EQUAL ) {
                        push( new Instruction( Opcode.JUMP_EQUAL, Arguments.from_label( if_node.end_of_body ) ) );
                    } else {
                        System.out.println( new Error( "StackMachine Error!", "Some unexpected operator in the guard of an if clause: " + ope.getPrintableName() ));
                        System.exit(-1);
                    }
                } else {
                    Operand left = Operand.from_register( Register.RAX, AddressingMode.REGISER );
                    push( new Instruction( Opcode.POP, Arguments.from_register( Register.RAX ) ) );
                    push( new Instruction( Opcode.COMPARE, new Arguments( List.of(left, Operand.from_int( 0 )) ) ) );
                    push( new Instruction( Opcode.JUMP_EQUAL, Arguments.from_label( if_node.end_of_body ) ) );
                }
                if_state = IfState.ENTER_BODY;
            } break;
            case NONE: {
                if_state = IfState.ENTER_GUARD;
            } break;
        }
    }
    
    @VisitorPattern( when = VisitOrder.AFTER_CHILD, order = 1 )
    public void if_statement_after_child( IfNode if_node ) {
        if ( function_depth != 0 ) return;
        switch (if_state) {
            case ENTER_BODY: {
                push( new Instruction( Opcode.JUMP, Arguments.from_label( if_node.end_of_body ) ) );
                if_state = IfState.NONE;
            } break;
            case EXIT_BODY: {
                end_of_body_label = if_node.end_of_body;
                if_state = IfState.NONE;
            } break;
            case ENTER_GUARD: {
                if_state = IfState.EXIT_GUARD;
            } break;
            case EXIT_GUARD: {

            } break;
        
            case NONE: {
                if_state = IfState.ENTER_GUARD;
            } break;
        }
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void if_guard_enter( Expression guard ) {
        if ( function_depth != 0 ) return;
        if ( if_state != IfState.ENTER_GUARD ) return; // Ensure that we actually are in an if block

        if ( guard.node instanceof LexIdent ) {
            // Load the value
            LexIdent id = (LexIdent)guard.node;
           push( _load_var(id, new Operand( Register.RAX, AddressingMode.REGISER ) ) );
        } else if ( guard.node instanceof LexLiteral ) {
            // Load the literal
            LexLiteral lit = (LexLiteral)guard.node;
            push( new Instruction( Opcode.PUSH, Arguments.from_literal( lit ), new Comment( "Pushing value: " + lit.val ) ) );
            push( new Instruction( Opcode.POP, Arguments.from_register( Register.RAX ), new Comment( "Loading value stored at the top of the stack" ) ) );
        }
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 2 )
    public void if_body_leave( Statement body ) {
        if ( function_depth != 0 ) return;
        if ( if_state != IfState.ENTER_BODY) return;

        if_state = IfState.EXIT_BODY;

     }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void single_if_insert_end_of_body( IfNode if_node ) {
        if ( function_depth != 0 ) return;

        if ( if_node.parent.get() instanceof IfList ) return;
        push( new Instruction( Opcode.LABEL, Arguments.from_label( end_of_body_label ) ) );
        end_of_body_label = "";
    
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void setup_function_arguments( Argument arg ) {
        if ( function_depth != 0 ) return;
        

        if ( arg.node instanceof Expression && ((Expression)arg.node).node instanceof LexIdent ) {

            push( _load_var((LexIdent)((Expression)arg.node).node, new Operand( Register.RAX, AddressingMode.REGISER ) ) );
            push( new Instruction( Opcode.PUSH, Arguments.from_register( Register.RAX ) ) );
        }
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void function_call( FunctionCall call ) {
        if ( function_depth != 0 ) return; // Wtf

        
        String func_name = call.func_name.name;
        // System.out.println( func_name );
        // System.out.println( functions );
        Assignment func = functions.get( func_name ).function;

        

        // push( new Instruction( Opcode.LOAD_VARIABLE, new Arguments( List.of( 
        //     Operand.from_string( func_name ),
        //     Operand.from_register( Register.RBX, AddressingMode.REGISER )
        // ))));

        // push( new Instruction( Opcode.CALL, new Arguments( List.of( 
        //     Operand.from_register( Register.RBX, AddressingMode.FUNCTION_POINTER )
        // )), new Comment( "Calling function " + func_name ) ) );
        push( new Instruction( Opcode.CALL, Arguments.from_label( func_name ), new Comment( "Calling function " + func_name ) ) );

        //TODO! Implement the return types.
        // Slå functionen op. Maybe it might be bigger in size.
        if ( ((FunctionType)func.value).return_type.isPresent() ) {
            push( new Instruction( Opcode.PUSH, Arguments.from_register( Register.RAX ) ) );
        }

    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void binop_expression( BinaryOperation binop ) {
        if ( function_depth != 0 ) return;
        
        if ( binop.right.isLeaf() && binop.right instanceof LexIdent ) {
            push( _load_var( (LexIdent)binop.right, new Operand( Register.RCX, AddressingMode.REGISER ) ) );
        } else {
            push( new Instruction( Opcode.POP, Arguments.from_register( Register.RCX ), new Comment( "Retrieving second argument" ) ) );
        }
        if ( binop.left.isLeaf() && binop.left instanceof LexIdent ) {
            push( _load_var( (LexIdent)binop.left, new Operand( Register.RBX, AddressingMode.REGISER ) ) );
        } else
            push( new Instruction( Opcode.POP, Arguments.from_register( Register.RBX ), new Comment( "Retrieving first argument" ) ) );
        
        
        Opcode opcode = null;
        Operand r1 = new Operand( Register.RCX, AddressingMode.REGISER ); 
        Operand r2 = new Operand( Register.RBX, AddressingMode.REGISER ); 
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
                push( new Instruction( opcode, new Arguments( List.of(r1, r2, target) ) ) );
                push( new Instruction( Opcode.PUSH, 
                                            new Arguments( List.of( target) ), 
                                            new Comment( "Storing computed value" ) ) );
                return;
            } 
            case DIVISION: {
                opcode = Opcode.DIV_INTEGER;
            } break;
            
            case MODULO: {
                opcode = Opcode.MODULO;
            } break;
            case EQUALS: case NOT_EQUALS: {
                opcode = Opcode.COMPARE;

                push( new Instruction( opcode, new Arguments(List.of(r1, r2) ) ) );

                return;
            } 
            default: {
                System.out.println( "Some unexpected binary operation happend." );
                System.exit(-1);    
            }break;
        }

        push( new Instruction( opcode,      new Arguments( List.of(r1, r2) ) ) );
        push( new Instruction( Opcode.MOVE, new Arguments( List.of(r2, target) ) ) );
        push( new Instruction( Opcode.PUSH, 
                                    new Arguments( List.of( target) ), 
                                    new Comment( "Storing computed value" ) ) );
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2 )
    public void heap_decl_struct( Declaration decl ) {
        if ( !(decl.type instanceof RecordType) ) return;

        RecordType record = (RecordType)decl.type;
        String prefix = decl.id.name + ".";
        for ( Field field : record.fields.fields ) {
            Declaration d = field.field;
            if ( d.kind == DeclarationKind.POINTER ) {
                _register_var_heap( prefix, d);
            }
        }
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void heap_declaration( Declaration decl ) {
        if ( decl.kind != DeclarationKind.POINTER ) return;
        if ( decl.parent.get() instanceof Field ) return;

        _register_var_heap("", decl);
    }

    private void _register_var_heap( String prefix, Declaration var ) {
        LexIdent id = var.id;

        String layout_name = var.heap_layout.name;
        
        int size = var.size();
        // How much space do we want for our heap_memory?
        push( new Instruction( Opcode.MOVE, new Arguments( List.of( 
            Operand.from_int( size ),
            Operand.from_register( Register.RDI, AddressingMode.REGISER ) ) ) ) );

        
        // The layout
        push( new Instruction( Opcode.MOVE, new Arguments( List.of( 
            Operand.from_string( layout_name ),
            Operand.from_register( Register.RSI, AddressingMode.REGISER ) ) ) ) );


        push( new Instruction( Opcode.CALL, Arguments.from_label("new_ptr_size")));

        // Move it to the correct location.

        push( new Instruction( Opcode.STORE_POINTER, new Arguments(List.of(
            Operand.from_string( prefix + id.name ),
            Operand.from_register(Register.RAX, AddressingMode.REGISER )
        ))) );
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void assign_value_to_var( Assignment assignment_node ) {
        if ( assignment_node.value instanceof FunctionType ) { function_depth--; return; }
        if ( function_depth != 0 ) return;


        if ( assignment_node.value instanceof LexIdent ) {
            LexIdent id = (LexIdent)assignment_node.value;
            if ( functions.containsKey(id.name) )
                functions.put(assignment_node.name(), functions.get(id.name) );
            push( _load_var( id, new Operand( Register.RAX, AddressingMode.REGISER ) ) );

        } else { // Literal
            push( new Instruction( Opcode.POP, 
                                Arguments.from_register( Register.RAX ), 
                                new Comment( "Fetching value" ) ) );
        }

        push( new Instruction( Opcode.STORE_VARIABLE, 
                                new Arguments( List.of(
                                    new Operand( assignment_node.name(), AddressingMode.IMMEDIATE ), 
                                    new Operand( Register.RAX, AddressingMode.REGISER )
                                ) ), 
                                new Comment( "Store in variable " + assignment_node.name() ) ) );
        
    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2 )
    public void assign_func_to_var( Assignment assignment_node ) {
        if ( !(assignment_node.value instanceof FunctionType) ) return;
        // if ( function_depth != 0 ) return;

        FunctionType func = (FunctionType) assignment_node.value;

        String name = assignment_node.id instanceof LexIdent ? ((LexIdent) assignment_node.id).name
                                                             : ((NestedField)assignment_node.id).name();


        push( new Instruction( Opcode.LEA, new Arguments( List.of( 
            Operand.from_label( name ),
            Operand.from_register(Register.RBX, AddressingMode.REGISER )
        ))));

        push( new Instruction( Opcode.STORE_VARIABLE, new Arguments( List.of( 
            new Operand( name, AddressingMode.IMMEDIATE ),
            Operand.from_register(Register.RBX, AddressingMode.REGISER )
        )) ));

        // System.exit(-1);

    }

    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 3 )
    public void increase_function_depth( Assignment function_def ) {
        if ( function_def.value instanceof FunctionType ) 
            function_depth++;
    }

    @VisitorPattern( when = VisitOrder.EXIT_NODE, order = 1 )
    public void print( Print print_node ) {
        if ( function_depth != 0 ) return;

        push( new Instruction( Opcode.COMMENT, Arguments.from_label( "Setup Print" ) ) );
        // Make some fancy string stuff.

        StringNode str_node = strings.get( print_node );
        Operand string_operand = new Operand( str_node.name, AddressingMode.IMMEDIATE );
        Operand string_target = new Operand( Register.RDI, AddressingMode.REGISER );
                    

        if ( print_node.kind == PrintKind.STRING ) {
            int len = str_node.stop_indicators.get(0);
            push( new Instruction( Opcode.MOVE, new Arguments( List.of(string_operand, string_target) ), new Comment( "Fetching value " + print_node.value ) ) );
            push( new Instruction( Opcode.MOVE, new Arguments( List.of(Operand.from_int(len ), Operand.from_register( Register.RSI, AddressingMode.REGISER )) ) ) );
            push( new Instruction( Opcode.MOVE, new Arguments( List.of(Operand.from_int(0 ), Operand.from_register( Register.RDX, AddressingMode.REGISER )) ) ) );
            push( new Instruction( Opcode.PRINT_STRING, Arguments.from_label( "%\n" ) ) ); 
        } else {
            // Do the fancy things
            
            List<Argument> args;

            if ( print_node.arg_list.get() instanceof Argument ) {
                Argument a = (Argument)print_node.arg_list.get();
                args = List.of( a );
            } else { 
                args = ((ArgumentList)print_node.arg_list.get()).args;
            }

            int offset = 0;
            int prev = 0;

            for ( int i = 0; i < args.size(); ++i ) {
                Argument a = args.get(i);
                Expression exp = (Expression)a.node;

                int len = str_node.stop_indicators.get(i) - prev;

                // Print the main part
                push( new Instruction( Opcode.MOVE, new Arguments( List.of(string_operand, string_target )), new Comment( "The input text" ) ) );
                push( new Instruction( Opcode.MOVE, new Arguments( List.of(Operand.from_int(len ), Operand.from_register( Register.RSI, AddressingMode.REGISER )) ) ) );
                push( new Instruction( Opcode.MOVE, new Arguments( List.of(Operand.from_int(prev ), Operand.from_register( Register.RDX, AddressingMode.REGISER )) ) ) );
                push( new Instruction( Opcode.PRINT_STRING, Arguments.from_label( "%\n" ) ) ); 


                // Print subs
                if ( exp.node instanceof LexLiteral && ((LexLiteral)exp.node).literal_type.equals( PrimitiveType.STRING ) ) {
                    Operand string_sub = new Operand( str_node.substitute_name, AddressingMode.IMMEDIATE );
                    int sub_len = ((LexLiteral)exp.node).val.length()-2;
                    push( new Instruction( Opcode.MOVE, new Arguments( List.of(string_sub, string_target) ), new Comment( "Fetching value " + print_node.value ) ) );
                    push( new Instruction( Opcode.MOVE, new Arguments( List.of(Operand.from_int(sub_len ), Operand.from_register( Register.RSI, AddressingMode.REGISER )) ) ) );
                    push( new Instruction( Opcode.MOVE, new Arguments( List.of(Operand.from_int(offset ), Operand.from_register( Register.RDX, AddressingMode.REGISER )) ) ) );

                    push( new Instruction( Opcode.PRINT_STRING, Arguments.from_label( "%\n" ) ) ); 

                    offset += sub_len + 1;
                } else /*if ( exp.node instanceof LexLiteral && ((LexLiteral)exp.node).literal_type.equals( PrimitiveType.INT ) ) */ {
                    /* This should just pop in reverse order into rdi. The values have already been calculated and are located on the stack - IN REVERSE ORDER!
                    if ( exp.node instanceof LexIdent ) {
                        push( _load_var( (LexIdent)exp.node, Operand.from_register( Register.RDI, AddressingMode.REGISER ) ) );
                    } else if ( exp.node instanceof NestedField ) {
                        push( _load_var( (NestedField)exp.node, Operand.from_register( Register.RDI, AddressingMode.REGISER ) ) );
                    } else if ( exp.node instanceof LexLiteral ) {
                        LexLiteral lit = (LexLiteral)exp.node;
                        if ( lit.literal_type == PrimitiveType.INT ) {
                            push( new Instruction( Opcode.MOVE, new Arguments( List.of( 
                                Operand.from_int( Integer.parseInt(lit.val) ),
                                Operand.from_register( Register.RDI, AddressingMode.REGISER )
                            ) ) ) );
                        }
                    } else if ( exp.node instanceof BinaryOperation ) {
                        push( new Instruction( Opcode.POP, Arguments.from_register( Register.RDI ) ) ); 

                    } else {
                        System.out.println( "What am I?");
                        AST_Printer p = new AST_Printer();
                        exp.accept( p );
                        System.out.println( p.get_ascii() );
                        push( new Instruction( Opcode.POP, Arguments.from_register( Register.RDI ) ) ); 
                    } */

                    Operand arg = Operand.from_register( Register.RSP, AddressingMode.DIRECT_OFFSET );
                    arg.offset = args.size() - i - 1;

                    push( new Instruction( Opcode.MOVE, new Arguments( List.of(
                        arg,
                        Operand.from_register( Register.RDI, AddressingMode.REGISER )
                    ))));

                    push( new Instruction( Opcode.PRINT_NUM, Arguments.from_label( "%\n" ) ) ); 

                }

                prev += len + 1;

            }

            push( new Instruction( Opcode.MOVE, new Arguments( List.of(
                string_operand, 
                string_target )), new Comment( "The input text" ) ) );
            push( new Instruction( Opcode.MOVE, new Arguments( List.of(
                Operand.from_int(str_node.stop_indicators.get(str_node.stop_indicators.size()-1) - prev ), 
                Operand.from_register( Register.RSI, AddressingMode.REGISER )) ) ) );
            push( new Instruction( Opcode.MOVE, new Arguments( List.of(
                Operand.from_int(prev ), 
                Operand.from_register( Register.RDX, AddressingMode.REGISER )) ) ) );
            push( new Instruction( Opcode.PRINT_STRING, Arguments.from_label( "%\n" ) ) ); 

        }

        push( new Instruction( Opcode.COMMENT, Arguments.from_label( "End Print" ) ) );
    }

    private void push( Instruction instruction ) {
        code.add( instruction );
    }

    private void push( List<Instruction> instruction ) {
        code.addAll( instruction );
    }

    /**
     * This method loads the given variable
     * @param id
     */
    private List<Instruction> _load_var( LexIdent id, Operand target ) {
        String var_name = id.name;
        Operand var = new Operand( var_name, AddressingMode.IMMEDIATE );
        Symbols scope = AST_SymbolCollector.current_scope(id);
        boolean target_is_parameter = scope.get_parameters().contains( var_name );

        return List.of(new Instruction( Opcode.LOAD_VARIABLE, 
                            new Arguments( List.of(var, target) ), 
                            new Comment( "Load variable " + var_name ),
                            target_is_parameter ) );
    }

    /**
     * This method loads the given variable
     * @param id
     */
    private List<Instruction> _load_var( NestedField id, Operand target ) {
        String var_name = id.name();
        Operand var = new Operand( var_name, AddressingMode.IMMEDIATE );
        Symbols scope = AST_SymbolCollector.current_scope(id);
        boolean target_is_parameter = scope.get_parameters().contains( var_name );

        return List.of(new Instruction( Opcode.LOAD_VARIABLE, 
                            new Arguments( List.of(var, target) ), 
                            new Comment( "Load variable " + var_name ),
                            target_is_parameter ) );
    }

}
