package com.vuxiii.compiler.Visitors;

import java.util.Optional;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.Visitor.VisitorBase;

public abstract class ASTNode implements ASTToken {
    
    /**
     * This method returns a String representation of this node.
     * Usefull for debugging.
     * @return A String representation of this node.
     */
    public abstract String getPrintableName();

    /**
     * Returns whether or not this node is a leaf.
     * @return True if this node is a leaf.
     */
    public abstract boolean isLeaf();

    /**
     * Returns the amount of children this node contains.
     * @return The number of children this node contains
     */
    public abstract int getChildrenCount();

    /**
     * This method returns the first child of this node.
     * @return The first child contained in this node
     */
    protected abstract Optional<ASTNode> getChild1();

    /**
     * This method returns the second child of this node.
     * @return The second child contained in this node
     */
    protected abstract Optional<ASTNode> getChild2();

    /**
     * This method returns the third child of this node.
     * @return The third child contained in this node
     */
    protected abstract Optional<ASTNode> getChild3();

    /**
     * This method returns the fourth child of this node.
     * @return The fourth child contained in this node
     */
    protected abstract Optional<ASTNode> getChild4();

    
    @Override
    public void accept( VisitorBase visitor ) {
        switch (getChildrenCount()) {
            case 0: acceptNoChild(visitor); break;
            case 1: accept1Child(visitor); break;
            case 2: accept2Child(visitor); break;
            case 3: accept3Child(visitor); break;
            case 4: accept4Child(visitor); break;
            default: {
                System.out.println( "Amount of children is bigger than 4.\nPlease implement more methods in ASTNode.java\nExitting!");
                System.exit(-1);
            } break;
        }
    }

    /**
     * This method is for nodes that contian no children. 
     * Thus this node is a leaf.
     * @param visitor The visitor to accept
     */
    protected void acceptNoChild( VisitorBase visitor ) {
        visitor.visit( this );

        visitor.preVisit( this );
        visitor.midVisit( this );
        visitor.postVisit( this );
    }

    /**
     * This method is for nodes that contian one child. 
     * Thus this node is an inner-node.
     * @param visitor The visitor to accept
     */
    protected void accept1Child( VisitorBase visitor ) {
        visitor.visit( this );

        visitor.preVisit( this );
        Optional<ASTNode> maybeChild1 = getChild1();
        if ( maybeChild1.isPresent() ) {
            ASTNode child1 = maybeChild1.get();
            child1.accept( visitor );
        }
        visitor.midVisit( this );
        visitor.postVisit( this );
    }

    /**
     * This method is for nodes that contian two children. 
     * Thus this node is an inner-node.
     * @param visitor The visitor to accept
     */
    protected void accept2Child( VisitorBase visitor ) {
        visitor.visit( this );

        visitor.preVisit( this );
        Optional<ASTNode> maybeChild1 = getChild1();
        if ( maybeChild1.isPresent() ) {
            ASTNode child1 = maybeChild1.get();
            child1.accept( visitor );
        }
        visitor.midVisit( this );
        Optional<ASTNode> maybeChild2 = getChild2();
        if ( maybeChild2.isPresent() ) {
            ASTNode child2 = maybeChild2.get();
            child2.accept( visitor );
        }
        visitor.postVisit( this );
    }

    /**
     * This method is for nodes that contian three children. 
     * Thus this node is an inner-node.
     * @param visitor The visitor to accept
     */
    protected void accept3Child( VisitorBase visitor ) {
        visitor.visit( this );

        visitor.preVisit( this );
        Optional<ASTNode> maybeChild1 = getChild1();
        if ( maybeChild1.isPresent() ) {
            ASTNode child1 = maybeChild1.get();
            child1.accept( visitor );
        }
        visitor.midVisit( this );
        Optional<ASTNode> maybeChild2 = getChild2();
        if ( maybeChild2.isPresent() ) {
            ASTNode child2 = maybeChild2.get();
            child2.accept( visitor );
        }
        Optional<ASTNode> maybeChild3 = getChild3();
        if ( maybeChild3.isPresent() ) {
            ASTNode child3 = maybeChild3.get();
            child3.accept( visitor );
        }
        visitor.postVisit( this );
    }

    /**
     * This method is for nodes that contian three children. 
     * Thus this node is an inner-node.
     * @param visitor The visitor to accept
     */
    protected void accept4Child( VisitorBase visitor ) {
        visitor.visit( this );

        visitor.preVisit( this );
        Optional<ASTNode> maybeChild1 = getChild1();
        if ( maybeChild1.isPresent() ) {
            ASTNode child1 = maybeChild1.get();
            child1.accept( visitor );
        }
        visitor.midVisit( this );
        Optional<ASTNode> maybeChild2 = getChild2();
        if ( maybeChild2.isPresent() ) {
            ASTNode child2 = maybeChild2.get();
            child2.accept( visitor );
        }
        Optional<ASTNode> maybeChild3 = getChild3();
        if ( maybeChild3.isPresent() ) {
            ASTNode child3 = maybeChild3.get();
            child3.accept( visitor );
        }
        Optional<ASTNode> maybeChild4 = getChild4();
        if ( maybeChild4.isPresent() ) {
            ASTNode child4 = maybeChild4.get();
            child4.accept( visitor );
        }
        visitor.postVisit( this );
    }
}
