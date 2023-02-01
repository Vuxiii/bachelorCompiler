.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    push $0 # Making place for variable: a
    push $0 # Making place for variable: c
    push $4
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 0 ]] 
    movq $0, %rcx
    movq %rax, -8(%rbp, %rcx, 8)
    
    # [[ Loading variable a ]] 
    # [[ offset is 0 ]] 
    movq $0, %rax
    movq -8(%rbp, %rax, 8), %rax
    movq %rax, %rdi
    call printNum
    push $2
    push $3
    push $7
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
    pop %rax
    
    # [[ Storing variable c ]] 
    # [[ offset is 1 ]] 
    movq $1, %rcx
    movq %rax, -8(%rbp, %rcx, 8)
    
    # [[ Loading variable c ]] 
    # [[ offset is 1 ]] 
    movq $1, %rax
    movq -8(%rbp, %rax, 8), %rax
    movq %rax, %rdi
    call printNum
    movq %rbp, %rsp # Restore stackpointer
    pop %rax
    # Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
