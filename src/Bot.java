import java.util.concurrent.*;

public abstract class Bot {
    protected State state;
    private static final int TIMELIMIT_MILLISECONDS = 4000;

    public int[] move(State state) {
        this.state = state;
        try (ExecutorService service = Executors.newSingleThreadExecutor()) {
            Callable<int[]> searchAlgorithmThread = new Callable<>() {
                @Override
                public int[] call() throws Exception {
                    return search();
                }
            };
            long startTime = System.nanoTime();
            Future<int[]> f = service.submit(searchAlgorithmThread);
            try {
                int[] ret = f.get(TIMELIMIT_MILLISECONDS, TimeUnit.MILLISECONDS);
                long endTime = System.nanoTime();
                long totalTime = endTime - startTime;
                System.out.println("Move success in " + totalTime/1000000 + " ms");
                return ret;
            }
            catch (final InterruptedException | TimeoutException | ExecutionException e) {
                e.printStackTrace();
                f.cancel(true);
                return greedyMove();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new Error();
        }
    }

    /**
     * fallback plan if search() exceeds time limit
     * Must be fast (<50 ms) and could prevent basic error
     * @return int[2] - next move
     */
    protected int[] greedyMove() {
        // TODO: better random move
        System.out.println("This is fallback plan :(");
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    /**
     * main searching algorithm
     * @return int[2] - next move
     */
    protected abstract int[] search() throws Exception;
}
