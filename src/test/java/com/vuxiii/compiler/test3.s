.section .data
string1: .ascii " %\n"
string1_stops: .space 16
string1_subs: .space 8
string0: .ascii " %\n"
string0_stops: .space 16
string0_subs: .space 8
string2: .ascii " %\n"
string2_stops: .space 16
string2_subs: .space 8
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
    leaq string0_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string0_subs, %rdx
    popq %rax
    movq %rax, (%rdx)
    movq $1, %rcx
    call printStringWithReplace

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
    leaq string1_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string1_subs, %rdx
    popq %rax
    movq %rax, (%rdx)
    movq $1, %rcx
    call printStringWithReplace

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
    leaq string2_stops, %rsi
    movq $1, (%rsi)
    movq $3, 8(%rsi)
    leaq string2_subs, %rdx
    popq %rax
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
