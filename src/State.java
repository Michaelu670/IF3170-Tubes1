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
        setValues(values);
        setTurnsLeft(turnsLeft);
        setPlayerXTurn(playerXTurn);
    }

    /* getter and setter */
    public String[][] getValues() {return this.values;}
    public int getTurnsLeft() {return this.turnsLeft;}
    public boolean getPlayerXTurn() {return this.playerXTurn;}
    public void setValues(String[][] values) {
        this.values = new String[BOARD_SIZE][BOARD_SIZE];
        // this.values = values.clone();
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(values[i], 0, this.values[i], 0, values.length);
        }
    }
    public void setTurnsLeft(int turnsLeft) {this.turnsLeft = turnsLeft;}
    public void setPlayerXTurn(boolean playerXTurn) {this.playerXTurn = playerXTurn;}

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
        int xCount = (int) Arrays.stream(values).flatMap(Arrays::stream).filter((x) -> x.equals("X")).count();
        int oCount = (int) Arrays.stream(values).flatMap(Arrays::stream).filter((x) -> x.equals("O")).count();
        return oCount - xCount;
    }

    /**
     * simulate real game move on current state
     * @param x row, 0-indexing
     * @param y col, 0-indexing
     * @throws IllegalArgumentException when out of bound or place on non-empty cell
     * @return new State
     */
    public State move(int x, int y) throws IllegalArgumentException {
        State res = new State(values, turnsLeft-1, !playerXTurn);
        String marker = playerXTurn ? "X" : "O";
        String otherMarker = playerXTurn ? "O" : "X";
        if (x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE) throw new IllegalArgumentException();
        if(values[x][y].equals("X") || values[x][y].equals("O")) throw new IllegalArgumentException();
        res.putMarker(x, y, marker);
        if (x > 0 && values[x-1][y].equals(otherMarker)) {
            res.putMarker(x-1, y, marker);
        }
        if (y > 0 && values[x][y-1].equals(otherMarker)) {
            res.putMarker(x, y-1, marker);
        }
        if (x < BOARD_SIZE - 1 && values[x+1][y].equals(otherMarker)) {
            res.putMarker(x+1, y, marker);
        }
        if (y < BOARD_SIZE - 1 && values[x][y+1].equals(otherMarker)) {
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
        clone.setValues(values);
        clone.setTurnsLeft(turnsLeft);
        clone.setPlayerXTurn(playerXTurn);


        return clone;
    }
}
