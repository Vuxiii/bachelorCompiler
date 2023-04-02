.section .data
string0: .ascii "If\n"
string3: .ascii "If\n"
string4: .ascii "Else if\n"
string2: .ascii "Else\n"
string1: .ascii "If\n"
string5: .ascii "Else\n"
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
    pushq $4
    pushq $4
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax
    cmpq $1, %rax
    jne IfEndOfBody2

# Setup Print

    movq $string1, %rdi
    movq $3, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks1
IfEndOfBody2:

# Setup Print

    movq $string2, %rdi
    movq $5, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks1
EndOfIfBlocks1:
    pushq $4
    pushq $4
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax
    cmpq $1, %rax
    jne IfEndOfBody3

# Setup Print

    movq $string3, %rdi
    movq $3, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks2
IfEndOfBody3:
    pushq $1
    popq %rax
    pushq $1
    cmpq $1, %rax
    jne IfEndOfBody4

# Setup Print

    movq $string4, %rdi
    movq $8, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks2
IfEndOfBody4:

# Setup Print

    movq $string5, %rdi
    movq $5, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks2
EndOfIfBlocks2:
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
