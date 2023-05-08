package com.vuxiii.compiler.Parser.Nodes;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Nodes.Types.RecordType;
import com.vuxiii.compiler.Parser.Nodes.Types.Type;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.HeapLayout;
import com.vuxiii.compiler.VisitorPattern.Visitors.SymbolCollection.Symbols;

public class Declaration extends ASTNode {

    @VisitNumber( number = 1 ) public final LexIdent id;

    @VisitNumber( number = 2 ) public Type type;

    public DeclarationKind kind;

    public Symbols layout;

    public HeapLayout heap_layout;

    /**
     * This constructor constructs either a variable declaration or a type alias declaration, where:
     * 
     * @param term
     * @param id Is the variable name
     * @param type The variable's type
     * @param kind Determines whether it is a variable declaration (VARIABLE) or alias declaration (ALIAS_TYPE).
     */
    public Declaration( Term term, LexIdent id, Type type, DeclarationKind kind ) {
        // Either variable or alias to std_type
        super( term ); 
        this.id = id;
    
        this.type = type;
        this.kind = kind;
    
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return kind + " " + id.name + ": " + type;
    }

    @Override
    public String getPrintableName() {
        return "Declaration: " + kind;
    }

    public String get_parameter_form() {
        return id.name + ": " + type.simple_type_name();
    }

    public int size() {
        if ( type instanceof RecordType ) {
            return ((RecordType)type).fields.fields.size(); // call each fields size method
        } else {
            return 1; // std type
        }
    }
    
}
