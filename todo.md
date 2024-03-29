
Registering the layout for the stack i need:
    Store a pointer to the layout struct, which is a bunch of 64 bit integers,
    that indicate with a 1 if that offset is a pointer.
    This pointer is stored at -8(%rbp)
    Let's call this a stack frame.

---

I need to update the readme, so it follows the newly updated visitor pattern.
It is now more automatic and uses annotations.
These annotations should also be documented!


Note for jump instructions
    The fall-through path is often faster than the taken path (Engineering a COmpiler p. 176)
    That is not taking the jump is faster.


Print funktion i assembly:
    For hvert et print skal jeg angive:
    * String buffer: Hvad der skal printes
      * Kan laves under compile
    * stop_indicator array: Angiver hvilke indekser der skal erstattes.
      * Kan laves under compile
    * Substitute Array: Angiver hvad der skal erstattes med.
      * Kan laves under compile

Addressing modes

    Register Addressing

    Immediate Addressing
        Constant value

    Direct Memory Addressing
        mov buffer, %rax

    Direct Offset Addressing
        Bruges ikke rigtig. Måske
        Vil være offset lige efter hvor label ligger.

    Indirect Memory Addressing
        mov $42, (buffer)
    Indirect Offset Addressing
        mov $42, 8(buffer)


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

Add declaration

    declaration -> 'let' ident ':' type 
    
    // Simple for now. Ignore me
    declaration -> 'let' ident ':' type '=' Expression


# Symbol Collector



# Struct

# Types

For at lave en type:
let type integer: int; 

    let struct Person: {
    let Person: struct {
        age: int;
        name: string;
        position: Location;
    };

    let type string: [char];
    define string: [char];

    let enum Instruction: {
        ADD;
        SUB;
    }

    let type Location: {
        x: struct { x: int; y: int; };
        y: int;
    };

    let person: Person;


parser:

    statement -> n_Declaration

    n_Declare -> n_Variable_Declaration
    n_Declare -> n_Type_Declaration

    n_type_declare -> n_Type_Declare_Body n_New_Type
    n_type_declare -> n_Type_Declare_Body n_Type

    n_Type_Declare_Body -> t_Let t_Type_Declare t_Identifer t_Colon


    n_New_Type -> { n_Fields } // struct with fields.
    n_Type -> t_Type_Int
    n_Type -> t_Type_Double // Add funtion type. To register a user_type we need to also search for identifiers. And make sure it matches with something that has already been made after the parse state.
    
    // !!!! Or the tokenizer could do it, when calling the `new type` constructor!



## Function

Parser:

    n_Declaration -> n_Declaration_Function

    n_Declaration_Function -> n_Function_Type

    n_Function_Type -> n_Function_Signature 
    n_Function_Type -> n_Function_Signature n_Return_Type

    n_Function_Signature -> t_LParen n_Parameter_List t_RParen
    n_Function_Signature -> t_LParen t_RParen 

    n_Return_Type -> t_Arrow_Right n_Standard_Type
    n_Return_Type -> t_Arrow_Right n_User_Type

    n_Parameter_List -> n_Parameter t_Comma n_Parameter_List
    n_Parameter_List -> n_Parameter

    n_Parameter -> t_Identifer t_Colon n_Standard_Type
    n_Parameter -> t_Identifer t_Colon n_User_Type

### Declaration

    get_name: () -> string;

    set_name: (name: string, person: Person) -> void;
    set_name: (name: string, person: Person);
    set_name: (string, Person);

### Assign body

    get_name() -> String {
        return "William";
    }

    set_name( person: Person, name: string ) -> void {
        person.name = name;
    }

    set_name( person: Person, name: string ) {
        person.name = name;
    }

### Decl and Assign

    walk_to: ( person: Person, target: Location ) -> bool {
        let reached_Location = false;
        ... [body] ...

        return reached_Location;
    }


### Call on struct

Declare the function. The parameter that you wish to be able to call it on, should be prefixed with an '@' on the type.

If the function has no parameters, simply make a parameter called something like 'self' and declare it's type like normal with the '@' as a prefix.

It works like Lua. where you call var:func -> the var will be passed as the first argument.

decl: 

    walk_to: ( person: @Person, target Location ) -> bool {
        let reached_Location = false;
        ... [body] ...

        return reached_Location;
    }

    let person1: Person;

    person1.walk_to( sdu ); => walk_to( person1, sdu );
    get_name: ( self: @Person ) -> string {
        return self.name;
    }

    or

    get_name: ( this: @Person ) -> string {
        return this.name;
    }

    get_distance_between: ( person: @Person, location: @Location ) -> double {
        return ( (person.position.x - location.x)**2 + (person.position.y - location.y)**2 )**0.5;
    }

Call:

    let william: Person;

    set_name( william, "William" );

    let my_name = william.get_name();

    let home: Location = { 5, 3 }; // This is implicit x, y. Order matters
    let sdu: Location = { x = 9, y = 2 };

    walk_to( william, sdu );

    william.walk_to( home );

    let distance_to_sdu = sdu.get_distance_between( person = william );

    let work: Location = { y = 3, x = 9 };

    let distance_to_work = william.get_distance_between( location = work );