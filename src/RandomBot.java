public class RandomBot extends Bot {
    @Override
    protected int[] search() {
        // create random move
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }
}
