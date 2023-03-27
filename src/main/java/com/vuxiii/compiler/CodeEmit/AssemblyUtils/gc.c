#include "stdio.h"
#include "stdlib.h"
#include <string.h>
#include <stdint.h>

void print_bit_field( size_t bit_field );

// Garbage collector

static size_t heap_size = 4096 / sizeof(size_t);
static size_t *heap;
static size_t *to_heap;

static size_t pointer_offset = 0;

static size_t **scope_pointers;
static size_t scope_size = 100;
static size_t scope_offset = 0;

void initialize_heap() {
    heap = malloc( sizeof(size_t) * heap_size );
    to_heap = malloc( 2 * sizeof(size_t) * heap_size );

    scope_pointers = malloc( scope_size * sizeof(size_t *) );

    if ( heap == NULL || to_heap == NULL || scope_pointers == NULL ) {
        printf( "Some error initializing the heap...\nExitin!\n" );
        exit(-1);
    }
    printf( "Start of new heap: %p\n", heap );
    printf( "End   of new heap: %p\n", heap + heap_size );
}

int get_bit( size_t bit_field, int offset ) {
    uint64_t mask = 1ULL << offset;
    return mask & bit_field ? 1 : 0;
}

size_t get_size_of_record( size_t *record ) {
    return *record;
}

size_t get_bit_field( size_t *pointer ) {
    return *pointer;
}

size_t *locate_end_of_header( size_t *scope ) {
    while ( get_bit( get_bit_field( scope ), 0 ) == 1 ) {
        scope++;
    }

    return ++scope;
}

size_t *get_pointer_from_stack_at_offset( size_t *scope, size_t offset ) {
    return *((size_t**)scope - offset);
}
size_t *get_pointer_from_heap_at_offset( size_t *block, size_t offset ) {
    return *((size_t**)block + offset);
}

size_t *move_heap_block( size_t **memory_block, size_t *dst ) {

    // Start by moving memory_block to the new heap.
    // But not if we already are there. (Double LinkedList)
    if ( !(*memory_block >= dst && *memory_block <= dst + (2 * sizeof(size_t) * heap_size)) ) {
        // We havent moved it there yet

        size_t size_of_block = get_size_of_record( *memory_block );

        printf( "We have a size for the memory_block: %ld\n", size_of_block );
        size_t *new_dst = memcpy( dst, *memory_block, size_of_block );

        *memory_block = new_dst;

        pointer_offset += size_of_block;
        dst = new_dst + size_of_block;

    }

    // Loop through the layout bitfield. -> Get the pointers!
    size_t off = 0;
    size_t *memory_layout = *(memory_block+off+1); // The size field
    size_t bit_field = *(memory_layout+1);
    int should_repeat = bit_field == 0 ? 0 : 1;
    while ( should_repeat == 1 ) {

        for ( size_t i = 1; i < 64; ++i ) {
            if ( get_bit( bit_field, i ) == 1 ) {
                // We have found the pointer.
                // Call ourselves recursivly
                size_t **ptr = (size_t **)get_pointer_from_heap_at_offset( *memory_block, off*64 + i + 1 ); // The size field
                
                dst = move_heap_block( ptr, dst );
            }
        }

        should_repeat = get_bit( bit_field, 0 );
        if ( should_repeat == 1 ) {
            off++;
            bit_field = *(memory_layout + off+1); // Go to the next bit_field.
            printf( "We are repeating!\n" );
        } else {
            printf( "We are not repeating!\n" );
        }
    }
    return dst;
}

