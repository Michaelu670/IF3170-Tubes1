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
        return oCount - xCount;
    }

    /**
     *
     * @return any state value
     */
    public double objectiveFunctionHeuristic() {
        // TODO
        return 0.0;
    }

    public void moveThis(int x, int y) {
        turnsLeft -= 1;
        playerXTurn = !playerXTurn;
        String marker = playerXTurn ? "X" : "O";
        putMarker(x, y, marker);
        if (x > 0 && !values[x-1][y].isEmpty()) {
            putMarker(x-1, y, marker);
        }
        if (y > 0 && !values[x][y-1].isEmpty()) {
            putMarker(x, y-1, marker);
        }
        if (x < BOARD_SIZE - 1 && !values[x+1][y].isEmpty()) {
            putMarker(x+1, y, marker);
        }
        if (y < BOARD_SIZE - 1 && !values[x][y+1].isEmpty()) {
            putMarker(x, y+1, marker);
        }
    }

    /**
     *
     * @param x row, 0-indexing
     * @param y col, 0-indexing
     * @return new State
     */
    public State move(int x, int y) {
        State res = new State(valuesCopy(), turnsLeft-1, !playerXTurn);
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

    public String getValue(int x, int y) {
        return values[x][y];
    }

    public String[][] getValues() {
        return values;
    }

    public int getTurnsLeft() {
        return turnsLeft;
    }

    public String[][] valuesCopy() {
        String[][] copy = new String[values.length][values.length];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                copy[i][j] = values[i][j];
            }
        }
        return copy;
    }
    @Override
    protected State clone() throws CloneNotSupportedException {
        return new State(valuesCopy(), turnsLeft, playerXTurn);
    }
}
