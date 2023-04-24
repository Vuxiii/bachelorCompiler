.section .data
# [ String Buffers and Substitutes ]
string0: .ascii "a is %\n"
string0subs: .ascii ""
string1: .ascii "The %th fib number is: %\n"
string1subs: .ascii ""

# [ Pointers to Record Layouts ]

.section .text
fib:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $8, %rsp
    movq $0, %rdi
    leaq -8(%rbp), %rsi
    pushq $0
    leaq (%rsp), %rdx
    callq new_scope_header # LABEL
    addq $1, %rsp
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string0, %rdi
    movq $5, %rsi
    movq $0, %rdx
    callq print_string
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rdi
    
    callq print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $6, %rdx
    callq print_string

# End Print

    pushq $1
    popq %rcx
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rbx
    
    cmpq %rbx, %rcx
    jne IfEndOfBody1
    callq release_scope_header # LABEL
    pushq $1
    popq %rax
    movq %rbp, %rsp
    popq %rbp
    retq
    
    jmp EndOfIfBlocks1
IfEndOfBody1:
    pushq $2
    popq %rcx
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rbx
    
    cmpq %rbx, %rcx
    jne IfEndOfBody2
    callq release_scope_header # LABEL
    pushq $1
    popq %rax
    movq %rbp, %rsp
    popq %rbp
    retq
    
IfEndOfBody2:
    jmp EndOfIfBlocks1
EndOfIfBlocks1:
    callq release_scope_header # LABEL
    pushq $1
    popq %rcx
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rbx
    
    subq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    callq fib # LABEL
    pushq %rax
    pushq $2
    popq %rcx
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rbx
    
    subq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    callq fib # LABEL
    pushq %rax
    popq %rcx
    popq %rbx
    addq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    popq %rax
    movq %rbp, %rsp
    popq %rbp
    retq
    
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    movq %rbp, %rsp
    popq %rbp
    retq
    
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $24, %rsp
    callq initialize_heap # LABEL
    movq $0, %rdi
    leaq -8(%rbp), %rsi
    pushq $0
    leaq (%rsp), %rdx
    callq new_scope_header # LABEL
    addq $1, %rsp
    leaq fib, %rbx
    
    # [[ Storing variable fib [-2] ]] 
    # [[ offset is -2 ]] 
    movq %rbx, -16(%rbp)
    
    pushq $4
    popq %rax
    
    # [[ Storing variable n [-3] ]] 
    # [[ offset is -3 ]] 
    movq %rax, -24(%rbp)
    
    
    # [[ Loading variable n ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rax
    
    pushq %rax
    
    # [[ Loading variable n ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rax
    
    pushq %rax
    callq fib # LABEL
    pushq %rax

# Setup Print

    movq $string1, %rdi
    movq $4, %rsi
    movq $0, %rdx
    callq print_string
    
    # [[ Loading variable n ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rdi
    
    callq print_num
    movq $string1, %rdi
    movq $18, %rsi
    movq $5, %rdx
    callq print_string
    popq %rdi
    callq print_num
    movq $string1, %rdi
    movq $1, %rsi
    movq $24, %rdx
    callq print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
