.section .data
# [ String Buffers and Substitutes ]
string0: .ascii "a is %\n"
string0subs: .ascii ""

# [ Pointers to Record Layouts ]

.section .text
fun1:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $8, %rsp
    movq $0, %rdi
    leaq -8(%rbp), %rsi
    pushq $0
    leaq (%rsp), %rdx
    callq new_scope_header # LABEL
    addq $1, %rsp
    
    # [[ Loading variable a ]] 
    # [[ offset is 0 ]] 
    movq 0(%rbp), %rax
    
    # [[ offset is -4 ]] 
    movq -32(%rax), %rax
    
    pushq %rax

# Setup Print

    movq $string0, %rdi
    movq $5, %rsi
    movq $0, %rdx
    callq print_string
    
    # [[ Loading variable a ]] 
    # [[ offset is 0 ]] 
    movq 0(%rbp), %rdi
    
    # [[ offset is -4 ]] 
    movq -32(%rdi), %rdi
    
    callq print_num
    movq $string0, %rdi
    movq $1, %rsi
    movq $6, %rdx
    callq print_string

# End Print

    callq release_scope_header # LABEL
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    retq
    
.section .text
.global main
main:
    pushq %rbp
    movq %rsp, %rbp # Setup stackpointer
    subq $32, %rsp
    callq initialize_heap # LABEL
    movq $0, %rdi
    leaq -8(%rbp), %rsi
    pushq $0
    leaq (%rsp), %rdx
    callq new_scope_header # LABEL
    addq $1, %rsp
    pushq $69
    popq %rax
    
    # [[ Storing variable a [-4] ]] 
    # [[ offset is -4 ]] 
    movq %rax, -32(%rbp)
    
    leaq fun1, %rbx
    
    # [[ Storing variable fun1 [-2] ]] 
    # [[ offset is -2 ]] 
    movq %rbx, -16(%rbp)
    
    
    # [[ Loading variable fun1 ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rax
    
    
    # [[ Storing variable fun2 [-3] ]] 
    # [[ offset is -3 ]] 
    movq %rax, -24(%rbp)
    
    
    # [[ Loading variable fun2 ]] 
    # [[ offset is -3 ]] 
    movq -24(%rbp), %rbx
    
    callq *%rbx # FUNCTION_POINTER
    
    # [[ Loading variable fun1 ]] 
    # [[ offset is -2 ]] 
    movq -16(%rbp), %rbx
    
    callq *%rbx # FUNCTION_POINTER
    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
