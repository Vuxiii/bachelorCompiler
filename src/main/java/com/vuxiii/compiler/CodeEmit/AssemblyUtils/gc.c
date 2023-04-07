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
    // to_heap = malloc( 2 * sizeof(size_t) * heap_size );

    scope_pointers = malloc( scope_size * sizeof(size_t *) );

    if ( heap == NULL || scope_pointers == NULL ) {
        printf( "Some error initializing the heap...\nExitin!\n" );
        exit(-1);
    }
    // printf( "Start of new heap: %p\n", heap );
    // printf( "End   of new heap: %p\n", heap + heap_size );
}

int get_bit( size_t bit_field, int offset ) {
    uint64_t mask = 1ULL << offset;
    return mask & bit_field ? 1 : 0;
}

void print_layout( size_t *layout ) {
    size_t i = 0;
    size_t bitfield = layout[i];
    do {

        print_bit_field( bitfield );

        // Next bitfield
        if ( get_bit(bitfield, 0) == 1 ) {
            i++;
            bitfield = layout[i];
        } else {
            break;
        }
    } while ( bitfield != 0 );
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

        // printf( "We have a size for the memory_block: %ld\n", size_of_block );
        size_t *new_dst = memcpy( dst, *memory_block, size_of_block * sizeof(size_t) );

        *memory_block = new_dst;

        pointer_offset += size_of_block;
        dst = new_dst + size_of_block;

    } else {
        return dst;
    }

    // printf( "We have moved the memory_block to the new region!\nNew location is: %p\n", *memory_block );

    size_t *layout = *(size_t**)((*memory_block) + 1);
    
    if ( layout == 0 ) return dst;

    // printf( "layout: %p\n", layout );

    const size_t header_offset = 1;
    size_t i = 0;
    size_t bitfield = layout[i];
    while ( bitfield != 0 ) {
        // printf( "========================================\n" );
        // printf( "Bitfield: " );
        // print_bit_field( bitfield );
        for ( size_t b = 1; b < 64; b++ ) {
            if ( get_bit(bitfield, b) == 1 ) {
                size_t **new_memory_block = (size_t**)((*memory_block) + b + header_offset);
                // printf( "Found pointer at location: %ld\n", b );
                // printf( "&Pointer[%ld] = %p\n", b, new_memory_block );
                // printf( " Pointer[%ld] = %p\n", b, *new_memory_block );
                // printf( "*Pointer[%ld] = %ld\n", b, *(*new_memory_block) );
                
                dst = move_heap_block( new_memory_block, dst );
            }
        }

        // Next bitfield
        if ( get_bit(bitfield, 0) == 1 ) {
            i++;
            bitfield = layout[i];
        } else {
            break;
        }
    }
    return dst;

}

size_t *move_these_root_pointers( size_t* layout, size_t *stack, size_t *dst ) {
    
    size_t i = 0;
    size_t bitfield = layout[i];
    while ( bitfield != 0 ) {
        
        // Check all the 63 bits.
        for ( size_t b = 1; b < 64; b++ ) {
            size_t bit = get_bit(bitfield, b);
            if ( bit == 1 ) {
                size_t **memory_block = (size_t**)(stack - b);
                // printf( "Found pointer at location: %ld\n", b );
                // printf( "&Pointer[%ld] = %p\n", b, stack - b );
                // printf( "*Pointer[%ld] = %p\n", b, *memory_block );
                
                dst = move_heap_block( memory_block, dst );
            }
        }

        // Next bitfield
        if ( get_bit(bitfield, 0) == 1 ) {
            i++;
            bitfield = layout[i];
        } else {
            break;
        }
    }
    return dst;
}

void swap_heap_buffers() {

    // printf( "\n======================================\nStarting swap_heap_buffers\n" );
    // printf( "Start of old heap: %p\n", heap );
    // printf( "End of old heap:   %p\n", heap + sizeof(heap) );


    // We can move through scope_pointers one by one.
    to_heap = malloc( 2 * sizeof(size_t) * heap_size );
    if ( to_heap == NULL ) {
        printf( "Failed to allocate space for the new heap.\nExiting!\n" );
        exit(-1);
    }

    size_t *dst = to_heap;
    pointer_offset = 0;


    for ( size_t i = 0; i < scope_offset; ++i ) {
        size_t *scope        =  (size_t *) scope_pointers[i];
        size_t *scope_layout = *(size_t **)scope_pointers[i];
        
        // printf("Visiting scope: %p\n", scope_pointers[i] );
        
        // printf( "We are looking at scope at address: %p\n", scope );

        if ( scope_layout == 0 ) {
            continue; // There are no pointers in this scope. Continue to the check the next one.
        }

        // Find all the pointers in this scope by thier offset from scope.
        dst = move_these_root_pointers( scope_layout, scope, dst );
    }

    // Check if we need to expand the heap.
    if ( pointer_offset > heap_size * 0.7 ) {
        printf( "Let's expand it!\n" );
    }

    heap_size *= 2;

    size_t *old_heap = heap;
    heap = to_heap;
    
    free( old_heap );
    
    // printf( "Start of new heap: %p\n", heap );
    // printf( "End of new heap:   %p\n", heap + heap_size );
    // printf( "\n======================================\nEnding swap_heap_buffers\n" );
}

size_t *new_layout( size_t size, size_t *bitfields ) {
    size_t *layout = calloc( size, sizeof(size_t) );
    if ( layout == NULL ) {
        printf( "Error allocating new layout\nExiting!\n" );
        exit(-1);
    }

    memcpy( layout, bitfields, size * sizeof(size_t) );

    printf( "\n\n" );

    print_layout( layout );

    printf( "\n\n" );

    return layout;
}

