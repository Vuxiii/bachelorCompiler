.section .data
# [ String Buffers and Substitutes ]

# [ Pointers to Record Layouts ]
heap3: .space 8
heap2: .space 8
heap4: .space 8
heap0: .space 8
heap1: .space 8

.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $24, %rsp
    callq initialize_heap
    movq $2, %rdi
    leaq -8(%rbp), %rsi
    pushq $6
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp

# Number of Bitfields

    movq $1, %rdi

# Bitfields

    pushq $0
    leaq (%rsp), %rsi
    callq new_layout
    addq $1, %rsp
    movq %rax, heap3

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
    movq %rax, heap4

# Number of Bitfields

    movq $1, %rdi

# Bitfields

    pushq $6
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
    movq %rax, heap1
    movq $1, %rdi
    movq $heap3, %rsi
    callq new_ptr_size
    
    # [[ Storing variable f1 ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    
    movq $1, %rdi
    movq $heap4, %rsi
    callq new_ptr_size
    
    # [[ Storing variable f3 ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    movq $1, %rdi
    movq $heap3, %rsi
    callq new_ptr_size
    
    # [[ Storing variable f1 ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    
    movq $1, %rdi
    movq $heap4, %rsi
    callq new_ptr_size
    
    # [[ Storing variable f3 ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
