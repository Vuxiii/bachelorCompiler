.section .data
.section .text
.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $-8, %rsp
    movq %rsp, %rsp
    push $1
    push $2
    push $3
    pop %rcx
    pop %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    push %rax
    pop %rcx
    pop %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    push %rax
    pop %rdi
    call printNum
    push $5
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 1 ]] 
    movq %rax, -8(%rbp)
    push $69
    
    # [[ Loading variable a ]] 
    # [[ offset is 1 ]] 
    movq -8(%rbp), %rcx
    pop %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    push %rax
    pop %rdi
    call printNum
    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
