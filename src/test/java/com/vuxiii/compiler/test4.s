.section .data
# [ String Buffers and Substitutes ]
string0: .ascii "Hej %, du bor i %. Du er % aar. I Januar var du % aar gammel!\n"
string0subs: .ascii "William\0Odense\0"

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
    pushq $23
    pushq $22

# Setup Print

    movq $string0, %rdi
    movq $4, %rsi
    movq $0, %rdx
    call print_string
    movq $string0subs, %rdi
    movq $7, %rsi
    movq $0, %rdx
    call print_string
    movq $string0, %rdi
    movq $11, %rsi
    movq $5, %rdx
    call print_string
    movq $string0subs, %rdi
    movq $6, %rsi
    movq $8, %rdx
    call print_string
    movq $string0, %rdi
    movq $8, %rsi
    movq $17, %rdx
    call print_string
    movq $23, %rdi
    call print_num
    movq $string0, %rdi
    movq $22, %rsi
    movq $26, %rdx
    call print_string
    movq $22, %rdi
    call print_num
    movq $string0, %rdi
    movq $13, %rsi
    movq $49, %rdx
    call print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
