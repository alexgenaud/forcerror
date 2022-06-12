# Force Error (StackOverFlow, OutOfMemory)

A simple playground to force unrecoverable Java runtime Errors.
The code should compile, run, and exit without issues.
However, by tweaking a few parameters, it is possible
to force some Errors to be thrown.


## Build and run

Assumes Java 17.0.3 and gradle 7.4.2 (gradlew (gradle wrapper) not included):

     $ gradle clean assemble run


## Try to break it!

It should be possible to force one of several Errors
by increasing the recursion iteration count,
increasing the while loop count,
increasing the data size, or decreasing the jvm heap size,


### OutOfMemoryError

The JVM requires a few megabytes to load itself.
So, with only a megabyte of heap space,
the JVM may run out of memory before the Main loop even starts. 
Most JVMs will crash early if the max heap size is less than a few megabytes
(2050 kilobytes for me, to be exact).
We can test this by setting Xmx jvmArg incredibly low in build.gradle:

    jvmArgs += "-Xmx1k" // Way too small max heap

Or even

    jvmArgs += "-Xmx1000k" // Probably still too low

But

    jvmArgs += "-Xmx5000k" // Should start on most (v) machines

After discovering the bare minimum maximum heap size, plus a bit more,
we can test memory allocation. In forcerror.App.java
we can increase the DATA_SIZE (the size of 32 bit integer array) and run again.

    static final int WHILE_LOOPS = 1;
    static final int RECURSE_LOOPS = 1;
    static final int DATA_SIZE = 1000000;

If we constrained the heap size to the bare minimum, then
a million integers (4 MB) in a single loop will surely crash the JVM.


### StackOverFlowError

A while loop, no matter how many iterations, is unlikely to overflow the stack.
However, we can cause a stack overflow error to be thrown after
recursing thousands of calls deep.

    static final int WHILE_LOOPS = 0;
    static final int RECURSE_LOOPS = 12345;
    static final int DATA_SIZE = 1;

My JVM will throw an error before 8000 recursive method calls.


### Compilation optimizations

If the array size is moderately low and if my
JVM can handle one while loop, then it can handle billions of while loops.
Either the garbage collector cleans up after every iteration
or the (JIT) compiler realizes that the data array is never used
and thus perhaps the data array is not generated at all (optimized out of the bytecode).

    static final int WHILE_LOOPS = 0;
    static final int RECURSE_LOOPS = 1000;
    static final int DATA_SIZE = 1000;

However, this is not the case with recursive calls.
The JVM generates a unique and useless data array
for each instance of the recursive method.
The garbage collector cannot free up any of the data
referenced by any of the recursive method
instances (frames) on the stack.


## Links, for more information

https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/StackOverflowError.html

https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/OutOfMemoryError.html

