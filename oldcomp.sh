as src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.s -o src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.o
gcc -c src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.c
as src/main/java/com/vuxiii/compiler/CodeEmit/Output/asm.s -o src/main/java/com/vuxiii/compiler/CodeEmit/Output/asm.o
ld -o src/main/java/com/vuxiii/compiler/CodeEmit/Output/asm src/main/java/com/vuxiii/compiler/CodeEmit/Output/asm.o  src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/utils.o src/main/java/com/vuxiii/compiler/CodeEmit/AssemblyUtils/gc.o

src/main/java/com/vuxiii/compiler/CodeEmit/Output/asm