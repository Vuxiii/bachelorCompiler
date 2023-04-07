.section .data
# [ String Buffers and Substitutes ]
string2: .ascii "Field c is: %\n"
string2subs: .ascii ""
string1: .ascii "Field b is: %\n"
string1subs: .ascii ""
string0: .ascii "Field a is: %\n"
string0subs: .ascii ""

# [ Pointers to Record Layouts ]
nested: .space 8

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
    pushq $2
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp
    movq $1, %rdi
    pushq $8
    leaq (%rsp), %rsi
    callq new_layout
    addq $1, %rsp
    movq %rax, nested
    pushq $42
    popq %rax
    
    # [[ Storing variable rec.a ]] 
    # [[ offset is -2 ]] 
    movq %rax, -16(%rbp)
    
    pushq $69
    popq %rax
    
    # [[ Storing variable rec.b ]] 
    # [[ offset is -3 ]] 
    movq %rax, -24(%rbp)
    
    pushq $512
    popq %rax
    
    # [[ Storing variable rec.c ]] 
    # [[ offset is -4 ]] 
    movq %rax, -32(%rbp)
    

# Setup Print

    movq $string0, %rdi
    movq $12, %rsi
    movq $0, %rdx
    call print_string
    
    # [[ Loading variable rec.a ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rdi
    
    call print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $13, %rdx
    call print_string

# End Print


# Setup Print

    movq $string1, %rdi
    movq $12, %rsi
    movq $0, %rdx
    call print_string
    
    # [[ Loading variable rec.b ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rdi
    
    call print_num
    movq $string1, %rdi
    movq $1, %rsi
    movq $13, %rdx
    call print_string

# End Print


# Setup Print

    movq $string2, %rdi
    movq $12, %rsi
    movq $0, %rdx
    call print_string
    
    # [[ Loading variable rec.c ]] 
    # [[ offset is -4 ]] 
    movq -32(%rbp), %rdi
    
    call print_num
    movq $string2, %rdi
    movq $1, %rsi
    movq $13, %rdx
    call print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
