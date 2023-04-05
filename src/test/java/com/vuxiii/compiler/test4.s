.section .data
string0: .ascii "Hej %, du bor i %. Du er % aar. I Januar var du % aar gammel!\n"
string0subs: .ascii "William\0Odense\0\0\0"
.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $0, %rsp
    movq %rsp, %rsp
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
    movq 8(%rsp), %rdi
    call print_num
    movq $string0, %rdi
    movq $22, %rsi
    movq $26, %rdx
    call print_string
    movq (%rsp), %rdi
    call print_num
    movq $string0, %rdi
    movq $13, %rsi
    movq $49, %rdx
    call print_string
    addq $16, %rsp
    movq %rsp, %rsp

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
