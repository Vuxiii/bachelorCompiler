.section .data
# [ String Buffers and Substitutes ]
string4: .ascii "true == true -> true\n"
string12: .ascii "end\n"
string9: .ascii "false == false -> false\n"
string11: .ascii "false != false -> false\n"
string5: .ascii "true == true -> false\n"
string0: .ascii "2 + 2 == 5 -> true\n"
string2: .ascii "2 + 2 != 5 -> true\n"
string1: .ascii "2 + 2 == 5 -> false\n"
string6: .ascii "true != true -> true\n"
string3: .ascii "2 + 2 != 5 -> false\n"
string8: .ascii "false == false -> true\n"
string7: .ascii "true != true -> false\n"
string10: .ascii "false != false -> true\n"

# [ Pointers to Record Layouts ]

.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $0, %rsp
    callq initialize_heap
    movq $1, %rdi
    leaq -8(%rbp), %rsi
    pushq $2
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp
    pushq $2
    pushq $2
    popq %rcx
    popq %rbx
    addq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    pushq $5
    popq %rcx
    popq %rbx
    cmpq %rbx, %rcx
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
    addq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax
    pushq $5
    popq %rcx
    popq %rbx
    cmpq %rbx, %rcx
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
    pushq $1
    pushq $1
    popq %rcx
    popq %rbx
    cmpq %rbx, %rcx
    jne IfEndOfBody3

# Setup Print

    movq $string4, %rdi
    movq $21, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks3
IfEndOfBody3:

# Setup Print

    movq $string5, %rdi
    movq $22, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks3
EndOfIfBlocks3:
    pushq $1
    pushq $1
    popq %rcx
    popq %rbx
    cmpq %rbx, %rcx
    je IfEndOfBody4

# Setup Print

    movq $string6, %rdi
    movq $21, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks4
IfEndOfBody4:

# Setup Print

    movq $string7, %rdi
    movq $22, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks4
EndOfIfBlocks4:
    pushq $0
    pushq $0
    popq %rcx
    popq %rbx
    cmpq %rbx, %rcx
    jne IfEndOfBody5

# Setup Print

    movq $string8, %rdi
    movq $23, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks5
IfEndOfBody5:

# Setup Print

    movq $string9, %rdi
    movq $24, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks5
EndOfIfBlocks5:
    pushq $0
    pushq $0
    popq %rcx
    popq %rbx
    cmpq %rbx, %rcx
    je IfEndOfBody6

# Setup Print

    movq $string10, %rdi
    movq $23, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks6
IfEndOfBody6:

# Setup Print

    movq $string11, %rdi
    movq $24, %rsi
    movq $0, %rdx
    call print_string

# End Print

    jmp EndOfIfBlocks6
EndOfIfBlocks6:

# Setup Print

    movq $string12, %rdi
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
