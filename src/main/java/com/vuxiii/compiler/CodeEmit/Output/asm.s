.section .data
string0: .ascii "If"
string0stops: .space 8
string0subs: .space 0
string2: .ascii "Else if 2"
string2stops: .space 8
string2subs: .space 0
string1: .ascii "Else if 1"
string1stops: .space 8
string1subs: .space 0
string3: .ascii "Else %"
string3stops: .space 16
string3subs: .space 8
.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $0, %rsp
    movq %rsp, %rsp
    pushq $4
    pushq $4
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax
    cmpq $1, %rax
    jne IfEndOfBody1

# Setup Print

    popq %rdi
    call printStringWithReplace

# End Print

    jmp EndOfIfBlocks1
IfEndOfBody1:
    pushq $5
    pushq $5
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax
    cmpq $1, %rax
    jne IfEndOfBody2

# Setup Print

    popq %rdi
    call printStringWithReplace

# End Print

    jmp EndOfIfBlocks1
IfEndOfBody2:
    pushq $5
    pushq $50
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax
    cmpq $1, %rax
    jne IfEndOfBody3

# Setup Print

    popq %rdi
    call printStringWithReplace

# End Print

    jmp EndOfIfBlocks1
IfEndOfBody3:
    pushq $3

# Setup Print

    movq $string3, %rdi
    leaq string3stops, %rsi
    movq $5, (%rsi)
    movq $6, 8(%rsi)
    leaq string3subs, %rdx
    popq %rax
    movq %rax, (%rdx)
    movq $1, %rcx
    call printStringWithReplace

# End Print

    jmp EndOfIfBlocks1
EndOfIfBlocks1:
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