size_t *move_these_root_pointers( size_t bit_field, size_t *scope, size_t *dst ) {
    int should_repeat = 1;
    size_t off = 0;
    while ( should_repeat == 1 ) {
        printf( "We have out bit_field: " );
        print_bit_field( bit_field );
        printf( "\n" );

        for ( size_t j = 1; j < 64; ++j ) {
            if ( get_bit( bit_field, j ) == 1 ) {
                printf( "At bit %ld\n", j );
                printf( "And it is set!\n" );
                // we know scope + (8*j) is a pointer
                size_t **stack_pointer = (size_t **) (scope - j);
                size_t *memory_block = *stack_pointer;

                printf( "We have our stack_pointer at %p\n", stack_pointer );
                printf( "We have our memory_block  at %p\n", memory_block );

                size_t size_of_block = get_size_of_record( memory_block );

                printf( "We have a size for the memory_block: %ld\n", size_of_block );
                size_t *new_dst = memcpy( dst, memory_block, size_of_block );

                memory_block = new_dst;
                *stack_pointer = memory_block;

                pointer_offset += size_of_block;
                dst = new_dst + size_of_block;


                // Check to see if this pointer contains any nested pointers
                if ( get_bit( get_bit_field(memory_block), 0 ) != 0 ) {
                    // This has pointers
                    printf( "We have found nested pointers!\n" );
                    dst = move_heap_block( &memory_block, dst );
                }

            }
        }
        should_repeat = get_bit( bit_field, 0 );
        if ( should_repeat == 1 ) {
            off++;
            bit_field = *(scope + off); // Go to the next bit_field.
            printf( "We are repeating!\n" );
        } else {
            printf( "We are not repeating!\n" );
        }
    }
    return dst;
}

void swap_heap_buffers() {
    printf( "Start of old heap: %p\n", heap );
    printf( "End of old heap:   %p\n", heap + sizeof(heap) );


    // We can move through scope_pointers one by one.
    size_t *dst = to_heap;
    pointer_offset = 0;

    for ( size_t i = 0; i < scope_offset; ++i ) {
        size_t *scope = scope_pointers[i];
        size_t bit_field = get_bit_field( *(size_t **)scope);
        
        printf( "We are looking at scope at address: %p\n", scope );

        if ( bit_field == 0 ) {
            continue; // There are no pointers in this scope. Continue to the check the next one.
        }

        // Find all the pointers in this scope by thier offset from scope.
        dst = move_these_root_pointers( bit_field, scope, dst );
    }
    heap_size *= 2;

    size_t *old_heap = heap;
    heap = to_heap;
    
    to_heap = realloc( old_heap, 2 * sizeof(size_t) * heap_size );

    printf( "Start of new heap: %p\n", heap );
    printf( "End of new heap:   %p\n", heap + heap_size );
}

// Callable by the user. To allocate new memory


size_t *new_ptr_size( size_t size, size_t amount_of_bitfields ) {
    // Check if heap_pointer + size > heap_size
    if ( pointer_offset + size + 2 >= heap_size ) {
        // We need to move our heap to to_heap, and swap the two pointers
        // If the new heap cannot contain the requested size, increase the size of the heap.
            // Might be bad for performance. Double work
        swap_heap_buffers();
    }

    size_t *p = heap + pointer_offset;
    pointer_offset += size + 2;

    p[0] = size + 2; // The total size.
    
    size_t *layout = calloc( size + 1UL, sizeof( size_t )); // Bit field for pointers.

    layout[0] = amount_of_bitfields + 1UL;
    
    p[1] = (size_t)layout;

    

    printf( "Allocating new pointer: %p\n", p );
    printf( "Offset for heap_ptr is: 8 bytes * %ld\n", pointer_offset );

    return p;
}

size_t *new_scope_header( size_t size, size_t **pointer_to_stack ) {

    *pointer_to_stack = calloc( size + 1UL, sizeof( size_t ));
    **pointer_to_stack = size + 1UL;

    if ( pointer_to_stack == NULL ) {
        printf( "Failed trying to allocate memory for scope header!\nExiting..!\n" );
        exit(-1);
    }

    printf( "Allocating new scope header:  %p of size: %ld + 1 = %ld\n", *pointer_to_stack, size, size+1 );
    printf( "Which was stored at location: %p\n", pointer_to_stack );

    if ( scope_offset == scope_size ) {
        scope_size *= 2;
        scope_pointers = realloc( scope_pointers, sizeof( size_t *) * scope_size );
        if ( scope_pointers == NULL ) {
            printf( "Some error realloc scope_pointers!\nExiting!\n" );
            exit(-1);
        }
    }
    scope_pointers[scope_offset] = (size_t *)pointer_to_stack;
    scope_offset++;
}

