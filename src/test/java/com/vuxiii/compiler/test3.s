.section .data
string0: .ascii "Number 1 is: %\n"
string0subs: .ascii ""
string0stops: .space 16
string1: .ascii "Number 2 is: %\n"
string1subs: .ascii ""
string1stops: .space 16
string2: .ascii "Number 3 is: %\n"
string2subs: .ascii ""
string2stops: .space 16
.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    movq %rsp, %rsp
    pushq $7
    popq %rax
    
    # [[ Storing variable first ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    
    pushq $4
    pushq $2
    
    # [[ Loading variable first ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rcx
    
    popq %rbx
    movq %rbx, %rax
    imulq %rcx
    movq %rax, %rax
    pushq %rax
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax

# Setup Print

    movq $string0, %rdi
    movq $13, %rsi
    movq $0, %rdx
    call print_string
    movq (%rsp), %rdi
    call print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $14, %rdx
    call print_string
    addq $8, %rsp
    movq %rsp, %rsp

# End Print

    pushq $2
    
    # [[ Loading variable first ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rcx
    
    popq %rbx
    movq %rbx, %rax
    imulq %rcx
    movq %rax, %rax
    pushq %rax
    pushq $4
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax

# Setup Print

    movq $string1, %rdi
    movq $13, %rsi
    movq $0, %rdx
    call print_string
    movq (%rsp), %rdi
    call print_num
    movq $string1, %rdi
    movq $1, %rsi
    movq $14, %rdx
    call print_string
    addq $8, %rsp
    movq %rsp, %rsp

# End Print

    pushq $2
    pushq $3
    
    # [[ Loading variable first ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rcx
    
    popq %rbx
    movq %rbx, %rax
    imulq %rcx
    movq %rax, %rax
    pushq %rax
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax

# Setup Print

    movq $string2, %rdi
    movq $13, %rsi
    movq $0, %rdx
    call print_string
    movq (%rsp), %rdi
    call print_num
    movq $string2, %rdi
    movq $1, %rsi
    movq $14, %rdx
    call print_string
    addq $8, %rsp
    movq %rsp, %rsp

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
