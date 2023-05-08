.section .data
# [ String Buffers and Substitutes ]
string26: .ascii "%\n"
string26subs: .ascii ""

# [ Pointers to Record Layouts ]
heap1: .space 8

.section .text
a:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    movq $0, %rdi
    leaq -8(%rbp), %rsi
    pushq $0
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp
    
    # [[ Loading variable b ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rax
    
    pushq %rax
    callq c
    pushq %rax
    pushq $5
    popq %rcx
    popq %rbx
    addq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    popq %rax
    callq release_scope_header
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    retq
    
c:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $8, %rsp
    movq $0, %rdi
    leaq -8(%rbp), %rsi
    pushq $0
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp
    pushq $4
    popq %rcx
    
    # [[ Loading variable d ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rbx
    
    addq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    popq %rax
    callq release_scope_header
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    retq
    
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    callq initialize_heap
    movq $0, %rdi
    leaq -8(%rbp), %rsi
    pushq $0
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp

# Number of Bitfields

    movq $1, %rdi

# Bitfields

    pushq $0
    leaq (%rsp), %rsi
    callq new_layout
    addq $1, %rsp
    movq %rax, heap1
    pushq $3
    popq %rax
    
    # [[ Storing variable first [-3] ]] 
    # [[ offset is -3 ]] 
    movq %rax, -24(%rbp)
    
    
    # [[ Loading variable first ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rax
    
    pushq %rax
    callq a
    pushq %rax
    callq a
    pushq %rax
    popq %rcx
    
    # [[ Loading variable first ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rbx
    
    addq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax

# Setup Print

    movq $string26, %rdi
    movq $0, %rsi
    movq $0, %rdx
    callq print_string
    popq %rdi
    callq print_num
    movq $string26, %rdi
    movq $1, %rsi
    movq $1, %rdx
    callq print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
