.section .data
.section .text
.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    push $0 # Making place for variable: my_function
    push $0 # Making place for variable: a
    push $3
    pop %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is 1 ]] 
    movq $1, %rcx
    movq %rax, -8(%rbp, %rcx, 8)
    movq %rbp, %rsp # Restore stackpointer
    pop %rax
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
