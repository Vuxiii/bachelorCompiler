.section .data
.section .text
a:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    push $2
    pop %rax
    movq %rax, %rdi
    call printNum
    movq %rbp, %rsp # Restore stackpointer
    pop %rax
    ret
.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    push $0 # Making place for variable: a
    push $0 # Making place for variable: my_func
    call a
    movq %rbp, %rsp # Restore stackpointer
    pop %rax
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
