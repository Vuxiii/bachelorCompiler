.section .data
string0: .ascii "Yay"
.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $0, %rsp
    movq %rsp, %rsp
    pushq $1
    popq %rax
    pushq $1
    cmpq $1, %rax
    jne IfEndOfBody1

# Setup Print

    movq $string0, %rdi
    movq $3, %rsi
    movq $0, %rdx
    call print_string

# End Print

IfEndOfBody1:
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
