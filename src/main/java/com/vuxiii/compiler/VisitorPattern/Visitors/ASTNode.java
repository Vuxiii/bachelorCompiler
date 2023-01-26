package com.vuxiii.compiler.VisitorPattern.Visitors;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;

import com.vuxiii.LR.Records.ASTToken;
import com.vuxiii.LR.Records.Term;
import com.vuxiii.Visitor.VisitorBase;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitLeaf;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitNumber;
import com.vuxiii.compiler.VisitorPattern.Annotations.VisitorPattern;

public abstract class ASTNode implements ASTToken {
    
    public final Term term;

    private final PriorityQueue<Field> ASTNodeQueue = new PriorityQueue<>( Comparator.comparing( field -> field.getAnnotation( VisitNumber.class ).number() ) );
    
    private int children_count;

    public ASTNode( Term term ) {
        this.term = term;
        // setup_ASTNodeQueue();
    }

    /**
     * This method looks through each field in this node, and stores those
     * that have been marked with the Visitor Pattern Annotation 'VisitNumber'.
     */
    protected void setup_ASTNodeQueue() {
        // Look for fields within 'this' that have the VisitNumber annotation.
        // And visit them in that order.
        
        // Retrieve all the fields that are annotated to be visited by the Visitor Pattern.

        Field[] fields = this.getClass().getFields();

        for ( Field field : fields ) {
            VisitNumber visitNumber = field.getAnnotation( VisitNumber.class );
            if ( visitNumber == null ) continue;

            // Check for empty optionals and skip them...
            // (*) This garuantees that any Optionals in our queue are filled with an ASTNode!
            if ( field.getType().equals( Optional.class ) ) {
                try {
                    Optional<?> opt = (Optional<?>) field.get( this );
                    if ( opt.isEmpty() ) continue; // The optional is empty, we don't need to add it...

                } catch (IllegalArgumentException | IllegalAccessException e) {
                    System.out.println( "--[[ Visitor Pattern Error! ]]--\nTried to unwrap '" + field.getName() + "', but failed!\nExiting!");
                    System.exit(-1);
                }
            }

            // System.out.println( field.getName() + ": " + visitNumber.number() );
            ASTNodeQueue.add( field );
        }
        children_count = ASTNodeQueue.size();
    }

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
    public boolean isLeaf() {
        return this.getClass().isAnnotationPresent( VisitLeaf.class );
    }

    /**
     * Returns the amount of children this node contains.
     * @return The number of children this node contains
     */
    public int getChildrenCount() {
        return children_count;
    }

    @Override
    public Term getTerm() {
        return term;
    }

    /**
     * This method returns the first child of this node.
     * @return The first child contained in this node
     */
    public Optional<ASTNode> getChild1() { 
        return Optional.empty(); 
    }

    /**
     * This method returns the second child of this node.
     * @return The second child contained in this node
     */
    public Optional<ASTNode> getChild2() { 
        return Optional.empty(); 
    }

    /**
     * This method returns the third child of this node.
     * @return The third child contained in this node
     */
    public Optional<ASTNode> getChild3() { 
        return Optional.empty(); 
    }

    /**
     * This method returns the fourth child of this node.
     * @return The fourth child contained in this node
     */
    public Optional<ASTNode> getChild4() { 
        return Optional.empty(); 
    }

    /**
     * This method checks if this object has the correct type to be passed to the given method.
     * @param method Method to the first parameter for correct type.
     * @return True if this object can be passed as the method's first argument. 
     */
    private boolean hasCorrectParameterType( Method method ) {
        Class<?>[] param_type = method.getParameterTypes();
        if ( param_type.length != 1 ) return false;
        return param_type[0].isAssignableFrom( this.getClass() );
    }
    
