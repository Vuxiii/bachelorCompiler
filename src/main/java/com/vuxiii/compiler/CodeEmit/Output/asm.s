.section .data
string0: .ascii " %\n"
string0_stops: .space 16
string0_subs: .space 8
.section .text
f1:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $8, %rsp
    pushq $10
    
    # [[ Loading variable param ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rcx
    
    popq %rbx
    addq %rbx, %rcx
    movq %rcx, %rax
    pushq %rax
    popq %rbx
    movq %rbx, 16(%rbp)
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    retq
    
.section .data
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $8, %rsp
    movq %rsp, %rsp
    subq $24, %rsp
    movq %rsp, %rsp
    pushq $42
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    
    pushq $69
    popq %rax
    
    # [[ Storing variable b ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    popq %rbx
    movq %rbx, 16(%rbp)
    pushq $4242
    callq f1
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    

# Setup Print

    movq $string0, %rdi
    leaq string0_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string0_subs, %rdx
    
    # [[ Loading variable a ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rax
    
    movq %rax, (%rdx)
    movq $1, %rcx
    call printStringWithReplace

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
