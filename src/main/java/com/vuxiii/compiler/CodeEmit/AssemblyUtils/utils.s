.section .data

.section .text

.global printString
.global openFileRO
.global closeFile
.global read4Byte

openFileRO:
# INPUT: RDI filename
# OUTPUT: RAX File-descriptor
        # Open the file in rdi
        movq $2, %rax           # Open file
        movq $0, %rsi           # Read-only mode
        syscall                 # File descriptor is in RAX
        ret

closeFile:
# INPUT: RDI File-descriptor
        movq $3, %rax           # Close the file.
        syscall
        ret

read4Byte:
# INPUT: RDI filestream
#        RSI buffer
# OUTPUT: RAX amount of bytes read
        movq $0, %rax
        movq $20, %rdx
        syscall
        ret

printString:
# INPUT: RAX: the String
# INPUT: R8: the length, 0 if unknown

        push %rcx

        cmpq $0, %r8            # If the length of the string is known
        jne printWithLength


        movq $0, %rcx           # This is the current index in the string & will be the length of the string

loopBegin:
        # This moves through the (byte) dl = (quad)rax[(quad)rcx]
        movb ( %rax, %rcx ), %dl
        cmpb $0, %dl
        je loopEnd

        addq $1, %rcx

        jmp loopBegin

loopEnd:

        # rcx is the length of the string

        movq %rax, %rsi
        movq $1, %rax           # Write.
        movq $1, %rdi           # Standart out
        movq %rcx, %rdx         # How long is the string
        syscall                 # Make the call
        pop %rcx
        ret
printWithLength:
        movq %rax, %rsi
        movq $1, %rax
        movq $1, %rdi
        movq %r8, %rdx
        syscall
        pop %rcx
        ret





# Print RDI as an unsigned integer following by a newline.
# To print 42:
# 	mov $42, %rdi
# 	call printNum
# Note: the function does not follow the ordinary calling convention,
#       but restores all registers.
.type printNum, @function
.globl printNum
printNum:
        push %rbp
        movq %rsp, %rbp

        # save
        push %rax
        push %rdi
        push %rsi
        push %rdx
        push %rcx
        push %r8
        push %r9

        movq %rdi, %rax # arg

        movq $1, %r9 # we always print "\n"
        push $10 # '\n'
.LprintNum_convertLoop:
        movq $0, %rdx
        movq $10, %rcx
        idivq %rcx
        addq $48, %rdx # '0' is 48
        push %rdx
        addq $1, %r9
        cmpq $0, %rax   
        jne .LprintNum_convertLoop
.LprintNum_printLoop:
        movq $1, %rax # sys_write
        movq $1, %rdi # stdout
        movq %rsp, %rsi # buf
        movq $1, %rdx # len
        syscall
        addq $8, %rsp
        addq $-1, %r9
        jne .LprintNum_printLoop

        # restore
        pop %r9
        pop %r8
        pop %rcx
        pop %rdx
        pop %rsi
        pop %rdi
        pop %rax

        movq %rbp, %rsp
        pop %rbp
        ret

