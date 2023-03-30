.section .data
string0: .ascii " %\n"
string0_stops: .space 16
string0_subs: .space 8
.section .text
f1:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $24, %rsp
    push $0
    pop %rax
    
    # [[ Storing variable param ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    push $1
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    push $2
    pop %rax
    
    # [[ Storing variable b ]] 
    # [[ offset is 3 ]] 
    movq %rax, -24(%rbp)
    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    ret
    
.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $24, %rsp
    movq %rsp, %rsp
    push $42
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 1 ]] 
    movq %rax, -8(%rbp)
    push $69
    pop %rax
    
    # [[ Storing variable b ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    push $2
    push $3
    push $5
    pop %rcx
    pop %rbx
    movq %rbx, %rax
    imulq %rcx
    movq %rax, %rax
    push %rax
    pop %rcx
    pop %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    push %rax

# Setup Print

    movq $string0, %rdi
    leaq string0_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string0_subs, %rdx
    pop %rax
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
