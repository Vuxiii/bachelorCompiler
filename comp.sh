as src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s -o src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.o
as src/main/java/com/vuxiii/compiler/CodeEmit/asm.s -o src/main/java/com/vuxiii/compiler/CodeEmit/asm.o
ld src/main/java/com/vuxiii/compiler/CodeEmit/asm.o -o src/main/java/com/vuxiii/compiler/CodeEmit/asm src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.o

src/main/java/com/vuxiii/compiler/CodeEmit/asm