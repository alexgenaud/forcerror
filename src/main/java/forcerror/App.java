package forcerror;

/**
 * App [ NUM_WHILE_LOOPS | NUM_RECURSE_LOOPS | DATA_SIZE ]
 *
 * App takes three optional numeric arguments (all default to 10)
 * and may report an ERROR and exit without incident
 *
 * DATA_BYTES is in 32 bit integers, so that 100 is 400 bytes
 **/
public class App {
    void recurse(long start, long end, int dataSize) {
        if (++start > end) {
            return;
        }
        int[] data = new int[dataSize];
        if (0 == start % 100) {
            System.out.println("i:" + start + " recursions");
        }
        recurse(start, end, dataSize);
    }

    void whileloop(long start, long end, int dataSize) {
        while (++start <= end) {
            int[] data = new int[dataSize];
            if (0 == start % (100000)) {
                System.out.println("i:" + start + " while iterations");
            }
        }
    }

    public static void main(String[] args) {
        long WHILE_LOOPS = 4; // try 0 to Long.MAX_VALUE
        long RECURSE_LOOPS = 4; // try 0 to 1000 to 100000
        int DATA_SIZE = 4; // try 0, 10, 1000, or more

        if (null == args) {
            throw new IllegalAccessError();
        }

        int processingArg = 0;
        for (String arg : args) {
            try {
                long larg = Long.parseLong(arg);
                if (larg < 0) {
                    continue;
                }
                switch (processingArg) {
                    case 0:
                        WHILE_LOOPS = larg;
                        break;
                    case 1:
                        RECURSE_LOOPS = larg;
                        break;
                    case 2:
                        DATA_SIZE = (int) ((larg + 3) / 4); // round up
                        break;
                }
                if (++processingArg >= 3) {
                    break;
                }
            } catch (Exception ignore) {
            }
        }
        System.out.println("Force Errors with " + WHILE_LOOPS + " while loops, " +
                RECURSE_LOOPS + " recursive iterations, and data size of " +
                (DATA_SIZE * 4) + " bytes.");
        App app = new App();
        try {
            app.whileloop(0, WHILE_LOOPS, DATA_SIZE);
            app.recurse(0, RECURSE_LOOPS, DATA_SIZE);
        } catch (Error e) {
            System.out.println("catch(Error: " + e + ")");
        } catch (Exception e) {
            System.out.println("catch(Exception: " + e + ")");
        }
    }
}
