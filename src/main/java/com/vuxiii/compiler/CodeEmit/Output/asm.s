.section .data
.section .text
.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $8, %rsp
    movq %rsp, %rsp
    push $1
    pop %rax
    
    # [[ Storing variable bas ]] 
    # [[ offset is 1 ]] 
    movq %rax, -8(%rbp)
    
    # [[ Loading variable bas ]] 
    # [[ offset is 1 ]] 
    movq -8(%rbp), %rax
    cmpq %rax, $1
    jne IfLabelExit1
    push $0
    pop %rax
    
    # [[ Storing variable bas ]] 
    # [[ offset is 1 ]] 
    movq %rax, -8(%rbp)
    jmp IfLabelExit1
IfLabelEnter1:
    push $1
    pop %rax
    
    # [[ Storing variable bas ]] 
    # [[ offset is 1 ]] 
    movq %rax, -8(%rbp)
    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