void free_ptr( size_t *p ) {
    // Check bounds
    
    
    size_t size = *p;
    size_t *end = p + (size/sizeof(size_t));
    printf( "Clearing everything from %p\nto                       %p\n", p, end );
    for ( size_t *c = p; c <= end; ++c ) {
        printf("Clearing value stored at address: %p\n", c);
        *c = 0;
    }
}

void print_bit_field( size_t bit_field ) {
    printf( "[" );
    for ( int i = 63; i >= 0; i-- ) {
        size_t mask = 1ULL << i;
        int bit = (bit_field & mask) ? 1 : 0;
        printf("%d", bit);
        if (i % 4 == 0 && i != 0) {
            printf(" ");
        }
    }
    printf( "]\n");
}

void print_tabs( size_t amount ) {
    for ( size_t i = 0; i < amount; ++i ) {
        printf( "\t" );
    }
}

void print_block( size_t *scope_layout, size_t *stack, size_t amount_of_tabs ) {
    size_t *end_of_header = scope_layout + get_size_of_record(scope_layout);
    
    // print header
    print_tabs(amount_of_tabs);
    printf( "--[ Header ]--\n" );

    print_tabs(amount_of_tabs);
    printf( "Header Size = %ld\n", *scope_layout );
    size_t j = 1;
    // do {
        size_t bit_field = get_bit_field( scope_layout + j );
        print_tabs(amount_of_tabs);
        printf( "Bitfield[%ld] = ", j );
        print_bit_field( bit_field );
        j++;
    // } while( scope_layout + j != end_of_header );

    size_t body_size = 0;
    if ( amount_of_tabs != 1 )
        body_size = *stack;

    // print body
    j = 0;
    do {
        size_t bit_field = get_bit_field( 1 + scope_layout + j );
        if ( bit_field == 0 ) break;
    
        if ( j == 0 ) {
            print_tabs(amount_of_tabs);
            printf( "--[  Body  ]--\n" );
        }

        for ( size_t z = 1; z < 64; ++z ) {
            if ( get_bit( bit_field, z ) == 1 ) {
                size_t offset = j*64UL + z;
                size_t *p;
                if ( amount_of_tabs == 1 ) 
                    p = get_pointer_from_stack_at_offset( stack, offset );
                else 
                    p = get_pointer_from_heap_at_offset( stack, offset );

                print_tabs(amount_of_tabs);
                printf( ".size - meta = %ld\n", *(p) - 2 );
                print_tabs(amount_of_tabs);
                printf( " Pointer[%ld] = %p\n", offset, p  );

                size_t **newP = (size_t**)p+1;

                print_block( *newP, p, amount_of_tabs + 1 );

            } else if ( body_size > 0 ) {
                size_t offset = j*64UL + z + 2;
                if ( offset <= body_size + 2 ) {
                    size_t *p = stack + offset;
                    
                    print_tabs(amount_of_tabs);
                    printf( ".data = %ld\n", *p );
                }
            }
        }
        j++;
    } while( 1 + scope_layout + j != end_of_header );
}

void print_scopes() {
    printf( "Amount of scopes: %ld\n", scope_offset );
    for ( size_t i = 0; i < scope_offset; ++i ) {
        size_t *stack        =  (size_t *) scope_pointers[i];
        size_t *scope_layout = *(size_t **)scope_pointers[i];
        printf("Visiting scope: %p\n", scope_pointers[i] );
        
        print_block( scope_layout, stack, 1 );
    }
}
