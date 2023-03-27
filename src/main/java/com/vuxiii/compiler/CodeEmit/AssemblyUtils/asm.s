
.section .data
.section .text
.global main
main:

# Setup stackpointer
    push %rbp   
    movq %rsp, %rbp         

    subq $32, %rsp          # -8(%rbp) -> scope pointer

    callq initialize_heap

    leaq -8(%rbp), %rdi
    callq register_pointer_layout

    movq $3, -8(%rbp) # size
    movq $2, -16(%rbp) # bit_field

    movq $2, %rdi
    callq new_ptr_size 

    movq %rax, -24(%rbp) # pointer

    movq $42, 8(%rax)

    callq print_scopes

    push %rbp   
    movq %rsp, %rbp

    subq $32, %rsp          # -8(%rbp) -> scope pointer

    leaq -8(%rbp), %rdi
    callq register_pointer_layout

    movq $4, -8(%rbp) # size
    movq $6, -16(%rbp) # bit_field

    movq $2, %rdi
    callq new_ptr_size 

    movq %rax, -24(%rbp) # pointer
    movq $42, 8(%rax)


    movq $2, %rdi
    callq new_ptr_size 

    movq %rax, -32(%rbp) # pointer
    movq $69, 8(%rax)


    callq print_scopes

    movq %rbp, %rsp         # Restore stackpointer
    pop %rbp

    movq %rbp, %rsp         # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
