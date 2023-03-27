
.section .data
.section .text
.global main
main:

# Setup stackpointer
    push %rbp   
    movq %rsp, %rbp         

    subq $32, %rsp          # -8(%rbp) -> scope pointer

    call initialize_heap

    movq $1, %rdi
    leaq -8(%rbp), %rsi
    call new_scope
    

    movq $8, %rdi           # 8 bytes of memory. Actually 8+8, where the first 8 is the length.
    call new_ptr_size
    movq %rax, -24(%rbp)    # Store the ptr


    movq $0, %rdi           # Index of our ptr
    leaq -24(%rbp), %rsi    # What address the ptr is at
    movq -8(%rbp), %rdx     # The scope ptr
    call fill_scope_ptr     # Insert the ptr ptr into the scope.

    movq -24(%rbp), %rax
    movq $42, 8(%rax)       # Write the golden number


    movq $1, %rdi
    leaq -16(%rbp), %rsi
    call new_scope

    movq $8, %rdi           # 8 bytes of memory. Actually 8+8, where the first 8 is the length.
    call new_ptr_size
    movq %rax, -32(%rbp)    # Store the ptr


    movq -32(%rbp), %rax
    movq $69, 8(%rax)       # Write the golden number

    movq $0, %rdi           # Index of our ptr
    leaq -32(%rbp), %rsi    # What address the ptr is at
    movq -16(%rbp), %rdx     # The scope ptr
    call fill_scope_ptr     # Insert the ptr ptr into the scope.

    call print_all_scopes

    call swap_heap_buffers

    call print_all_scopes


    movq %rbp, %rsp         # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
