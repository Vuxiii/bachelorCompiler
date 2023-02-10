.section .data
.section .text
.section .data
hellotext: .ascii "hejsa%\n"
substitute: .space 8
stopindicators: .space 16 # Fill me with 7 8. This grows positive for some reason lmao.


.section .text
.global _start
_start:
    push %rbp
    movq %rsp, %rbp # Setup stackpointer
    
    # subq $8, %rsp

    movq $hellotext, %rdi # The input text
    
    leaq stopindicators, %rsi # Loading the buffer address

    movq $5, (%rsi) # Making the indicator stops
    movq $8, 8(%rsi) # Making the indicator stops
    
    leaq substitute, %rdx # Loading the substitute buffer address
    movq $420, (%rdx)

    call printStringWithReplace


    movq %rbp, %rsp # Restore stackpointer
    pop %rbp
    
# Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