size_t *new_ptr_size( size_t size, size_t *layout ) {
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
    
    p[1] = (size_t)layout;

    

    // printf( "Allocating new pointer: %p\n", p );
    // printf( "Offset for heap_ptr is: 8 bytes * %ld\n", pointer_offset );

    return p;
}

void new_scope_header( size_t size, size_t **pointer_to_stack, size_t *bitfield ) {

    *pointer_to_stack = calloc( size, sizeof( size_t ));
    
    memcpy( *pointer_to_stack, bitfield, size );

    if ( pointer_to_stack == NULL ) {
        printf( "Failed trying to allocate memory for scope header!\nExiting..!\n" );
        exit(-1);
    }

    // printf( "Allocating new scope header:  %p of size: %ld\n", *pointer_to_stack, size );
    // printf( "Which was stored at location: %p\n", pointer_to_stack );

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

size_t calc_header_size( size_t *layout ) {
    if ( layout == 0 ) return 0;

    size_t size = 0;
    while ( get_bit( layout[size], 0 ) == 1 ) {
        size++;
    }
    return 1 + size;
}



void print_block( size_t *memory_block, size_t amount_of_tabs ) {
    
    print_tabs( amount_of_tabs );
    printf( "Visiting memory_block at location: %p\n", memory_block );
    print_tabs( amount_of_tabs );
    printf( "size:   %ld\n", memory_block[0] );
    size_t *layout = (size_t*)memory_block[1];
    if ( layout == 0 ) {
        print_tabs( amount_of_tabs );
        printf( "This memory_block has no pointers. Returning\n" );        
        return;
    }

    print_tabs( amount_of_tabs );
    printf( "layout: %p\t",  (size_t*)memory_block[1] );

    print_layout( layout );

    const size_t offset = 1;
    size_t i = 0;
    size_t bitfield = layout[i];

    do { // check if this while loop is correct.

        for ( size_t b = 1; b < 64; ++b ) {
            if ( get_bit( bitfield, b ) == 1 ) {
                size_t pointer_offset = b + offset;
                print_tabs( amount_of_tabs );
                printf( "Found poitner at location: %p\n", memory_block + pointer_offset );
                print_block( *(size_t**)(memory_block + pointer_offset), amount_of_tabs + 1 );
            } 
        }

        // Next bitfield
        if ( get_bit(bitfield, 0) == 1 ) {
            i++;
            bitfield = layout[i];
        } else {
            break;
        }
    } while ( bitfield != 0 );
}

void print_stack_pointers( size_t *layout, size_t *stack ) {
    // Get first bitfield
    const size_t offset = 0;
    size_t i = 0;
    size_t bitfield = layout[i];
    while ( bitfield != 0 ) {
        // Check all the 63 bits.
        for ( size_t b = 1; b < 64; b++ ) {
            size_t bit = get_bit(bitfield, b);
            if ( bit == 1 ) {
                size_t *ptrptr = *(size_t**)(stack - b - offset);
                printf( "Found pointer at location: %ld\n", b + offset );
                printf( "&Pointer[%ld] = %p\n", b+offset, stack - b - offset );
                printf( "*Pointer[%ld] = %p\n", b+offset, ptrptr );
                print_block( ptrptr, 1 );
            }
        }

        // Next bitfield
        if ( get_bit(bitfield, 0) == 1 ) {
            i++;
            bitfield = layout[i];
        } else {
            break;
        }
    }
}

/**
 * @param buffer %rdi The buffer to print
 * @param offsets %rdx The offset in the buffer where the substitutes are located
 * @param subs %rsi What to substitute with
 * @param num_of_subs %rcx The number of substitutes
 * !TODO: Possibly add a layout pointer, such that we get this print for free
 */
void print_subs( char *buffer, long *offsets, char *subs, long num_of_subs ) {
    int curr = 0;
    int offset_into_buffer = 0;
    while ( curr < num_of_subs ) {
        long first_n = offsets[curr] - (curr > 0 ? offsets[curr-1] : 0);
        char *to_write = buffer + offset_into_buffer;
        char *sub = subs;

        printf( "%.*s%s\n", first_n, to_write, sub );
        
        ++offset_into_buffer; // Skip %
        ++curr; // Advance to next sub.
        offset_into_buffer += first_n;
        subs += strlen(subs) + 1;
    }
}

void print_string( char *buffer, long len, long offset ) {
    printf( "%.*s", len, buffer + offset );
    fflush(NULL);
}

void print_num( long num ) {
    printf( "%ld", num );
    fflush(NULL);
}

void print_scopes() {
    printf( "Amount of scopes: %ld\n", scope_offset );
    for ( size_t i = 0; i < scope_offset; ++i ) {
        size_t *stack        =  (size_t *) scope_pointers[i];
        size_t *scope_layout = *(size_t **)scope_pointers[i];
        printf("Visiting scope: %p\n", scope_pointers[i] );
        
        print_stack_pointers( scope_layout, stack );
    }
}

void print_stack( size_t *rbp, size_t *rsp ) {
    size_t offset = 0;
    printf( "======================\nrsp:\t%p\n\n", rsp );
    while ( rsp + offset < rbp ) {
        if ( rsp[offset] < 9999999 ) {
            printf( "rbp %ld\t%ld\n", 8* (rsp + offset - rbp), rsp[offset] );
        } else {
            printf( "rbp %ld\t%p\n", 8 * (rsp + offset - rbp), rsp[offset] );
        }
        offset++;
    }
    printf( "\nrbp:\t%p\n======================\n\n", rbp );
}