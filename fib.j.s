.section .data
# [ String Buffers and Substitutes ]
string1: .ascii "fib(%) = %\n"
string1subs: .ascii ""
string0: .ascii "fib(%) = %\n"
string0subs: .ascii ""
string2: .ascii "The %th fib number is: %\n"
string2subs: .ascii ""

# [ Pointers to Record Layouts ]

.section .text
fib:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $24, %rsp
    movq $0, %rdi
    leaq -8(%rbp), %rsi
    pushq $0
    leaq (%rsp), %rdx
    callq new_scope_header # LABEL
    addq $1, %rsp
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
    popq %rax
    
    # [[ Storing variable fib1 [-3] ]] 
    # [[ offset is -3 ]] 
    movq %rax, -24(%rbp)
    
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
    popq %rax
    
    # [[ Storing variable fib2 [-4] ]] 
    # [[ offset is -4 ]] 
    movq %rax, -32(%rbp)
    
    pushq $1
    popq %rcx
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rbx
    
    subq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    
    # [[ Loading variable fib1 ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string0, %rdi
    movq $4, %rsi
    movq $0, %rdx
    callq print_string
    movq 8(%rsp), %rdi
    callq print_num
    movq $string0, %rdi
    movq $4, %rsi
    movq $5, %rdx
    callq print_string
    movq (%rsp), %rdi
    callq print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $10, %rdx
    callq print_string

# End Print

    pushq $2
    popq %rcx
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rbx
    
    subq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    
    # [[ Loading variable fib2 ]] 
    # [[ offset is -4 ]] 
    movq -32(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string1, %rdi
    movq $4, %rsi
    movq $0, %rdx
    callq print_string
    movq 8(%rsp), %rdi
    callq print_num
    movq $string1, %rdi
    movq $4, %rsi
    movq $5, %rdx
    callq print_string
    movq (%rsp), %rdi
    callq print_num
    movq $string1, %rdi
    movq $1, %rsi
    movq $10, %rdx
    callq print_string

# End Print

    callq release_scope_header # LABEL
    
    # [[ Loading variable fib2 ]] 
    # [[ offset is -4 ]] 
    movq -32(%rbp), %rcx
    
    
    # [[ Loading variable fib1 ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rbx
    
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
    
    pushq $7
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

    movq $string2, %rdi
    movq $4, %rsi
    movq $0, %rdx
    callq print_string
    movq 8(%rsp), %rdi
    callq print_num
    movq $string2, %rdi
    movq $18, %rsi
    movq $5, %rdx
    callq print_string
    movq (%rsp), %rdi
    callq print_num
    movq $string2, %rdi
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
