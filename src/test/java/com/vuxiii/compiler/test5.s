.section .data
# [ String Buffers and Substitutes ]
string4: .ascii "Else if\n"
string0: .ascii "If\n"
string1: .ascii "If\n"
string2: .ascii "Else\n"
string3: .ascii "If\n"
string6: .ascii "Fix ending...\n"
string5: .ascii "Else\n"

# [ Pointers to Record Layouts ]

.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $0, %rsp
    callq initialize_heap
    movq $0, %rdi
    leaq -8(%rbp), %rsi
    pushq $0
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp
    pushq $1
    popq %rax
    pushq $1
    popq %rax
    cmpq $0, %rax
    je IfEndOfBody1

# Setup Print

    movq $string0, %rdi
    movq $3, %rsi
    movq $0, %rdx
    callq print_string

# End Print

IfEndOfBody1:
    pushq $4
    pushq $4
    popq %rcx
    popq %rbx
    subq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    popq %rax
    cmpq $0, %rax
    je IfEndOfBody2

# Setup Print

    movq $string1, %rdi
    movq $3, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks1
IfEndOfBody2:

# Setup Print

    movq $string2, %rdi
    movq $5, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks1
EndOfIfBlocks1:
    pushq $4
    pushq $4
    popq %rcx
    popq %rbx
    subq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    popq %rax
    cmpq $0, %rax
    je IfEndOfBody3

# Setup Print

    movq $string3, %rdi
    movq $3, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks2
IfEndOfBody3:
    pushq $1
    popq %rax
    pushq $1
    popq %rax
    cmpq $0, %rax
    je IfEndOfBody4

# Setup Print

    movq $string4, %rdi
    movq $8, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks3
IfEndOfBody4:

# Setup Print

    movq $string5, %rdi
    movq $5, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks3
EndOfIfBlocks3:
    jmp EndOfIfBlocks2
EndOfIfBlocks2:

# Setup Print

    movq $string6, %rdi
    movq $14, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
