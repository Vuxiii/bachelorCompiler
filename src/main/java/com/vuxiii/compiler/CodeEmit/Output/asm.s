.section .data
.section .text
function:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    push $2
    pop %rax
    
    # [[ Storing variable third ]] 
    # [[ offset is 2 ]] 
    movq %rax, -16(%rbp)
    
    # [[ Loading variable third ]] 
    # [[ offset is 2 ]] 
    movq -16(%rbp), %rcx
    
    # [[ Loading variable second ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rbx
    movq %rbx, %rax
    imulq %rcx
    movq %rax, %rax
    push %rax
    pop %rcx
    
    # [[ Loading variable first ]] 
    # [[ offset is 3 ]] 
    movq 24(%rbp), %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    push %rax
    pop %rax
    movq %rax, %rdi
    call printNum
    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    ret
    
.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    push $10
    push $20
    call function
    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
