	.file	"c.c"
	.text
.Ltext0:
	.file 0 "/home/william/Desktop/sdu/6sem/bachelor/test" "c.c"
	.data
	.align 8
	.type	heap_size, @object
	.size	heap_size, 8
heap_size:
	.quad	4096
	.local	heap
	.comm	heap,8,8
	.local	to_heap
	.comm	to_heap,8,8
	.local	heap_pointer
	.comm	heap_pointer,8,8
	.text
	.globl	new
	.type	new, @function
new:
.LFB6:
	.file 1 "c.c"
	.loc 1 16 31
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movq	%rdi, -40(%rbp)
	.loc 1 24 9
	movq	heap_pointer(%rip), %rax
	movl	%eax, -20(%rbp)
	.loc 1 25 18
	movq	heap_pointer(%rip), %rdx
	movq	-40(%rbp), %rax
	addq	%rdx, %rax
	movq	%rax, heap_pointer(%rip)
	.loc 1 27 9
	movl	-20(%rbp), %eax
	cltq
	movq	%rax, -16(%rbp)
	movq	-40(%rbp), %rax
	movq	%rax, -8(%rbp)
	.loc 1 28 12
	movq	-16(%rbp), %rax
	movq	-8(%rbp), %rdx
	.loc 1 29 1
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE6:
	.size	new, .-new
	.section	.rodata
.LC0:
	.string	"%d\n"
	.text
	.globl	another_function
	.type	another_function, @function
