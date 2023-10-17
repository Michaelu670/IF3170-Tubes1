public class RandomBot extends Bot {
    protected RandomBot() {
        // any marker will do.
        super(State.X_MARKER);
    }

    @Override
    protected int[] search() throws Exception {
        for (int i = 0; i < 10; i++) {
            i--;
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        }
        // create random move
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }
}
