.section .data
string0: .ascii "a has the value: %\n"
string0subs: .ascii ""
string1: .ascii "a has the value: %\n"
string1subs: .ascii ""
layoutptr1: .space 8
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
    pushq $0
    leaq (%rsp), %rsi
    callq new_layout
    addq $1, %rsp
    
    movq %rax, layoutptr1

    movq $1, %rdi
    movq layoutptr1, %rsi
    callq new_ptr_size

    movq $42, 16(%rax)
    movq layoutptr1, 8(%rax)
    movq %rax, -16(%rbp)

    callq print_scopes

#     pushq $3
#     popq %rax
#     movq %rax, 16(%rbx)
    
#     # [[ Loading variable a ]] 
#     # [[ offset is -1 ]] 
#     movq -8(%rbp), %rax
    
#     movq 16(%rax), %rax
#     pushq %rax

# # Setup Print

#     movq $string0, %rdi
#     movq $17, %rsi
#     movq $0, %rdx
#     call print_string
#     movq (%rsp), %rdi
#     call print_num
#     movq $string0, %rdi
#     movq $1, %rsi
#     movq $18, %rdx
#     call print_string
#     addq $8, %rsp

# # End Print

#     pushq $42
    
#     # [[ Loading variable a ]] 
#     # [[ offset is -1 ]] 
#     movq -8(%rbp), %rcx
    
#     movq 16(%rcx), %rcx
#     popq %rbx
#     subq %rcx, %rbx
#     pushq %rax
#     popq %rax
#     movq %rax, 16(%rbx)
    
#     # [[ Loading variable a ]] 
#     # [[ offset is -1 ]] 
#     movq -8(%rbp), %rax
    
#     movq 16(%rax), %rax
#     pushq %rax

# # Setup Print

#     movq $string1, %rdi
#     movq $17, %rsi
#     movq $0, %rdx
#     call print_string
#     movq (%rsp), %rdi
#     call print_num
#     movq $string1, %rdi
#     movq $1, %rsi
#     movq $18, %rdx
#     call print_string
#     addq $8, %rsp

# End Print

    movq %rbp, %rsp # Restore stackpointer
    popq %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
