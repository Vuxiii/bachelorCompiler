.section .data
string0: .ascii "%\n"
string0subs: .ascii ""
string2: .ascii "%\n"
string2subs: .ascii ""
string1: .ascii "%\n"
string1subs: .ascii ""
.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    movq %rsp, %rsp
    pushq $0
    popq %rax
    
    # [[ Storing variable bas ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    
    pushq $1
    popq %rax
    pushq $1
    pushq $1
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks1
IfEndOfBody1:
    pushq $0
    popq %rax
    pushq $0
    pushq $2
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks2
IfEndOfBody2:
    pushq $3
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks2
EndOfIfBlocks2:
    jmp EndOfIfBlocks1
EndOfIfBlocks1:
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
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

    pushq $0
    popq %rax
    pushq $0
    pushq $1
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks3
IfEndOfBody3:
    pushq $1
    popq %rax
    pushq $1
    pushq $2
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks4
IfEndOfBody4:
    pushq $3
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks4
EndOfIfBlocks4:
    jmp EndOfIfBlocks3
EndOfIfBlocks3:
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string1, %rdi
    movq $0, %rsi
    movq $0, %rdx
    call print_string
    movq (%rsp), %rdi
    call print_num
    movq $string1, %rdi
    movq $1, %rsi
    movq $1, %rdx
    call print_string
    addq $8, %rsp
    movq %rsp, %rsp

# End Print

    pushq $0
    popq %rax
    pushq $0
    pushq $1
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks5
IfEndOfBody5:
    pushq $0
    popq %rax
    pushq $0
    pushq $2
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks6
IfEndOfBody6:
    pushq $3
    popq %rax
    
    # [[ Storing variable a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    jmp EndOfIfBlocks6
EndOfIfBlocks6:
    jmp EndOfIfBlocks5
EndOfIfBlocks5:
    
    # [[ Loading variable a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    pushq %rax

# Setup Print

    movq $string2, %rdi
    movq $0, %rsi
    movq $0, %rdx
    call print_string
    movq (%rsp), %rdi
    call print_num
    movq $string2, %rdi
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
