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

            Future<int[]> f = service.submit(searchAlgorithmThread);
            try {
                return f.get(TIMELIMIT_MILLISECONDS, TimeUnit.MILLISECONDS);
            }
            catch (final InterruptedException | TimeoutException | ExecutionException e) {
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
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    /**
     * main searching algorithm
     * @return int[2] - next move
     */
    protected abstract int[] search() throws Exception;
}
