.section .data
.section .text
.section .data
.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $8, %rsp
    movq %rsp, %rsp
    pop %rax
    
    # [[ Storing variable bas ]] 
    # [[ offset is 1 ]] 
    movq %rax, -8(%rbp)

# Setup Print

    pop %rdi
    call printStringWithReplace

# End Print


# Setup Print

    pop %rdi
    call printStringWithReplace

# End Print

    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