    @Override
    public void accept( VisitorBase visitor ) {
        
        // Probably allocate once, and after each new visitor just clear them...
        PriorityQueue<Method> enterMethodQueue = new PriorityQueue<>( Comparator.comparing( method -> method.getAnnotation( VisitorPattern.class ).order() ) );
        PriorityQueue<Method> exitMethodQueue = new PriorityQueue<>( Comparator.comparing( method -> method.getAnnotation( VisitorPattern.class ).order() ) );
        PriorityQueue<Method> beforeMethodQueue = new PriorityQueue<>( Comparator.comparing( method -> method.getAnnotation( VisitorPattern.class ).order() ) );
        PriorityQueue<Method> afterMethodQueue = new PriorityQueue<>( Comparator.comparing( method -> method.getAnnotation( VisitorPattern.class ).order() ) );

        // Collect all the methods from the visitor that are marked with the Visitor Pattern
        
        Method[] methods = visitor.getClass().getMethods();

        // Add the methods to the correct queues.
        for ( Method method : methods ) {
            VisitorPattern visitorPattern = method.getAnnotation( VisitorPattern.class );
            if ( visitorPattern == null ) continue;
            // Ensure correct type.
            if ( !hasCorrectParameterType( method ) ) continue;

            switch (visitorPattern.when()) {
                case AFTER_CHILD: {
                    afterMethodQueue.add( method );
                } break;
                case BEFORE_CHILD: {
                    beforeMethodQueue.add( method );
                } break;
                case ENTER_NODE: {
                    enterMethodQueue.add( method );
                } break;
                case EXIT_NODE: {
                    exitMethodQueue.add( method );
                } break;            
                default: {
                    System.out.println( "--[[ Visitor Pattern Error! ]]--\nEnum of type '" + visitorPattern.getClass().getSimpleName() + "' has not been added to the switch statement!\nExiting!" );
                    System.exit(-1);
                } break;
            }
        }

        // Variable for nice error message on method-exception
        Method methodFailure = null;
        Field fieldFailure   = null;
        
        // Pass the Visitor to the correct nodes.
        try {
            // Run all methods that are annotated to be run when entering a node.
            for ( Method method : enterMethodQueue ){
                // System.out.println( "Enter: Invoking '" + method.getName() + "' on visitor '" + visitor.getClass().getSimpleName() + "' with node-parameter '" + this.getPrintableName() + "'" );
                methodFailure = method;
                method.invoke( visitor, this );
            }

            for ( Field field : ASTNodeQueue ) {
                fieldFailure = field;
                ASTNode child;
                if ( field.getType().equals( Optional.class ) )
                    child = (ASTNode) ((Optional<?>) field.get( this )).get(); // We are garuanteed that this optional is filled by (*)
                else
                    child = (ASTNode) field.get(this);
                // Run all methods that are annotated to be run before visiting a child
                for ( Method method : beforeMethodQueue ) {
                    // System.out.println( "Before: Invoking '" + method.getName() + "' on visitor '" + visitor.getClass().getSimpleName() + "' with node-parameter '" + this.getPrintableName() + "'" );
                    
                    methodFailure = method;
                    method.invoke( visitor, this );
                }

                child.accept( visitor );
                

                // Run all methods that are annotated to be run after visiting a child
                for ( Method method : afterMethodQueue ) {     
                    
                    // System.out.println( "After: Invoking '" + method.getName() + "' on visitor '" + visitor.getClass().getSimpleName() + "' with node-parameter '" + this.getPrintableName() + "'" );
                    methodFailure = method;
                    method.invoke( visitor, this );
                } 
            } 

            // Run all methods that are annotated to be run when exiting a node.
            for ( Method method : exitMethodQueue ){
                
                // System.out.println( "Exit: Invoking '" + method.getName() + "' on visitor '" + visitor.getClass().getSimpleName() + "' with node-parameter '" + this.getPrintableName() + "'" );
                methodFailure = method;
                method.invoke( visitor, this );
            }

        } catch (IllegalArgumentException | IllegalAccessException e ) {
            System.out.println( "--[[ Visitor Error! ]]--\nTried to pass the visitor '" + visitor.getClass().getSimpleName() + "' to node '" + (fieldFailure == null ? "fieldFailure is not assigned" : fieldFailure.getName()) + "' in class '" + this.getClass().getSimpleName() + "', but failed for some reason.\nExiting!" );
            e.printStackTrace();
            System.exit(-1);
        } catch (InvocationTargetException e) {
            System.out.println( "--[[ Visitor Error! ]]--\nTried to invoke the method '" + methodFailure.getName() + "' on visitor '" + visitor.getClass().getName() + "' in class '" + this.getClass().getSimpleName() + "', but failed for some reason.\nExiting!" );
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
