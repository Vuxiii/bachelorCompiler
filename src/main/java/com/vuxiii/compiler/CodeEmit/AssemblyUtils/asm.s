
.section .data
layoutptr1: .space 8 
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
    push $2
    leaq (%rsp), %rdx
    callq new_scope_header  # Create scope header
    addq $1, %rsp           # Remove the 2 from the stack.


    movq $1, %rdi
    pushq $2
    leaq (%rsp), %rsi
    callq new_layout
    addq $1, %rsp           # Remove the 2 from the stack.

    # rax has layout pointer now.

    movq $1, %rdi
    movq %rax, %rsi
    callq new_ptr_size      # Create ptr

    movq %rax, -16(%rbp)

    pushq %rax

    movq $1, %rdi
    movq $0, %rsi           # Has no ptr.
    callq new_ptr_size      # Create ptr


    popq %rbx

    movq %rax, 16(%rbx)
    movq $69, 16(%rax)

    callq print_scopes
    callq swap_heap_buffers
    callq print_scopes

    


    movq %rbp, %rsp         # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
