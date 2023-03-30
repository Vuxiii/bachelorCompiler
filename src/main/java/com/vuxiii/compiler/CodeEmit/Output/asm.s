.section .data
.section .text
f1:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $24, %rsp
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
    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
