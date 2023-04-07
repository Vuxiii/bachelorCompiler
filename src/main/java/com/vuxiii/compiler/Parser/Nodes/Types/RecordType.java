package com.vuxiii.compiler.Parser.Nodes.Types;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.Leaf.LexIdent;
import com.vuxiii.compiler.Parser.Nodes.FieldList;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class RecordType extends Type {
    @VisitNumber( number = 1 ) public LexIdent identifier;

    @VisitNumber( number = 2 ) public FieldList fields; // Figure out what these should be.

    public RecordType( Term term, LexIdent user_type, FieldList fields ) {
        super(term);
        this.identifier = user_type;
        this.fields = fields;
        super.setup_ASTNodeQueue();
    }

    @Override
    public String getPrintableName() {
        return "Record_Type";
    }
    
    @Override
    public String simple_type_name() {
        return identifier.name;
    }

    @Override
    public int physical_size() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'physical_size'");
    }

}
