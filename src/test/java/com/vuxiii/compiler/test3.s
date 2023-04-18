.section .data
# [ String Buffers and Substitutes ]
string1: .ascii "Number 2 is: %\n"
string1subs: .ascii ""
string0: .ascii "Number 1 is: %\n"
string0subs: .ascii ""
string2: .ascii "Number 3 is: %\n"
string2subs: .ascii ""

# [ Pointers to Record Layouts ]

.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $16, %rsp
    callq initialize_heap
    movq $1, %rdi
    leaq -8(%rbp), %rsi
    pushq $2
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp
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
    movq %rcx, %rax
    imulq %rbx
    movq %rax, %rax
    pushq %rax
    popq %rcx
    popq %rbx
    addq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax

# Setup Print

    movq $string0, %rdi
    movq $13, %rsi
    movq $0, %rdx
    call print_string
    popq %rdi
    call print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $14, %rdx
    call print_string

# End Print

    pushq $2
    
    # [[ Loading variable first ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rcx
    
    popq %rbx
    movq %rcx, %rax
    imulq %rbx
    movq %rax, %rax
    pushq %rax
    pushq $4
    popq %rcx
    popq %rbx
    addq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax

# Setup Print

    movq $string1, %rdi
    movq $13, %rsi
    movq $0, %rdx
    call print_string
    popq %rdi
    call print_num
    movq $string1, %rdi
    movq $1, %rsi
    movq $14, %rdx
    call print_string

# End Print

    pushq $2
    pushq $3
    
    # [[ Loading variable first ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rcx
    
    popq %rbx
    movq %rcx, %rax
    imulq %rbx
    movq %rax, %rax
    pushq %rax
    popq %rcx
    popq %rbx
    addq %rcx, %rbx
    movq %rbx, %rax
    pushq %rax

# Setup Print

    movq $string2, %rdi
    movq $13, %rsi
    movq $0, %rdx
    call print_string
    popq %rdi
    call print_num
    movq $string2, %rdi
    movq $1, %rsi
    movq $14, %rdx
    call print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
