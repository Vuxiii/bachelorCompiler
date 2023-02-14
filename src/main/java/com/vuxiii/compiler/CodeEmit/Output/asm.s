.section .data
string0: .ascii "as: %\n"
string0_stops: .space 16
string0_subs: .space 8
string1: .ascii "as: %\n"
string1_stops: .space 16
string1_subs: .space 8
.section .text
.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $-16, %rsp
    movq %rsp, %rsp
    push $7
    pop %rax
    
    # [[ Storing variable first ]] 
    # [[ offset is 1 ]] 
    movq %rax, -8(%rbp)
    push $2
    
    # [[ Loading variable first ]] 
    # [[ offset is 1 ]] 
    movq -8(%rbp), %rcx
    pop %rbx
    movq %rbx, %rax
    imulq %rcx
    movq %rax, %rax
    push %rax
    push $4
    pop %rcx
    pop %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    push %rax
    
    # Setup Print
    
    movq $string0, %rdi
    leaq string0_stops, %rsi
    movq $4, (%rsi)
    movq $9, 8(%rsi)
    leaq string0_subs, %rdx
    pop %rax
    movq %rax, (%rdx)
    movq $1, %rcx
    # call printStringWithReplace
    
    # End Print
    
    push $2
    
    # [[ Loading variable first ]] 
    # [[ offset is 1 ]] 
    movq -8(%rbp), %rcx

    # movq %rcx, %rdi
    # call printNum



    pop %rbx
    movq %rbx, %rax
    imulq %rcx
    movq %rax, %rax
    push %rax
    push $4
    pop %rcx
    pop %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    push %rax
    
    # Setup Print
    
    movq $string1, %rdi
    leaq string1_stops, %rsi
    movq $4, (%rsi)
    movq $9, 8(%rsi)
    leaq string1_subs, %rdx
    pop %rax
    movq %rax, (%rdx)
    movq $1, %rcx
    # call printStringWithReplace
    
    # End Print
    
    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
