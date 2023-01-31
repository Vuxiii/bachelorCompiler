package com.vuxiii.compiler.Parser.Nodes.Types;

import java.util.HashMap;
import java.util.Map;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.VisitorPattern.ASTNode;

public abstract class Type extends ASTNode {

    public Type(Term term) {
        super(term);
        super.setup_ASTNodeQueue();
    }
}
