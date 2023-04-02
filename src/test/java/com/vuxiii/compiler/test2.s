.section .data
string1: .ascii " %\n"
string1_stops: .space 16
string1_subs: .space 8
string2: .ascii " %\n"
string2_stops: .space 16
string2_subs: .space 8
string0: .ascii " %\n"
string0_stops: .space 16
string0_subs: .space 8
.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    movq %rsp, %rsp
    pushq $0
    popq %rax
    
    # [[ Storing variable bas ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    
    pushq $1
    popq %rax
    pushq $1
    cmpq $1, %rax
    jne IfEndOfBody1
    pushq $1
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp IfEndOfBody1
    jmp EndOfIfBlocks1
    pushq $0
    popq %rax
    pushq $0
    cmpq $1, %rax
    jne IfEndOfBody2
    pushq $2
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp IfEndOfBody2
    jmp EndOfIfBlocks1
    pushq $3
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks1
EndOfIfBlocks1:
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string0, %rdi
    leaq string0_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string0_subs, %rdx
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    movq %rax, (%rdx)
    movq $1, %rcx
    call printStringWithReplace

# End Print

    pushq $0
    popq %rax
    pushq $0
    cmpq $1, %rax
    jne IfEndOfBody3
    pushq $1
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp IfEndOfBody3
    jmp EndOfIfBlocks3
    pushq $1
    popq %rax
    pushq $1
    cmpq $1, %rax
    jne IfEndOfBody4
    pushq $2
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp IfEndOfBody4
    jmp EndOfIfBlocks3
    pushq $3
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks3
EndOfIfBlocks3:
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string1, %rdi
    leaq string1_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string1_subs, %rdx
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    movq %rax, (%rdx)
    movq $1, %rcx
    call printStringWithReplace

# End Print

    pushq $0
    popq %rax
    pushq $0
    cmpq $1, %rax
    jne IfEndOfBody5
    pushq $1
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp IfEndOfBody5
    jmp EndOfIfBlocks5
    pushq $0
    popq %rax
    pushq $0
    cmpq $1, %rax
    jne IfEndOfBody6
    pushq $2
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp IfEndOfBody6
    jmp EndOfIfBlocks5
    pushq $3
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks5
EndOfIfBlocks5:
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string2, %rdi
    leaq string2_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string2_subs, %rdx
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    movq %rax, (%rdx)
    movq $1, %rcx
    call printStringWithReplace

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
