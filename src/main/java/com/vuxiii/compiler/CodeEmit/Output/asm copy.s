.section .data
.section .text
function:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    
    push $420 # Will be -8(%rbp)
    push $69 # Will be -16(%rbp)

    movq 16(%rbp), %rdi
    call printNum # 18

    movq 24(%rbp), %rdi
    call printNum # 15

    movq 32(%rbp), %rdi
    call printNum # 15

    movq 48(%rbp), %rdi
    call printNum # 15

    movq 56(%rbp), %rdi
    call printNum # 15

    movq 64(%rbp), %rdi
    call printNum # 15

    movq 72(%rbp), %rdi
    call printNum # 15

    movq 80(%rbp), %rdi
    call printNum # 15

    movq -8(%rbp), %rdi
    call printNum # 420


    movq -16(%rbp), %rdi
    call printNum # 69
   
    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    ret
    
# To access parameter values, +
# To access local values, -

.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    push $0 # Making place for variable: function
    
    push $80 # Will be 24(%rbp)
    push $72 # Will be 24(%rbp)
    push $64 # Will be 24(%rbp)
    push $56 # Will be 24(%rbp)
    push $48 # Will be 24(%rbp)
    push $40 # Will be 24(%rbp)
    push $32 # Will be 24(%rbp)
    push $24 # Will be 24(%rbp)
    push $16 # Will be 16(%rbp)
    call function
    
    movq %rbp, %rsp # Restore stackpointer
    pop %rax
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall


    
.section .data
.section .text
function:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    push $0 # Making place for variable: first
    
    
    # [[ Storing variable second ]] 
    # [[ offset is 2 ]] 
    movq $20, -8(%rbp)
    
    # [[ Loading variable second ]] 
    # [[ offset is 2 ]] 
    movq -8(%rbp), %rcx
    

    movq -8(%rbp), %rdi
    call printNum

    # [[ Loading variable first ]] 
    # [[ offset is 1 ]] 
    movq 16(%rbp), %rbx
    addq %rbx, %rcx
    
    movq %rcx, %rdi
    call printNum
    
    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    ret
    