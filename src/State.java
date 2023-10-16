import java.util.Arrays;

/**
 * State class
 * Store board state
 *
 * TODO: UNTESTED
 */
public class State {
    public static final String X_MARKER = "X";
    public static final String O_MARKER = "O";
    private String[][] values;
    private int turnsLeft;
    private boolean playerXTurn;
    public static final int BOARD_SIZE = 8;

    public State(String[][] values, int turnsLeft, boolean playerXTurn) {
        setValues(values);
        setTurnsLeft(turnsLeft);
        setPlayerXTurn(playerXTurn);
    }
    
    public static State initialState(int turnsLeft, boolean playerXTurn) {
        String[][] board = new String[State.BOARD_SIZE][State.BOARD_SIZE];
        for (int i = 0; i < State.BOARD_SIZE ; i++) {
            for (int j = 0; j < State.BOARD_SIZE ; j++) {
                board[i][j] = " ";
            }
        }
        board[State.BOARD_SIZE - 2][0] = "X";
        board[State.BOARD_SIZE - 1][0] = "X";
        board[State.BOARD_SIZE - 2][1] = "X";
        board[State.BOARD_SIZE - 1][1] = "X";
        board[0][State.BOARD_SIZE - 2] = "O";
        board[0][State.BOARD_SIZE - 1] = "O";
        board[1][State.BOARD_SIZE - 2] = "O";
        board[1][State.BOARD_SIZE - 1] = "O";

        State state = new State(board, turnsLeft, playerXTurn);
        return state;
    }

    /* getter and setter */
    public String[][] getValues() {return this.values;}
    public int getTurnsLeft() {return this.turnsLeft;}
    public boolean getPlayerXTurn() {return this.playerXTurn;}
    public String getValue(int x, int y) {return this.values[x][y];}
    public void setValue(int x, int y, String p) {this.values[x][y] = p;}
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
        return oCount - xCount;
    }

    /**
     *
     * @return any state value
     */
    public double objectiveFunctionHeuristic(String player_marker) {
        int opponent_l = 0;
        int opponent_u = 0;
        int player_l = 0;
        int player_u = 0;
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (values[i][j].isBlank()) {
                    continue;
                }
                boolean empty = false;
                for (int k = 0; k < 4; k++) {
                    int nx = i + dx[k];
                    int ny = j + dy[k];
                    if (nx < 0 || ny < 0 || nx >= BOARD_SIZE || ny >= BOARD_SIZE) {
                        continue;
                    }
                    if (values[nx][ny].isBlank()) {
                        empty = true;
                        break;
                    }
                }

                if (empty) {
                    if (values[i][j].equals(player_marker)) {
                        player_u++;
                    }
                    else {
                        opponent_u++;
                    }
                }
                else {
                    if (values[i][j].equals(player_marker)) {
                        player_l++;
                    }
                    else {
                        opponent_l++;
                    }
                }
            }
        }

        int C = opponent_u > player_u ? 1 : -1;
        return opponent_l - player_l + C * Math.max(Math.abs(opponent_u-player_u)-turnsLeft, 0);
    }

    public void moveThis(int x, int y) {
        turnsLeft -= 1;
        String marker = playerXTurn ? "X" : "O";
        playerXTurn = !playerXTurn;
        putMarker(x, y, marker);
        if (x > 0 && !values[x-1][y].isBlank()) {
            putMarker(x-1, y, marker);
        }
        if (y > 0 && !values[x][y-1].isBlank()) {
            putMarker(x, y-1, marker);
        }
        if (x < BOARD_SIZE - 1 && !values[x+1][y].isBlank()) {
            putMarker(x+1, y, marker);
        }
        if (y < BOARD_SIZE - 1 && !values[x][y+1].isBlank()) {
            putMarker(x, y+1, marker);
        }
    }


    /**
     * simulate real game move on current state
     * @param x row, 0-indexing
     * @param y col, 0-indexing
     * @throws IllegalArgumentException when out of bound or place on non-empty cell
     * @return new State
     */
    public State move(int x, int y) throws IllegalArgumentException {
        State res = new State(valuesCopy(), turnsLeft-1, !playerXTurn);
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

    public int emptyCount() {
        return (int) Arrays.stream(values).flatMap(Arrays::stream).filter(String::isBlank).count();
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
    protected Object clone() throws CloneNotSupportedException {
        return new State(valuesCopy(), turnsLeft, playerXTurn);
    }

    public void printState() {
        for (int i = 0; i < State.BOARD_SIZE ; i++) {
            for (int j = 0; j < State.BOARD_SIZE ; j++) {
                System.out.print(values[i][j].equals("X") ? "X" :
                                values[i][j].equals("O") ? "O" :
                                "-");
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public int[] getScore() {
        int[] scores = new int[2];
        for (int i = 0; i < State.BOARD_SIZE ; i++) {
            for (int j = 0; j < State.BOARD_SIZE ; j++) {
                if(values[i][j].equals("X")) {
                    scores[0]++;
                } else if(values[i][j].equals("O")) {
                    scores[1]++;
                }
            }
        }
        return scores;
    }
}
