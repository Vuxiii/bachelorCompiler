.section .data
# [ String Buffers and Substitutes ]
string0: .ascii "%\n"
string0subs: .ascii ""

# [ Pointers to Record Layouts ]

.section .text
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $8, %rsp
    callq initialize_heap
    movq $1, %rdi
    leaq -8(%rbp), %rsi
    pushq $2
    leaq (%rsp), %rdx
    callq new_scope_header
    addq $1, %rsp
    movq $1, %rdi
    pushq $2
    movq $, %rsi
    addq $1, %rsp
    pushq $42
    popq %rax
    movq %rax, 16(%rbx)
    
    # [[ Loading variable a ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rax
    
    movq 16(%rax), %rax
    pushq %rax

# Setup Print

    movq $string0, %rdi
    movq $0, %rsi
    movq $0, %rdx
    call print_string
    
    # [[ Loading variable a ]] 
    # [[ offset is -1 ]] 
    movq -8(%rbp), %rdi
    
    movq 16(%rdi), %rdi
    call print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $1, %rdx
    call print_string

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
