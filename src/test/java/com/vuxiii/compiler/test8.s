.section .data
# [ String Buffers and Substitutes ]
string24: .ascii "Field b is: %\n"
string24subs: .ascii ""
string23: .ascii "Field a is: %\n"
string23subs: .ascii ""
string25: .ascii "Field c is: %\n"
string25subs: .ascii ""

# [ Pointers to Record Layouts ]
heap1: .space 8

.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $32, %rsp
    callq initialize_heap
    movq $0, %rdi
    leaq -8(%rbp), %rsi
    pushq $0
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
    movq %rax, heap1
    pushq $42
    popq %rax
    
    # [[ Storing variable rec.a [-2] ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    pushq $69
    popq %rax
    
    # [[ Storing variable rec.b [-3] ]] 
    # [[ offset is -3 ]] 
    movq %rax, -24(%rbp)
    
    pushq $512
    popq %rax
    
    # [[ Storing variable rec.c [-4] ]] 
    # [[ offset is -4 ]] 
    movq %rax, -32(%rbp)
    

# Setup Print

    movq $string23, %rdi
    movq $12, %rsi
    movq $0, %rdx
    callq print_string
    
    # [[ Loading variable rec.a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rdi
    
    callq print_num
    movq $string23, %rdi
    movq $1, %rsi
    movq $13, %rdx
    callq print_string

# End Print


# Setup Print

    movq $string24, %rdi
    movq $12, %rsi
    movq $0, %rdx
    callq print_string
    
    # [[ Loading variable rec.b ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rdi
    
    callq print_num
    movq $string24, %rdi
    movq $1, %rsi
    movq $13, %rdx
    callq print_string

# End Print


# Setup Print

    movq $string25, %rdi
    movq $12, %rsi
    movq $0, %rdx
    callq print_string
    
    # [[ Loading variable rec.c ]] 
    # [[ offset is -4 ]] 
    movq -32(%rbp), %rdi
    
    callq print_num
    movq $string25, %rdi
    movq $1, %rsi
    movq $13, %rdx
    callq print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
