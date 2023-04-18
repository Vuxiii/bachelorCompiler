.section .data
# [ String Buffers and Substitutes ]
string0: .ascii "%\n"
string0subs: .ascii ""
string2: .ascii "%\n"
string2subs: .ascii ""
string1: .ascii "%\n"
string1subs: .ascii ""

# [ Pointers to Record Layouts ]

.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    callq initialize_heap
    movq $1, %rdi
    leaq -8(%rbp), %rsi
    pushq $2
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp
    pushq $0
    popq %rax
    
    # [[ Storing variable bas ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    
    pushq $1
    popq %rax
    pushq $1
    popq %rax
    cmpq $0, %rax
    je IfEndOfBody1
    pushq $1
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks1
IfEndOfBody1:
    pushq $0
    popq %rax
    pushq $0
    popq %rax
    cmpq $0, %rax
    je IfEndOfBody2
    pushq $2
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks2
IfEndOfBody2:
    pushq $3
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks2
EndOfIfBlocks2:
    jmp EndOfIfBlocks1
EndOfIfBlocks1:
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string0, %rdi
    movq $0, %rsi
    movq $0, %rdx
    call print_string
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rdi
    
    call print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $1, %rdx
    call print_string

# End Print

    pushq $0
    popq %rax
    pushq $0
    popq %rax
    cmpq $0, %rax
    je IfEndOfBody3
    pushq $1
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks3
IfEndOfBody3:
    pushq $1
    popq %rax
    pushq $1
    popq %rax
    cmpq $0, %rax
    je IfEndOfBody4
    pushq $2
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks4
IfEndOfBody4:
    pushq $3
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks4
EndOfIfBlocks4:
    jmp EndOfIfBlocks3
EndOfIfBlocks3:
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string1, %rdi
    movq $0, %rsi
    movq $0, %rdx
    call print_string
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rdi
    
    call print_num
    movq $string1, %rdi
    movq $1, %rsi
    movq $1, %rdx
    call print_string

# End Print

    pushq $0
    popq %rax
    pushq $0
    popq %rax
    cmpq $0, %rax
    je IfEndOfBody5
    pushq $1
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks5
IfEndOfBody5:
    pushq $0
    popq %rax
    pushq $0
    popq %rax
    cmpq $0, %rax
    je IfEndOfBody6
    pushq $2
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks6
IfEndOfBody6:
    pushq $3
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks6
EndOfIfBlocks6:
    jmp EndOfIfBlocks5
EndOfIfBlocks5:
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string2, %rdi
    movq $0, %rsi
    movq $0, %rdx
    call print_string
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rdi
    
    call print_num
    movq $string2, %rdi
    movq $1, %rsi
    movq $1, %rdx
    call print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