another_function:
.LFB7:
	.loc 1 33 31
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movl	%edi, -20(%rbp)
	.loc 1 34 9
	movl	$3, -4(%rbp)
	.loc 1 35 7
	addl	$5, -4(%rbp)
	.loc 1 36 5
	movl	-20(%rbp), %edx
	movl	-4(%rbp), %eax
	addl	%edx, %eax
	movl	%eax, %esi
	leaq	.LC0(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 37 14
	movl	-20(%rbp), %edx
	movl	-4(%rbp), %eax
	addl	%edx, %eax
	.loc 1 38 1
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE7:
	.size	another_function, .-another_function
	.globl	print_num
	.type	print_num, @function
print_num:
.LFB8:
	.loc 1 40 27
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	movl	%edi, -4(%rbp)
	.loc 1 41 5
	movl	-4(%rbp), %eax
	movl	%eax, %esi
	leaq	.LC0(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 42 1
	nop
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE8:
	.size	print_num, .-print_num
	.globl	get_buffer
	.type	get_buffer, @function
get_buffer:
.LFB9:
	.loc 1 44 40
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	movq	%rdi, -8(%rbp)
	.loc 1 45 12
	movq	-8(%rbp), %rax
	movq	%rax, %rdi
	call	malloc@PLT
	.loc 1 46 1
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE9:
	.size	get_buffer, .-get_buffer
	.globl	initialize_heap
	.type	initialize_heap, @function
initialize_heap:
.LFB10:
	.loc 1 48 24
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	.loc 1 49 12
	movq	heap_size(%rip), %rax
	movq	%rax, %rdi
	call	malloc@PLT
	.loc 1 49 10
	movq	%rax, heap(%rip)
	.loc 1 50 15
	movq	heap_size(%rip), %rax
	movq	%rax, %rdi
	call	malloc@PLT
	.loc 1 50 13
	movq	%rax, to_heap(%rip)
	.loc 1 52 15
	movq	heap(%rip), %rax
	.loc 1 52 8
	testq	%rax, %rax
	je	.L9
	.loc 1 52 34 discriminator 1
	movq	to_heap(%rip), %rax
	.loc 1 52 23 discriminator 1
	testq	%rax, %rax
	jne	.L11
.L9:
	.loc 1 53 9
	movl	$-1, %edi
	call	exit@PLT
.L11:
	.loc 1 54 1
	nop
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE10:
	.size	initialize_heap, .-initialize_heap
	.section	.rodata
.LC1:
	.string	"PROT_READ:    %d\n"
.LC2:
	.string	"PROT_WRITE:   %d\n"
.LC3:
	.string	"PROT_EXEC:    %d\n"
.LC4:
	.string	"MAP_PRIVATE:  %d\n"
	.text
	.globl	print_flags
	.type	print_flags, @function
print_flags:
.LFB11:
	.loc 1 56 20
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	.loc 1 57 5
	movl	$1, %esi
	leaq	.LC1(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 58 5
	movl	$2, %esi
	leaq	.LC2(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 59 5
	movl	$4, %esi
	leaq	.LC3(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 60 5
	movl	$2, %esi
	leaq	.LC4(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 61 1
	nop
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE11:
	.size	print_flags, .-print_flags
.Letext0:
	.file 2 "/usr/include/stdlib.h"
	.file 3 "/usr/include/stdio.h"
	.file 4 "/usr/lib/gcc/x86_64-pc-linux-gnu/12.2.1/include/stddef.h"
	.section	.debug_info,"",@progbits
.Ldebug_info0:
	.long	0x263
	.value	0x5
	.byte	0x1
	.byte	0x8
	.long	.Ldebug_abbrev0
	.uleb128 0xb
	.long	.LASF27
	.byte	0x1d
	.long	.LASF0
	.long	.LASF1
	.quad	.Ltext0
	.quad	.Letext0-.Ltext0
	.long	.Ldebug_line0
	.uleb128 0x1
	.byte	0x1
	.byte	0x8
	.long	.LASF2
	.uleb128 0x1
	.byte	0x2
	.byte	0x7
	.long	.LASF3
	.uleb128 0x1
	.byte	0x4
	.byte	0x7
	.long	.LASF4
	.uleb128 0x1
	.byte	0x8
	.byte	0x7
	.long	.LASF5
	.uleb128 0x1
	.byte	0x1
	.byte	0x6
	.long	.LASF6
	.uleb128 0x1
	.byte	0x2
	.byte	0x5
	.long	.LASF7
	.uleb128 0xc
	.byte	0x4
	.byte	0x5
	.string	"int"
	.uleb128 0x1
	.byte	0x8
	.byte	0x5
	.long	.LASF8
	.uleb128 0xd
	.byte	0x8
	.uleb128 0x1
	.byte	0x1
	.byte	0x6
	.long	.LASF9
	.uleb128 0xe
	.long	0x68
	.uleb128 0xf
	.long	.LASF18
	.byte	0x4
	.byte	0xd6
	.byte	0x17
	.long	0x43
	.uleb128 0x1
	.byte	0x8
	.byte	0x5
	.long	.LASF10
	.uleb128 0x1
	.byte	0x8
	.byte	0x7
	.long	.LASF11
	.uleb128 0x2
	.long	.LASF12
	.byte	0x5
	.byte	0x16
	.long	0x43
	.uleb128 0x9
	.byte	0x3
	.quad	heap_size
	.uleb128 0x2
	.long	.LASF13
	.byte	0x6
	.byte	0xe
	.long	0x66
	.uleb128 0x9
	.byte	0x3
	.quad	heap
	.uleb128 0x2
	.long	.LASF14
	.byte	0x7
	.byte	0xe
	.long	0x66
	.uleb128 0x9
	.byte	0x3
	.quad	to_heap
	.uleb128 0x2
	.long	.LASF15
	.byte	0x9
	.byte	0x16
	.long	0x43
	.uleb128 0x9
	.byte	0x3
	.quad	heap_pointer
	.uleb128 0x10
	.string	"ptr"
	.byte	0x10
	.byte	0x1
	.byte	0xb
	.byte	0x10
	.long	0x106
	.uleb128 0x4
	.long	.LASF16
	.byte	0xc
	.long	0x43
	.byte	0
	.uleb128 0x4
	.long	.LASF17
	.byte	0xd
	.long	0x43
	.byte	0x8
	.byte	0
	.uleb128 0x11
	.string	"ptr"
	.byte	0x1
	.byte	0xe
	.byte	0x3
	.long	0xe2
	.uleb128 0x12
	.long	.LASF19
	.byte	0x2
	.value	0x27d
	.byte	0xd
	.long	0x125
	.uleb128 0x3
	.long	0x58
	.byte	0
	.uleb128 0x5
	.long	.LASF20
	.byte	0x2
	.value	0x229
	.byte	0xe
	.long	0x66
	.long	0x13c
	.uleb128 0x3
	.long	0x74
	.byte	0
	.uleb128 0x5
	.long	.LASF21
	.byte	0x3
	.value	0x164
	.byte	0xc
	.long	0x58
	.long	0x154
	.uleb128 0x3
	.long	0x154
	.uleb128 0x13
	.byte	0
	.uleb128 0x14
	.byte	0x8
	.long	0x6f
	.uleb128 0x6
	.long	.LASF22
	.byte	0x38
	.quad	.LFB11
	.quad	.LFE11-.LFB11
	.uleb128 0x1
	.byte	0x9c
	.uleb128 0x6
	.long	.LASF23
	.byte	0x30
	.quad	.LFB10
	.quad	.LFE10-.LFB10
	.uleb128 0x1
	.byte	0x9c
	.uleb128 0x7
	.long	.LASF24
	.byte	0x2c
	.byte	0x7
	.long	0x66
	.quad	.LFB9
	.quad	.LFE9-.LFB9
	.uleb128 0x1
	.byte	0x9c
	.long	0x1ba
	.uleb128 0x8
	.long	.LASF17
	.byte	0x2c
	.byte	0x21
	.long	0x43
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x15
	.long	.LASF26
	.byte	0x1
	.byte	0x28
	.byte	0x6
	.quad	.LFB8
	.quad	.LFE8-.LFB8
	.uleb128 0x1
	.byte	0x9c
	.long	0x1e7
	.uleb128 0x9
	.string	"out"
	.byte	0x28
	.byte	0x15
	.long	0x58
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0x7
	.long	.LASF25
	.byte	0x21
	.byte	0x5
	.long	0x58
	.quad	.LFB7
	.quad	.LFE7-.LFB7
	.uleb128 0x1
	.byte	0x9c
	.long	0x220
	.uleb128 0x9
	.string	"b"
	.byte	0x21
	.byte	0x1b
	.long	0x58
	.uleb128 0x2
	.byte	0x91
	.sleb128 -36
	.uleb128 0xa
	.string	"a"
	.byte	0x22
	.long	0x58
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0x16
	.string	"new"
	.byte	0x1
	.byte	0x10
	.byte	0x5
	.long	0x106
	.quad	.LFB6
	.quad	.LFE6-.LFB6
	.uleb128 0x1
	.byte	0x9c
	.uleb128 0x8
	.long	.LASF17
	.byte	0x10
	.byte	0x18
	.long	0x43
	.uleb128 0x2
	.byte	0x91
	.sleb128 -56
	.uleb128 0x2
	.long	.LASF16
	.byte	0x18
	.byte	0x9
	.long	0x58
	.uleb128 0x2
	.byte	0x91
	.sleb128 -36
	.uleb128 0xa
	.string	"p"
	.byte	0x1b
	.long	0x106
	.uleb128 0x2
	.byte	0x91
	.sleb128 -32
	.byte	0
	.byte	0
	.section	.debug_abbrev,"",@progbits
.Ldebug_abbrev0:
	.uleb128 0x1
	.uleb128 0x24
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3e
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0xe
	.byte	0
	.byte	0
	.uleb128 0x2
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x3
	.uleb128 0x5
	.byte	0
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x4
	.uleb128 0xd
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0x21
	.sleb128 19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x38
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0x5
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0x5
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x6
	.uleb128 0x2e
	.byte	0
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0x21
	.sleb128 6
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x7
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x8
	.uleb128 0x5
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x9
	.uleb128 0x5
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0xa
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0x21
	.sleb128 9
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0xb
	.uleb128 0x11
	.byte	0x1
	.uleb128 0x25
	.uleb128 0xe
	.uleb128 0x13
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0x1f
	.uleb128 0x1b
	.uleb128 0x1f
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x10
	.uleb128 0x17
	.byte	0
	.byte	0
	.uleb128 0xc
	.uleb128 0x24
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3e
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0x8
	.byte	0
	.byte	0
	.uleb128 0xd
	.uleb128 0xf
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0xe
	.uleb128 0x26
	.byte	0
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0xf
	.uleb128 0x16
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x10
	.uleb128 0x13
	.byte	0x1
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x11
	.uleb128 0x16
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x12
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0x5
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x87
	.uleb128 0x19
	.uleb128 0x3c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x13
	.uleb128 0x18
	.byte	0
	.byte	0
	.byte	0
	.uleb128 0x14
	.uleb128 0xf
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x15
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x16
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7a
	.uleb128 0x19
	.byte	0
	.byte	0
	.byte	0
	.section	.debug_aranges,"",@progbits
	.long	0x2c
	.value	0x2
	.long	.Ldebug_info0
	.byte	0x8
	.byte	0
	.value	0
	.value	0
	.quad	.Ltext0
	.quad	.Letext0-.Ltext0
	.quad	0
	.quad	0
	.section	.debug_line,"",@progbits
.Ldebug_line0:
	.section	.debug_str,"MS",@progbits,1
.LASF12:
	.string	"heap_size"
.LASF18:
	.string	"size_t"
.LASF19:
	.string	"exit"
.LASF3:
	.string	"short unsigned int"
.LASF22:
	.string	"print_flags"
.LASF17:
	.string	"size"
.LASF5:
	.string	"long unsigned int"
.LASF15:
	.string	"heap_pointer"
.LASF14:
	.string	"to_heap"
.LASF2:
	.string	"unsigned char"
.LASF24:
	.string	"get_buffer"
.LASF4:
	.string	"unsigned int"
.LASF11:
	.string	"long long unsigned int"
.LASF13:
	.string	"heap"
.LASF23:
	.string	"initialize_heap"
.LASF10:
	.string	"long long int"
.LASF9:
	.string	"char"
.LASF21:
	.string	"printf"
.LASF16:
	.string	"offset"
.LASF25:
	.string	"another_function"
.LASF7:
	.string	"short int"
.LASF26:
	.string	"print_num"
.LASF27:
	.string	"GNU C17 12.2.1 20230201 -mtune=generic -march=x86-64 -g"
.LASF8:
	.string	"long int"
.LASF6:
	.string	"signed char"
.LASF20:
	.string	"malloc"
	.section	.debug_line_str,"MS",@progbits,1
.LASF0:
	.string	"c.c"
.LASF1:
	.string	"/home/william/Desktop/sdu/6sem/bachelor/test"
	.ident	"GCC: (GNU) 12.2.1 20230201"
	.section	.note.GNU-stack,"",@progbits
