.section .data
# [ String Buffers and Substitutes ]
string0: .ascii "b.f1: %\n"
string0subs: .ascii ""
string1: .ascii "b.f2: %\n"
string1subs: .ascii ""
string2: .ascii "b.f3: %\n"
string2subs: .ascii ""

# [ Pointers to Record Layouts ]
heap3: .space 8
heap1: .space 8
heap2: .space 8

.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $32, %rsp
    callq initialize_heap
    movq $2, %rdi
    leaq -8(%rbp), %rsi
    pushq $136
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp

# Number of Bitfields

    movq $1, %rdi

# Bitfields

    pushq $2
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
    movq %rax, heap1

# Number of Bitfields

    movq $1, %rdi

# Bitfields

    pushq $4
    leaq (%rsp), %rsi
    callq new_layout
    addq $1, %rsp
    movq %rax, heap2
    movq $1, %rdi
    movq $heap3, %rsi
    callq new_ptr_size
    
    # [[ Storing variable b.f2 ]] 
    # [[ offset is 2 ]] 
    movq %rax, 16(%rbp)
    
    pushq $6
    popq %rax
    
    # [[ Storing variable b.f1 ]] 
    # [[ offset is 1 ]] 
    movq %rax, 8(%rbp)
    
    pushq $7
    popq %rax
    
    # [[ Storing variable b.f2 ]] 
    # [[ offset is 2 ]] 
    movq %rax, 16(%rbp)
    
    pushq $8
    popq %rax
    
    # [[ Storing variable b.f3 ]] 
    # [[ offset is 3 ]] 
    movq %rax, 24(%rbp)
    

# Setup Print

    movq $string0, %rdi
    movq $6, %rsi
    movq $0, %rdx
    callq print_string
    
    # [[ Loading variable b.f1 ]] 
    # [[ offset is 1 ]] 
    movq 8(%rbp), %rdi
    
    callq print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $7, %rdx
    callq print_string

# End Print


# Setup Print

    movq $string1, %rdi
    movq $6, %rsi
    movq $0, %rdx
    callq print_string
    
    # [[ Loading variable b.f2 ]] 
    # [[ offset is 2 ]] 
    movq 16(%rbp), %rdi
    
    callq print_num
    movq $string1, %rdi
    movq $1, %rsi
    movq $7, %rdx
    callq print_string

# End Print


# Setup Print

    movq $string2, %rdi
    movq $6, %rsi
    movq $0, %rdx
    callq print_string
    
    # [[ Loading variable b.f3 ]] 
    # [[ offset is 3 ]] 
    movq 24(%rbp), %rdi
    
    callq print_num
    movq $string2, %rdi
    movq $1, %rsi
    movq $7, %rdx
    callq print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
