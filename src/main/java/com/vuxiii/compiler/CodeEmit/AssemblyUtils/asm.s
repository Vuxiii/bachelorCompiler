
.section .data
.section .text
.global main
main:

# Setup stackpointer
    push %rbp   
    movq %rsp, %rbp         

    subq $32, %rsp          # -8(%rbp) -> scope pointer

    callq initialize_heap
    
    movq $1, %rdi           # How many bit fields does this require
    leaq -8(%rbp), %rsi     # Where to store scope header
    callq new_scope_header  # Create scope header

    movq -8(%rbp), %rax     # Read scope header
    movq $2, (%rax)         # bit_field into scope header

    movq $1, %rdi
    callq new_ptr_size      # Create ptr

    movq %rax, -16(%rbp)    # Store ptr
    movq $69, 8(%rax)

    callq print_scopes
    movq %rbp, %rsp         # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
