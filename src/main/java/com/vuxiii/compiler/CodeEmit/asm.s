.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    push $0 # Making place for variable: a
    push $0 # Making place for variable: string
    push $0 # Making place for variable: name
    push $0 # Making place for variable: age
    movq %rbp, %rsp # Restore stackpointer
    pop %rax
    # Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
