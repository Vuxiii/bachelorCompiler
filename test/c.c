/**
 * * NOTE: Currently this GC doesn't support double pointers.
 * * I need to find a way to support double pointers in a cheap way.
 * * Using a bitfield is a possibility, but limits the number of pointers to 64.
 * * I could also use a 'header' that is a struct like this:
 * *    struct mem_header {
 * *        size_t num_of_pointers;
 * *        array_of **pointers;
 * *    };
 */

#include "sys/mman.h"
#include "stdio.h"
#include "stdlib.h"
#include <string.h>

static size_t heap_size = 4096;
static char *heap;
static char *to_heap;

static size_t pointer_offset = 0;

typedef char *ptr;
typedef unsigned long stack_frame; // Bit vector. A '1' at place i, indicates that v[i] = pointer
typedef int bit;
typedef void *array_of;

void print_addr( void *p ) {
    printf( "Given address is: %p\n", p );
}

void print_addr_heap( char *p ) {
    if ( p >= heap && p <= heap + sizeof(heap) ) {
        printf( "The given pointer is located in heap\n" );
    } else if ( p >= to_heap && p <= to_heap + sizeof(to_heap) ) {
        printf( "The given pointer is located in to_heap\n" );
    } else {
        printf( "The given pointer's location is unknown\n" );
    }
}

void print_num( long out ) {
    printf("%ld\n", out );
}

void print_ptr( void *p ) {
    // printf( "%p\n", p );
    printf( "    p + 0 = %ld\n    p + 8 = %ld\n", *(long *)(p), *(long *)(p+8) );
}


struct LL {
    struct scope *head; // Subject to dangling. If only one node, this will still point to the node when freed
    struct scope *tail;
};

static struct LL ll = { .head = NULL, .tail = NULL };

struct scope {
    struct scope *next;
    struct scope *prev;
    size_t size;
    array_of **root_pointers;
};

struct mem_header {
    size_t num_of_pointers;
    array_of **pointers;
};



void print_scope( struct scope *scp ) {
    printf( "====Struct Scope====\n" );
    printf( "  This: %p\n", scp );
    printf( "  Prev: %p\n", scp->prev );
    printf( "  Next: %p\n", scp->next );
    printf( "  Size: %ld\n", scp->size );

    for ( size_t i = 0; i < scp->size; ++i ) {
        printf( "  p[%ld]:  %p\n", i, scp->root_pointers[i] );
        if ( scp->root_pointers[i] != NULL ) {
            printf( "  *p[%ld]: %p\n", i, *scp->root_pointers[i] );
            print_ptr( *scp->root_pointers[i] );
        }
    }
    printf( "=========END========\n" );
}

void print_all_scopes() {
    struct scope *current_scope = ll.head;
    printf( "====*Linked List of Structs*====\n" );
    while ( current_scope != NULL ) {
        print_scope( current_scope );
        current_scope = current_scope->next;
    }
    printf( "========*End of Structs*========\n" );
}

void free_scope( struct scope *scp ) {

    if ( scp->next != NULL )
        scp->next->prev = scp->prev;
    if ( scp->prev != NULL )
        scp->prev = scp->next;
    
    free(scp);
}

void append_scope( struct scope *scp ) {
    if ( ll.head == NULL ) {
        ll.head = scp;
        ll.tail = scp;
        return;
    }

    ll.tail->next = scp;
    scp->prev = ll.tail;
    ll.tail = scp;
}

/**
 *  Meant to be an easy way to append ptrs to the current scope in assembly-code.
 */
void fill_scope_ptr( size_t index, void **p, struct scope *scp ) {
    scp->root_pointers[index] = p;
}


/**
 * This struct will be filled out in the assembly-code.
 */
void new_scope( size_t num_of_ptrs, struct scope **address_to_store_scope ) {
    struct scope *scp = malloc( sizeof(struct scope) );
    
    scp->prev = NULL;
    scp->next = NULL;
    scp->size = num_of_ptrs;
    scp->root_pointers = malloc(sizeof(void *)*num_of_ptrs);
    append_scope( scp );

    *address_to_store_scope = scp;

}

size_t get_size_of_record( void *record ) {
    // Sizeof(size_t) = storage to store the size field of the record
    // *(size_t *)record = The first field is the size of the record.
    return sizeof(size_t) + *(size_t *)record;
}

void swap_heap_buffers() {
    // Go through each root ptr.
        // Make this ptr point to to_heap
        // See if this record has any pointers.
            // Move these pointers to to_heap.
    

    // Check if ptr points to some memory that has already been transfered to to_heap.
    // If so, ensure that it points to the correct address in to_heap.
    // Thus, we need some way to identify whether or not this already has happend.
    // We could store a pointer, in the heap, to the new place in the to_heap.
    // That way we need to check if what we are reading is a pointer in the to_heap region.

    printf( "Start of old heap: %p\n", heap );
    printf( "End of old heap:   %p\n", heap + sizeof(heap) );

    pointer_offset = 0;

    char *dst = to_heap;

    struct scope *current_scope = ll.head;
    while ( current_scope != NULL ) {

        for ( size_t i = 0; i < current_scope->size; ++i ) {
            void **p = current_scope->root_pointers[i];
            if ( !p ) continue;

            // Check if p already has been moved.
            if ( **(size_t **)p >= to_heap && **(size_t **)p <= dst ) {
                // ptr has already been moved
                // Read the new address, which is stored at **p
                *p = **(void ***)p;

            } else {
                // the record hasen't been moved yet
                size_t record_size = get_size_of_record(*p); 
                *p = memcpy( dst, heap + pointer_offset, record_size );
                
                dst = *p + record_size;

                pointer_offset += record_size;
            }
        }


        current_scope = current_scope->next;
    }

    // Make heap = to_heap
    char *old_heap = heap;
    heap = to_heap;

    to_heap = realloc(old_heap, 2* sizeof(heap));
    
    printf( "Start of new heap: %p\n", heap );
    printf( "End of new heap:   %p\n", heap + sizeof(heap) );
}

/**
 *  This function compacts the heap, such that it doesn't occupy unnecessary memory.
 */
void compact_heap() {

}

ptr new_ptr_size( size_t size ) {
    // Check if heap_pointer + size > heap_size
    if ( pointer_offset + size + sizeof(size_t) >= heap_size ) {
        // We need to move our heap to to_heap, and swap the two pointers
        // If the new heap cannot contain the requested size, increase the size of the heap.
            // Might be bad for performance. Double work
        swap_heap_buffers();
    }

    ptr p = heap + pointer_offset;
    pointer_offset += size + sizeof(size_t);

    *(size_t*)p = size;

    return p;
}

void free_ptr( size_t *p ) {
    // Check bounds
    
    
    size_t size = *p;
    size_t *end = p+ (size/sizeof(size_t));
    printf( "Clearing everything from %p\nto                       %p\n", p, end );
    for ( size_t *c = p; c <= end; ++c ) {
        printf("Clearing value stored at address: %p\n", c);
        *c = 0;
    }
}


void initialize_heap() {
    heap = malloc( heap_size );
    to_heap = malloc( 2*heap_size );

    if ( heap == NULL || to_heap == NULL ) 
        exit(-1);
}