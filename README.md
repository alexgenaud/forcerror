# Force Error (StackOverFlow, OutOfMemory)

A playground to force unrecoverable Java runtime Errors.
The code should compile, run, and exit without issues.
However, by tweaking a few parameters, it is possible
to force some Errors to be thrown.


## Gradle build and run

Assumes Java 17.0.3 and gradle 7.4.2 (gradlew (gradle wrapper) not included):

     $ gradle clean jar run --args "1 2 4"

## Run from the command line

After compiling forcerror with gradle, a jar should be found in build/libs/forcerror.jar.
We can set the heap size (with the Xmx flag) and pass three arguments
(while loop count, recursive iteration count, and data size per loop in bytes):

     $ java -Xmx5000k -jar build/libs/forcerror.jar 10 20 40

## Try to break it!

It should be possible to force one of several Errors by
increasing the while loop count,
increasing the recursion iteration count,
increasing the data size (per loop),
or decreasing the JVM heap size.


### OutOfMemoryError

The JVM requires a few megabytes to load itself.
So, with only a megabyte of heap space,
the JVM may run out of memory before the Main loop even starts. 
Most JVMs will crash early if the max heap size is less than a few megabytes
(2049 kilobytes for me, to be exact).

We can modify the heap size in build.gradle (line 31)
by setting Xmx jvmArg below and above 2000k (give or take a few hundred)
and recompiling with 'gradle jar'.

    jvmArgs += "-Xmx1500k" // Probably too low

However, rather than editing the source code and build configuration,
the heap size can be set (-Xmx)
and the jar can be executed directly from the command line:

     $ java -Xmx2500k -jar build/libs/forcerror.jar 0 0 4

After discovering the bare minimum heap size,
we can test memory allocation.
The following command will initialize a 2500 kilobyte heap
and one while loop (no recursion) initializing
four megabytes of unused data.

     $ java -Xmx2500k -jar build/libs/forcerror.jar 1 0 4004004

If we constrained the heap size to the bare minimum, then
four megabytes will surely crash a JVM with a smaller heap.


### StackOverFlowError

A while loop, no matter how many iterations, is unlikely to overflow the stack.
However, we can cause a stack overflow error to be thrown after
recursing thousands of calls deep.

     $ java -Xmx2500k -jar build/libs/forcerror.jar 0 12345 4

My JVM will throw an error before 7500 recursive method calls.


### Compilation optimizations

If my JVM can handle one while loop,
then it will most likley handle billions of while loops.
Perhaps the garbage collector cleans up after every iteration
or the (JIT) compiler realizes that the data array is never used
and thus the data array is optimized away
(reused or no array allocated at all in bytecode).

     $ java -Xmx2500k -jar build/libs/forcerror.jar 123456789 0 12340

This is not the case with recursive calls.
The JVM generates a unique and useless data array
for each instance of the recursive method.
The garbage collector cannot free up any of the data
referenced by any of the recursive method
instances (frames) on the stack.

     $ java -Xmx2500k -jar build/libs/forcerror.jar 0 1234 12340

## Links, for more information

https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/StackOverflowError.html

https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/OutOfMemoryError.html

