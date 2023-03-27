#include "stdio.h"
#include "stdlib.h"
#include <string.h>
#include <stdint.h>

// Garbage collector

static size_t heap_size = 4096;
static size_t *heap;
static size_t *to_heap;

static size_t pointer_offset = 0;

static size_t ***scope_pointers;
static size_t scope_size = 100;
static size_t scope_offset = 0;

void initialize_heap() {
    heap = malloc( sizeof(char) * heap_size );
    to_heap = malloc( 2 * sizeof(char) * heap_size );

    scope_pointers = malloc( scope_size * sizeof(size_t *) );

    if ( heap == NULL || to_heap == NULL || scope_pointers == NULL ) {
        printf( "Some error initializing the heap...\nExitin!\n" );
        exit(-1);
    }
    printf( "Start of new heap: %p\n", heap );
}

int get_bit( size_t bit_field, int offset ) {
    uint64_t mask = 1ULL << offset;
    return mask & bit_field ? 1 : 0;
}

size_t get_size_of_record( size_t *record ) {
    return *(record + 1); // Second field is the size of the memory block
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

size_t *move_this_scopes_pointers( size_t bit_field, size_t *scope, size_t *dst ) {
    int should_repeat = 1;
    size_t off = 0;
    while ( should_repeat == 1 ) {
        for ( size_t j = 1; j < 64; ++j ) {
            if ( get_bit( bit_field, j ) == 1 ) {
                // we know scope + (8*j) is a pointer
                size_t **stack_pointer = (size_t **) (scope + j);
                size_t *memory_block = *stack_pointer;
                // Check if the pointer already has been moved
                // if ( *pointer >= to_heap )

                size_t size_of_block = get_size_of_record( memory_block );
                void *new_dst = memcpy( dst, memory_block, size_of_block );

                memory_block = new_dst;
                *stack_pointer = memory_block;

                pointer_offset += size_of_block;
                dst = new_dst + size_of_block;


                // Check to see if this pointer contains any nested pointers
                if ( get_bit_field(memory_block) != 0 ) {
                    // This has pointers
                    dst = move_this_scopes_pointers( get_bit_field(memory_block), memory_block, dst );
                }

            }
        }
        should_repeat = get_bit( bit_field, 0 );
        if ( should_repeat == 1 ) {
            off++;
            bit_field = *(scope + off); // Go to the next bit_field.
        }
    }
    return dst;
}

void swap_heap_buffers() {
    // We can move through scope_pointers one by one.
    size_t *dst = to_heap;
    pointer_offset = 0;

    for ( size_t i = 0; i < scope_offset; ++i ) {
        size_t *scope = *scope_pointers[i];
        size_t bit_field = get_bit_field(scope);
        
        if ( bit_field == 0 ) {
            continue; // There are no pointers in this scope. Continue to the check the next one.
        }

        // Find all the pointers in this scope by thier offset from scope.
        dst = move_this_scopes_pointers( bit_field, scope, dst );
    }

    size_t *old_heap = heap;
    heap = to_heap;
    to_heap = realloc( old_heap, 2*sizeof(heap) );
}

// Callable by the user. To allocate new memory


size_t *new_ptr_size( size_t size ) {
    // Check if heap_pointer + size > heap_size
    if ( pointer_offset + size + 1 >= heap_size ) {
        // We need to move our heap to to_heap, and swap the two pointers
        // If the new heap cannot contain the requested size, increase the size of the heap.
            // Might be bad for performance. Double work
        swap_heap_buffers();
    }

    size_t *p = heap + pointer_offset;
    pointer_offset += size + 1;

    *p = size + 1;

    printf( "Allocating new pointer: %p\n", p );
    printf( "Offset for heap_ptr is: 8 bytes * %ld\n", pointer_offset );

    return p;
}

size_t *new_scope_header( size_t size, size_t **pointer_to_stack ) {

    *pointer_to_stack = malloc( sizeof( size_t ) * size );

    if ( pointer_to_stack == NULL ) {
        printf( "Failed trying to allocate memory for scope header!\nExiting..!\n" );
        exit(-1);
    }

    printf( "Allocating new scope header:  %p of size: %ld\n", *pointer_to_stack, size );
    printf( "Which was stored at location: %p\n", pointer_to_stack );

    if ( scope_offset == scope_size ) {
        scope_size *= 2;
        scope_pointers = realloc( scope_pointers, sizeof( size_t *) * scope_size );
        if ( scope_pointers == NULL ) {
            printf( "Some error realloc scope_pointers!\nExiting!\n" );
            exit(-1);
        }
    }
    scope_pointers[scope_offset] = pointer_to_stack;
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


void print_block( size_t *block ) {
    size_t size = *block;
    printf("\tVisiting block: %p with size: %ld\n", block, size );
    
    size_t bit_field = get_bit_field( block );
    size_t *end_of_header = locate_end_of_header( block );
    
    // print header
    // printf( "\tElement[0] = %ld\n", *block );
    // size_t j = 1;
    // do {
    //     size_t bit_field = get_bit_field( block - (j-1) );
    //     printf( "\tElement[%ld] = ", j );
    //     print_bit_field( bit_field );
    //     j++;
    // } while( block + (j-1) != end_of_header );

    // print body
    for ( size_t j = 0; j < size; ++j ) {
        // if ( get_bit( bit_field, j-1 ) == 1 ) {
        //     printf( "\tPointer[%ld] = %p\n", j, get_pointer_from_heap_at_offset( block, j ));
        // } else {
            printf( "\t\tElement[%ld] = %ld\n", j, *(block + j));
        // }
    }
}


void print_scopes() {
    printf( "Amount of scopes: %ld\n", scope_offset );
    for ( size_t i = 0; i < scope_offset; ++i ) {
        size_t *stack = (size_t *)scope_pointers[i];
        size_t *scope_layout = *scope_pointers[i];
        printf("Visiting scope: %p\n", scope_pointers[i] );
        
        size_t *end_of_header = locate_end_of_header( scope_layout );
        
        // print header
        size_t j = 0;
        do {
            size_t bit_field = get_bit_field( scope_layout - j );
            printf( "\tBitfield[%ld] = ", j );
            print_bit_field( bit_field );
            j++;
        } while( scope_layout + j != end_of_header );



        // print body
        j = 0;
        do {
            size_t bit_field = get_bit_field( scope_layout - j );
            for ( size_t z = 1; z < 64; ++z ) {
                if ( get_bit( bit_field, z ) == 1 ) {
                    size_t offset = j*64UL + z;
                    size_t *p = get_pointer_from_stack_at_offset( stack, offset );
                    printf( "\t Pointer[%ld] = %p\n", j*64 + z, p  );

                    size_t size = *p;
                    for ( size_t x = 0; x < size; ++x ) 
                        printf( "\t*Pointer[%ld][%ld] = %ld\n", j*64 + z, x, p[x] );
                }
            }
            j++;
        } while( scope_layout + j != end_of_header );
    }
}
