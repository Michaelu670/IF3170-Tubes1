public class RandomBot extends Bot {
    @Override
    protected int[] search() throws Exception {
        for (int i = 0; i < 10; i++) {
            i--;
            Thread.sleep(0);
        }
        // create random move
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }
}
