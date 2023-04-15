.section .data
# [ String Buffers and Substitutes ]

# [ Pointers to Record Layouts ]
root: .space 8
intheap: .space 8
.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $8, %rsp
    callq initialize_heap
    movq $1, %rdi
    leaq -8(%rbp), %rsi
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp

# Number of Bitfields

    movq $1, %rdi

# Bitfields

    pushq $2
    leaq (%rsp), %rsi
    callq new_layout
    addq $1, %rsp
    movq %rax, root
    movq $42, intheap


    movq $1, %rdi
    movq intheap, %rsi
    callq new_ptr_size
    
    # [[ Storing variable p1 ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    
    movq $2, 16(%rax)

    movq intheap, %rdi
    callq print_num

    # pushq $2
    # popq %rax
    
    # [[ Storing variable p1 ]] 
    # [[ offset is -1 ]] 
    # movq %rax, -8(%rbp)
    
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
