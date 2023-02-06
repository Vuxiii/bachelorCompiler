.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    push $0 # Making place for variable: b
    push $4
    pop %rax
    
    # [[ Storing variable b ]] 
    # [[ offset is 0 ]] 
    movq $0, %rcx
    movq %rax, -8(%rbp, %rcx, 8)
    push $2
    pop %rcx
    
    # [[ Loading variable b ]] 
    # [[ offset is 0 ]] 
    movq $0, %rbx
    movq -8(%rbp, %rbx, 8), %rbx
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
    movq %rbp, %rsp # Restore stackpointer
    pop %rax
    # Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
