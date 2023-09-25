import java.util.Arrays;

/**
 * State class
 * Store board state
 *
 * TODO: UNTESTED
 */
public class State {
    private String[][] values;
    private int turnsLeft;
    private boolean playerXTurn;
    public static final int BOARD_SIZE = 8;

    public State(String[][] values, int turnsLeft, boolean playerXTurn) {
        this.values = values;
        this.turnsLeft = turnsLeft;
        this.playerXTurn = playerXTurn;
    }

    /**
     * @return final state value
     * @throws NoSuchMethodError if this is not terminal state
     */
    public double objectiveFunction() throws NoSuchMethodError {
        if (turnsLeft > 0) throw new NoSuchMethodError();
        int xCount = (int) Arrays.stream(values).flatMap(Arrays::stream).filter((x) -> x.equals("X")).count();
        int oCount = (int) Arrays.stream(values).flatMap(Arrays::stream).filter((x) -> x.equals("O")).count();
        return xCount - oCount;
    }

    /**
     *
     * @return any state value
     */
    public double objectiveFunctionHeuristic() {
        // TODO
        return 0.0;
    }

    /**
     *
     * @param x row, 0-indexing
     * @param y col, 0-indexing
     * @return new State
     */
    public State move(int x, int y) {
        State res = new State(values, turnsLeft-1, !playerXTurn);
        String marker = playerXTurn ? "X" : "O";
        res.putMarker(x, y, marker);
        if (x > 0) {
            res.putMarker(x-1, y, marker);
        }
        if (y > 0) {
            res.putMarker(x, y-1, marker);
        }
        if (x < BOARD_SIZE - 1) {
            res.putMarker(x+1, y, marker);
        }
        if (y < BOARD_SIZE - 1) {
            res.putMarker(x, y+1, marker);
        }
        return res;
    }

    /**
     * do values[x][y] = marker
     * @param x row, 0-indexing
     * @param y col, 0-indexing
     * @param marker "O" or "X"
     * @throws IllegalArgumentException when out of bound or marker invalid
     */
    public void putMarker(int x, int y, String marker) throws IllegalArgumentException {
        if (!marker.equals("O") && !marker.equals("X")) throw new IllegalArgumentException();
        if (x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE) throw new IllegalArgumentException();

        values[x][y] = marker;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        State clone = (State) super.clone();
        clone.values = values;
        clone.turnsLeft = turnsLeft;
        clone.playerXTurn = playerXTurn;

        return clone;
    }
}
