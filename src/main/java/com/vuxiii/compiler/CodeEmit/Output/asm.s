.section .data
.section .text
.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    push $7
    pop %rax
    
    # [[ Storing variable first ]] 
    # [[ offset is 1 ]] 
    movq %rax, 8(%rbp)
    push $4
    push $2
    
    # [[ Loading variable first ]] 
    # [[ offset is 1 ]] 
    movq 8(%rbp), %rcx


    movq %rcx, %rdi
    call printNum

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
    
    movq %rax, %rdi
    call printNum
    
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
    pop %rax
    movq %rax, %rdi
    call printNum
    push $2
    push $3
    
    # [[ Loading variable first ]] 
    # [[ offset is 1 ]] 
    movq -8(%rbp), %rcx
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
    movq %rax, %rdi
    call printNum
    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
