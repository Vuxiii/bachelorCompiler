
let fib: (a: int) -> int;

fib = (a: int) -> int {
    if ( a == 1 ) {
        return 1;
    } else if ( a == 2 ) {
        return 1;
    }
    let fib1: int;
    let fib2: int;
    fib1 = fib(a-1);
    fib2 = fib(a-2);
    print("fib(%) = %\n", a-1, fib1);
    print("fib(%) = %\n", a-2, fib2);
    return fib1 + fib2; 
};
let n: int;
n = 7;

print( "The %th fib number is: %\n", n, fib(n) );
