.section .data
string0: .ascii "%\n"
string0subs: .ascii ""
.section .text
a:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    
    # [[ Loading variable b ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rax
    
    pushq %rax
    callq c
    pushq %rax
    pushq $5
    popq %rcx
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax
    popq %rax
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    retq
    
c:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $8, %rsp
    pushq $4
    popq %rcx
    
    # [[ Loading variable d ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rbx
    
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax
    popq %rax
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    retq
    
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    movq %rsp, %rsp
    pushq $3
    popq %rax
    
    # [[ Storing variable first ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    
    # [[ Loading variable first ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    pushq %rax
    callq a
    pushq %rax
    callq a
    pushq %rax
    popq %rcx
    
    # [[ Loading variable first ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rbx
    
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax

# Setup Print

    movq $string0, %rdi
    movq $0, %rsi
    movq $0, %rdx
    call print_string
    movq (%rsp), %rdi
    call print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $1, %rdx
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
