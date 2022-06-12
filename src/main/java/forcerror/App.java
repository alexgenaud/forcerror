package forcerror;

public class App {
    static final int WHILE_LOOPS = 10; // try 0 to Long.MAX_VALUE
    static final int RECURSE_LOOPS = 10; // try 0 to 1000 to 100000
    static final int DATA_SIZE = 10; // try 0, 10, 1000, or more

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
            if (0 == start % (1000 * 1000)) {
                System.out.println("i:" + start + " while iterations");
            }
        }
    }

    public static void main(String[] args) {
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
