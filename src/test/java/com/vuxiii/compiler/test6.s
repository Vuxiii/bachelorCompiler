.section .data
# [ String Buffers and Substitutes ]
string17: .ascii "false != false -> true\n"
string16: .ascii "false == false -> false\n"
string10: .ascii "2 + 2 != 5 -> false\n"
string12: .ascii "true == true -> false\n"
string19: .ascii "end\n"
string13: .ascii "true != true -> true\n"
string11: .ascii "true == true -> true\n"
string14: .ascii "true != true -> false\n"
string8: .ascii "2 + 2 == 5 -> false\n"
string9: .ascii "2 + 2 != 5 -> true\n"
string7: .ascii "2 + 2 == 5 -> true\n"
string15: .ascii "false == false -> true\n"
string18: .ascii "false != false -> false\n"

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
    jne IfEndOfBody5

# Setup Print

    movq $string7, %rdi
    movq $19, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks4
IfEndOfBody5:

# Setup Print

    movq $string8, %rdi
    movq $20, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks4
EndOfIfBlocks4:
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
    je IfEndOfBody6

# Setup Print

    movq $string9, %rdi
    movq $19, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks5
IfEndOfBody6:

# Setup Print

    movq $string10, %rdi
    movq $20, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks5
EndOfIfBlocks5:
    pushq $1
    pushq $1
    popq %rcx
    popq %rbx
    cmpq %rbx, %rcx
    jne IfEndOfBody7

# Setup Print

    movq $string11, %rdi
    movq $21, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks6
IfEndOfBody7:

# Setup Print

    movq $string12, %rdi
    movq $22, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks6
EndOfIfBlocks6:
    pushq $1
    pushq $1
    popq %rcx
    popq %rbx
    cmpq %rbx, %rcx
    je IfEndOfBody8

# Setup Print

    movq $string13, %rdi
    movq $21, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks7
IfEndOfBody8:

# Setup Print

    movq $string14, %rdi
    movq $22, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks7
EndOfIfBlocks7:
    pushq $0
    pushq $0
    popq %rcx
    popq %rbx
    cmpq %rbx, %rcx
    jne IfEndOfBody9

# Setup Print

    movq $string15, %rdi
    movq $23, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks8
IfEndOfBody9:

# Setup Print

    movq $string16, %rdi
    movq $24, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks8
EndOfIfBlocks8:
    pushq $0
    pushq $0
    popq %rcx
    popq %rbx
    cmpq %rbx, %rcx
    je IfEndOfBody10

# Setup Print

    movq $string17, %rdi
    movq $23, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks9
IfEndOfBody10:

# Setup Print

    movq $string18, %rdi
    movq $24, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    jmp EndOfIfBlocks9
EndOfIfBlocks9:

# Setup Print

    movq $string19, %rdi
    movq $4, %rsi
    movq $0, %rdx
    callq print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
