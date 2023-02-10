.section .data
size_of_char: .quad 8
.section .text

.type openFileRO, @function
.globl openFileRO
openFileRO:
# INPUT: RDI filename
# OUTPUT: RAX File-descriptor
        # Open the file in rdi
        movq $2, %rax           # Open file
        movq $0, %rsi           # Read-only mode
        syscall                 # File descriptor is in RAX
        ret

.type closeFile, @function
.globl closeFile
closeFile:
# INPUT: RDI File-descriptor
        movq $3, %rax           # Close the file.
        syscall
        ret

.type read4Byte, @function
.globl read4Byte
read4Byte:
# INPUT: RDI filestream
#        RSI buffer
# OUTPUT: RAX amount of bytes read
        movq $0, %rax
        movq $20, %rdx
        syscall
        ret
.type printStringWithReplace, @function
.globl printStringWithReplace
printStringWithReplace:
# INPUT: RDI: The string buffer
# INPUT: RSI: Array containing indexes for stop indicators. '%' from the input string marks a stop indicator
# INPUT: RDX: Array containing what to substitute '%' with.
# INPUT: RCX: Number of substitutions to perform

        # r10: Stores the index of the current substitution element
        # r11: Stores the value of the start of the substring
        # r12: Stores the value of the current stop_indicator | RSI[r10]
        movq $0, %r10
        movq $0, %r11
        movq (%rsi, %r10, 8), %r12
        jmp printStringWithReplace_start
printStringWithReplace_next:
        
        
        movq (%rdx, %r10, 8), %rdi
        call printNum
        
        dec %rcx
        inc %r10                # Increment the index, indicating how many substitution elements we have been through
        
        movq %r12, %r11         # Fetch the start for the next substring
        inc %r11                # Account for the removal of '%'

        movq (%rsi, %r10, 8), %r12 # Fetch the next ending location for this substring

printStringWithReplace_start:
        # Step 1: Print the first substring upto the first stop_pointer
        
        # Offset into the inputbuffer the correct amount!!
        leaq (%rdi, %r11, 1), %rax # Start of substring
        

        # Find the length of the substring
        
        movq (%rsi, %r10, 8), %r8   # The position in the main_string this substring ends
        
        subq %r11, %r8              # The length of the substring
        
        push %rdx
        call printString
        pop %rdx

        # Step 2: If there are any substitutions left: Print the substitution goto 1.
        

        cmpq $0, %rcx
        jnz printStringWithReplace_next

        ret

.type printString, @function
.globl printString
printString:
# INPUT: RAX: the String
# INPUT: R8: the length, 0 if unknown
# MODIFIES: rax

        push %rdi
        push %rsi
        push %rdx
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
        pop %rdx
        pop %rsi
        pop %rdi
        ret
printWithLength:
        movq %rax, %rsi
        movq $1, %rax
        movq $1, %rdi
        movq %r8, %rdx
        syscall

        pop %rcx
        pop %rdx
        pop %rsi
        pop %rdi
        ret

.type printNum, @function
.globl printNum
printNum:
# INPUT: rdi: The Number
# MODIFIES: rax
# MODIFIES: rbx

        push %rbp
        movq %rsp, %rbp         # Setup stackpointer

        push %rcx
        push %rdx
        push %rdi
        push %rsi

        movq $10, %rbx          # Divide by 10 to get the digits one by one.
        movq $0, %rcx           # Initial length of 0
        movq %rdi, %rax
printNumLoop:

        cqto
        idivq %rbx
        addq $48, %rdx          # The char '0'
        push %dx                # The left-most digit 
        addq $2, %rcx           # Size of dx 2 bytes

        cmp $0, %rax
        jnz printNumLoop

        leaq (%rsp), %rsi       # The 'buffer' is the stack
        movq $1, %rax           # Write.
        movq $1, %rdi           # Standart out
        movq %rcx, %rdx         # How long is the string
        syscall                 # Make the call

        pop %rsi
        pop %rdi
        pop %rdx
        pop %rcx

        movq %rbp, %rsp         # Restore stackpointer
        pop %rbp

        ret
