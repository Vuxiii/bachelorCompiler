.section .data
string1: .ascii " %\n"
string1_stops: .space 16
string1_subs: .space 8
string0: .ascii " %\n"
string0_stops: .space 16
string0_subs: .space 8
string2: .ascii " %\n"
string2_stops: .space 16
string2_subs: .space 8
.section .text
.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    movq %rsp, %rsp
    push $0
    pop %rax
    
    # [[ Storing variable bas ]] 
    # [[ offset is 1 ]] 
    movq %rax, -8(%rbp)
    push $1
    pop %rax
    push $1
    cmp $1, %rax
    jne IfEndOfBody1
    push $1
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    jmp EndOfIfBlocks1
IfEndOfBody1:
    push $0
    pop %rax
    push $0
    cmp $1, %rax
    jne IfEndOfBody2
    push $2
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    jmp EndOfIfBlocks1
IfEndOfBody2:
    push $3
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    jmp EndOfIfBlocks1
EndOfIfBlocks1:

# Setup Print

    movq $string0, %rdi
    leaq string0_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string0_subs, %rdx
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq -16(%rbp), %rax
    movq %rax, (%rdx)
    movq $1, %rcx
    call printStringWithReplace

# End Print

    push $0
    pop %rax
    push $0
    cmp $1, %rax
    jne IfEndOfBody3
    push $1
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    jmp EndOfIfBlocks3
IfEndOfBody3:
    push $1
    pop %rax
    push $1
    cmp $1, %rax
    jne IfEndOfBody4
    push $2
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    jmp EndOfIfBlocks3
IfEndOfBody4:
    push $3
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    jmp EndOfIfBlocks3
EndOfIfBlocks3:

# Setup Print

    movq $string1, %rdi
    leaq string1_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string1_subs, %rdx
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq -16(%rbp), %rax
    movq %rax, (%rdx)
    movq $1, %rcx
    call printStringWithReplace

# End Print

    push $0
    pop %rax
    push $0
    cmp $1, %rax
    jne IfEndOfBody5
    push $1
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    jmp EndOfIfBlocks5
IfEndOfBody5:
    push $0
    pop %rax
    push $0
    cmp $1, %rax
    jne IfEndOfBody6
    push $2
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    jmp EndOfIfBlocks5
IfEndOfBody6:
    push $3
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    jmp EndOfIfBlocks5
EndOfIfBlocks5:

# Setup Print

    movq $string2, %rdi
    leaq string2_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string2_subs, %rdx
    
    # [[ Loading variable a ]] 
    # [[ offset is 2 ]] 
    movq -16(%rbp), %rax
    movq %rax, (%rdx)
    movq $1, %rcx
    call printStringWithReplace

# End Print

    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
