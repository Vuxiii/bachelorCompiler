package com.vuxiii.compiler.Parser.Nodes;

import java.util.Optional;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class UserType extends ASTNode {

    @VisitNumber( number = 1 ) public final Optional<Statement> field_list;


    public UserType( Term term ) {
        super( term );
        this.field_list = Optional.empty();
        super.setup_ASTNodeQueue();
    }

    public UserType( Term term, Statement field_list ) {
        super( term );
        this.field_list = Optional.of(field_list);
        super.setup_ASTNodeQueue();
    }

    public String toString() {
        return "ASTTokenUserType";
    }

    @Override
    public String getPrintableName() {
        return "User_Type";
    }
}
