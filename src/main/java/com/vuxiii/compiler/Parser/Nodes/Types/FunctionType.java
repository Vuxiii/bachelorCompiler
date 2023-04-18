package com.vuxiii.compiler.Parser.Nodes.Types;

import java.util.Optional;
import java.util.function.Function;

import com.vuxiii.LR.Records.Term;
import com.vuxiii.compiler.Lexer.Tokens.PrimitiveType;
import com.vuxiii.compiler.Parser.Nodes.Parameter;
import com.vuxiii.compiler.Parser.Nodes.Statement;
import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;

public class FunctionType extends Type {
    
    @VisitNumber( number = 1 ) public final Optional<Parameter> parameters;
    @VisitNumber( number = 2 ) public final Optional<Type> return_type;
    @VisitNumber( number = 3 ) public Optional<ASTNode> body;

    public FunctionType( Term term, Optional<Parameter> parameter_list, Optional<Type> return_type, Optional<ASTNode> body ) {
        super(term);
        
        this.parameters =  parameter_list;
        this.return_type = return_type;
        this.body = body;
        super.setup_ASTNodeQueue();
    }

    @Override
    public int hashCode() {
        int out = 0;
        Parameter current_param = null;
        int i = 1;
        if ( parameters.isPresent() ) {
            current_param = parameters.get();
            while ( current_param != null ) {
                out += i * 37 * current_param.hashCode();
                ++i;

                current_param = current_param.next.isPresent() ? current_param.next.get() : null;
            }
        }
        if ( return_type.isPresent() ) {
            out += i * 37 * return_type.get().hashCode();
        } else {
            out += i * 37 * PrimitiveType.VOID.hashCode();
        }
        return out;
    }

    @Override
    public boolean equals( Object other ) {
        if ( other == null ) return false;
        if ( !(other instanceof FunctionType) ) return false;
        FunctionType o = (FunctionType) other;

        if ( parameters.isPresent() ) {
            if ( o.parameters.isPresent() ) {
                if ( parameters.get().equals( o.parameters.get() ) == false ) return false;
            } else {
                return false;
            }
        } else if ( o.parameters.isPresent() ) {
            return false;
        }

        if ( return_type.isPresent() ) {
            if ( o.return_type.isPresent() ) {
                if ( return_type.get().equals( o.return_type.get() ) == false ) return false;
            } else {
                if ( (return_type.get() instanceof StandardType) && ((StandardType)return_type.get()).type.type.equals( PrimitiveType.VOID ) ) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            if ( o.return_type.isPresent() ) {
                if ( !o.return_type.get().equals( PrimitiveType.VOID ) ) {
                    return false;
                }
            }
        }

        return true;
    }

    public String toString() {
        return "FunctionType";
    }

    @Override
    public String getPrintableName() {
        return "FUNC_Type: " + simple_type_name();

    }
    
    @Override
    public String simple_type_name() {
        String out = "(";

        if ( parameters.isPresent() ) {
            out += parameters.get().get_readable_parameter_list();
        }
        
        out += ") -> ";

        if ( return_type.isPresent() ) 
            out += ((Type)return_type.get()).simple_type_name();
        else
            out += "void";
        return out;
    }

    @Override
    public int physical_size() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'physical_size'");
    }

}
