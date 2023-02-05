package com.vuxiii.compiler.VisitorPattern.Visitors.Debug;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.vuxiii.compiler.VisitorPattern.ASTNode;
import com.vuxiii.compiler.VisitorPattern.Visitor;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitOrder;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public class AST_Printer extends Visitor {

    private final String bar_with_child   = "├";
    private final String bar_end          = "└";
    private final String child_indicator  = "── ";
    private final String bar_with_spacing = "│   ";
    private final String spacing          = "    ";

    private final Map<ASTNode, String> parent_prefix = new HashMap<>();
    private final Map<ASTNode, ASTNode> child_to_parent = new HashMap<>();
    private final Map<ASTNode, Integer> parents_remaining_children = new HashMap<>();

    private String ast_string = "";

    private static final String pinkCode = "\u001B[35m";
    private static final String reset = "\u001B[0m";

    /**
     * This method returns the textual representation of the given AST.
     * @return A textual representation of the AST or the empty String if it hasn't been given to a node yet.
     */
    public String get_ascii() {
        return ast_string;
    }

    /**
     * This method is visited via the Visitor Pattern.
     * It sets up the initial values for a node.
     * 1: A counter of how many children have been displayed so far
     * 2: Pointers from this node's children to the parent node
     * 3: The prefix for any direct children of this node 
     * @param token The current node in the AST
     */
    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 1 )
    public void setup_node( ASTNode token ) {
        Optional<ASTNode> maybeParent = get_parent(token);
        if ( maybeParent.isPresent() ) {
            ASTNode parent = maybeParent.get();
            decrease_children_left(parent);
        }
        parents_remaining_children.put( token, token.getChildrenCount() );

        register_children(token);
        set_prefix_for_children( token );
    }

    /**
     * This method is visited via the Visitor Pattern.
     * It prints the prefix and the current Node to stdout
     * @param token
     */
    @VisitorPattern( when = VisitOrder.ENTER_NODE, order = 2  )
    public void print_node( ASTNode token ) {

        Optional<ASTNode> maybeParent = get_parent(token);
        // This is for printing the root. It should not be indented
        if ( maybeParent.isEmpty() ) { 
            ast_string += token.getPrintableName() + "\n";
            return;
        }

        String prefix = get_prefix(token);
        int children_left = get_children_left( maybeParent.get() );

        if ( children_left == 0 )
            prefix += bar_end;
        else
            prefix += bar_with_child;

        if ( token.isLeaf() )
            ast_string += prefix + child_indicator + pinkCode + token.getPrintableName() + reset + "\n";
        else 
            ast_string += prefix + child_indicator + token.getPrintableName() + "\n";
    }

    /**
     * This method decreases the counter for how many children 
     * the given parent still needs to print to stdout
     * @param node The child of the parent
     */
    private void decrease_children_left( ASTNode node ) {
        parents_remaining_children.put( node, parents_remaining_children.get(node)-1);
    }
    
    /**
     * This method returns the parent of the given child
     * @param child The child which parent we wish to retrieve
     * @return The child's parent if it exists
     */
    private Optional<ASTNode> get_parent( ASTNode child ) {
        if ( !child_to_parent.containsKey(child) )
            return Optional.empty();
        return Optional.of( child_to_parent.get( child ) );
    }

    /**
     * This method registres the given parent's children.
     * @param parent The parent
     */
    private void register_children( ASTNode parent ) {
        Optional<ASTNode> maybeChild1 = parent.getChild1();
        Optional<ASTNode> maybeChild2 = parent.getChild2();
        Optional<ASTNode> maybeChild3 = parent.getChild3();
        Optional<ASTNode> maybeChild4 = parent.getChild4();
        if ( maybeChild1.isPresent() )
            child_to_parent.put( maybeChild1.get(), parent );
        if ( maybeChild2.isPresent() )
            child_to_parent.put( maybeChild2.get(), parent );
        if ( maybeChild3.isPresent() )
            child_to_parent.put( maybeChild3.get(), parent );
        if ( maybeChild4.isPresent() )
            child_to_parent.put( maybeChild4.get(), parent );
    }

    /**
     * This method returns how many children the parent still
     * needs to print to stdout
     * @param parent The parent to check
     * @return How many children still need to be printed
     */
    private int get_children_left( ASTNode parent ) {
        return parents_remaining_children.get( parent );
    }

    /**
     * This method returns the prefix for the given node.
     * The prefix is determined by it's parent and former nodes
     * @param node The node to obtain the prefix for
     * @return The prefix for the node.
     */
    private String get_prefix( ASTNode node ) {
        Optional<ASTNode> maybeParent = get_parent(node);
        if ( maybeParent.isEmpty() ) 
            return "";

        return parent_prefix.getOrDefault( maybeParent.get(), "" );
    }

    /**
     * This method sets the prefix for any children of this node
     * @param node The node for which to give a prefix.
     */
    private void set_prefix_for_children( ASTNode node ) {
        Optional<ASTNode> maybeParent = get_parent(node);
        if ( maybeParent.isEmpty() ) return;

        String prefix = get_prefix(node);
        ASTNode parent = maybeParent.get();

        if ( get_children_left(parent) < 1 )
            prefix = prefix + spacing;
        else
            prefix = prefix + bar_with_spacing;

        parent_prefix.put( node, prefix );

    }
}
