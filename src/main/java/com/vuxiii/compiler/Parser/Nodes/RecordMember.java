package com.vuxiii.compiler.Parser.Nodes;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Nodes.Types.Type;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Symbols;

public class RecordMember extends ASTNode {

    @VisitNumber( number = 1 ) public final LexIdent id;

    @VisitNumber( number = 2 ) public Type type;

    public boolean is_pointer = false;

    public Symbols layout;

    /**
     * This constructor constructs a RecordMember, where:
     * 
     * @param term
     * @param id Is the variable name
     * @param type The variable's type
     * @param is_pointer Determines whether it is a pointer 
     */
    public RecordMember( Term term, LexIdent id, Type type, boolean is_pointer ) {
        // Either variable or alias to std_type
        super( term ); 
        this.id = id;
    
        this.type = type;
        this.is_pointer = is_pointer;
    
        super.setup_ASTNodeQueue();
    }

    /**
     * This constructor constructs a RecordMember, where:
     * 
     * @param term
     * @param id Is the variable name
     * @param type The variable's type
     */
    public RecordMember( Term term, LexIdent id, Type type ) {
        // Either variable or alias to std_type
        super( term ); 
        this.id = id;
    
        this.type = type;
    
        super.setup_ASTNodeQueue();
    }

    @Override
    public String toString() {
        return  "Record " + id.name + ": " + type;
    }

    @Override
    public String getPrintableName() {
        return "RecordMember: " + (is_pointer ? "Pointer" : "");
    }

    public String get_parameter_form() {
        return id.name + ": " + type.simple_type_name();
    }
    
}
