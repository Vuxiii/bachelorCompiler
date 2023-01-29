I need to update the readme, so it follows the newly updated visitor pattern.
It is now more automatic and uses annotations.
These annotations should also be documented!

There is a bug in printing BINARYOPERATION. For some reason right is printed before left?

Note for jump instructions
    The fall-through path is ogten faster than the taken path (Engineering a COmpiler p. 176)
    That is not taking the jump is faster.

Add scoping

    [ .. ]       This defines that the coming scope can access any outer_scoped variables
    [ arg_list ] This defines that the coming scope can access the defined variables in the arg_list

    {

    }
    ^ Makes a new scope.

    When can we see this, or how do we add the grammar?

    Should it be

        statement_list -> statement;
        statement_list -> statement; statement_list 

        statement -> new_scope
        new_scope -> capture scope_block
        new_scope -> scope_block
        
        // Capture is a hint that the block modifies or accesses global variables defined in the arg_list
        capture -> [ arg_list ]
        capture -> [ '.' '.' ] // For everyting. Or maybe '*'

        arg_list -> id
        arg_list -> id ',' arg_list

        scope_block -> { statement_list }

        statement -> assignment

    Yea, it is not an expression. It doesn't evaluate to/return anything.

    Maybe a scope should be allowed to return something?

    like let b = {
        let c = 2;
        return c * 5;
    }

    print( b ); -> 10

    Make scope per file.
        Automaticly insert a scope for each file.

