.section .data
# [ String Buffers and Substitutes ]
string2: .ascii "a.f3: %\n"
string2subs: .ascii ""
string1: .ascii "a.f2: %\n"
string1subs: .ascii ""
string0: .ascii "a.f1: %\n"
string0subs: .ascii ""

# [ Pointers to Record Layouts ]
heap2: .space 8
heap1: .space 8

.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $32, %rsp
    callq initialize_heap
    movq $1, %rdi
    leaq -8(%rbp), %rsi
    pushq $16
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
    movq $heap2, %rsi
    callq new_ptr_size
    
    # [[ Storing variable a ]] 
    # [[ offset is -1 ]] 
    movq %rax, -8(%rbp)
    
    pushq $1
    popq %rax
    
    # [[ Loading variable a.f1 ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rbx
    
    movq %rax, 16(%rbx)
    pushq $2
    popq %rax
    
    # [[ Loading variable a.f2 ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rbx
    
    movq %rax, 24(%rbx)
    pushq $3
    popq %rax
    
    # [[ Loading variable a.f3 ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rbx
    
    movq %rax, 32(%rbx)

# Setup Print

    movq $string0, %rdi
    movq $6, %rsi
    movq $0, %rdx
    callq print_string
    
    # [[ Loading variable a.f1 ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rdi
    
    movq 16(%rdi), %rdi
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
    
    # [[ Loading variable a.f2 ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rdi
    
    movq 24(%rdi), %rdi
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
    
    # [[ Loading variable a.f3 ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rdi
    
    movq 32(%rdi), %rdi
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
