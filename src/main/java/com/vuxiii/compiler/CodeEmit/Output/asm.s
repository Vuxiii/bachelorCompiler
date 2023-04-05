.section .data
string3: .ascii "2 + 2 != 5 -> false\n"
string0: .ascii "2 + 2 == 5 -> true\n"
string1: .ascii "2 + 2 == 5 -> false\n"
string2: .ascii "2 + 2 != 5 -> true\n"
string4: .ascii "end\n"
.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $0, %rsp
    movq %rsp, %rsp
    pushq $2
    pushq $2
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax
    pushq $5
    popq %rcx
    popq %rbx
    cmpq %rcx, %rbx
    jne IfEndOfBody1

# Setup Print

    movq $string0, %rdi
    movq $19, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks1
IfEndOfBody1:

# Setup Print

    movq $string1, %rdi
    movq $20, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks1
EndOfIfBlocks1:
    pushq $2
    pushq $2
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax
    pushq $5
    popq %rcx
    popq %rbx
    cmpq %rcx, %rbx
    je IfEndOfBody2

# Setup Print

    movq $string2, %rdi
    movq $19, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks2
IfEndOfBody2:

# Setup Print

    movq $string3, %rdi
    movq $20, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks2
EndOfIfBlocks2:

# Setup Print

    movq $string4, %rdi
    movq $4, %rsi
    movq $0, %rdx
    call print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
