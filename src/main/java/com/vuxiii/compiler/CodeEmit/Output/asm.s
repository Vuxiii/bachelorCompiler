.section .data
# [ String Buffers and Substitutes ]
string0: .ascii "p1 is %\n"
string0subs: .ascii ""
string1: .ascii "p2 is %\n"
string1subs: .ascii ""

# [ Pointers to Record Layouts ]
heap0: .space 8
heap2: .space 8
heap1: .space 8

.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $24, %rsp
    callq initialize_heap
    movq $1, %rdi
    leaq -8(%rbp), %rsi
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp

# Number of Bitfields

    movq $1, %rdi

# Bitfields

    pushq $10
    leaq (%rsp), %rsi
    callq new_layout
    addq $1, %rsp
    movq %rax, heap0

# Number of Bitfields

    movq $1, %rdi

# Bitfields

    pushq $0
    leaq (%rsp), %rsi
    callq new_layout
    addq $1, %rsp
    movq %rax, heap2

# Number of Bitfields

    movq $1, %rdi

# Bitfields

    pushq $0
    leaq (%rsp), %rsi
    callq new_layout
    addq $1, %rsp
    movq %rax, heap1
    movq $1, %rdi
    movq $heap1, %rsi
    callq new_ptr_size
    
    # [[ Storing variable p1 ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    
    pushq $2
    popq %rax
    
    # [[ Loading variable p1 ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rbx
    
    movq %rax, 16(%rbx)
    
    # [[ Loading variable p1 ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rax
    
    movq 16(%rax), %rax
    pushq %rax

# Setup Print

    movq $string0, %rdi
    movq $6, %rsi
    movq $0, %rdx
    call print_string
    
    # [[ Loading variable p1 ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rdi
    
    movq 16(%rdi), %rdi
    call print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $7, %rdx
    call print_string

# End Print

    movq $1, %rdi
    movq $heap2, %rsi
    callq new_ptr_size
    
    # [[ Storing variable p2 ]] 
    # [[ offset is -3 ]] 
    movq %rax, -24(%rbp)
    
    pushq $3
    popq %rcx
    
    # [[ Loading variable p1 ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rbx
    
    movq 16(%rbx), %rbx
    movq %rcx, %rax
    imulq %rbx
    movq %rax, %rax
    pushq %rax
    popq %rax
    
    # [[ Loading variable p2 ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rbx
    
    movq %rax, 16(%rbx)
    
    # [[ Loading variable p2 ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rax
    
    movq 16(%rax), %rax
    pushq %rax

# Setup Print

    movq $string1, %rdi
    movq $6, %rsi
    movq $0, %rdx
    call print_string
    
    # [[ Loading variable p2 ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rdi
    
    movq 16(%rdi), %rdi
    call print_num
    movq $string1, %rdi
    movq $1, %rsi
    movq $7, %rdx
    call print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
