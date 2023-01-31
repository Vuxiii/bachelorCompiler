# .section .data
#      hello: .ascii "Hello World!\n"

# .section .text
# .global _start

# _start:
#     movq $1, %rax
#     movq $1, %rdi
#     push $0x32
#     push $0x31
#     movq %rsp, %rsi

#     movq $2, %rdx
#     syscall

#     movq $60, %rax
#     movq $0, %rdi

#     syscall
.section .data

buffer: .space 20       # Allocate a buffer of 20 bytes

.section .text

.global _start

_start:

    movq $0, %rdi           # Read from stdin
    movq $buffer, %rsi      # Store the data in buffer
    movq $20, %rdx          # How many bytes to read
    syscall                 # Make the read call

    movq $buffer, %rax
    call printString

    # Exit call
    movq $60, %rax
    movq $0, %rdi
    syscall
