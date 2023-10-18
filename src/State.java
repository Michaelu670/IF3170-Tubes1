import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * State class
 * Store board state
 *
 * TODO: UNTESTED
 */
public class State {
    public static final char X_MARKER = 'X';
    public static final char O_MARKER = 'O';
    public static final char BLANK_MARKER = ' ';
    private char[][] values;
    private int turnsLeft;
    private boolean playerXTurn;
    public static final int BOARD_SIZE = 8;

    public State(char[][] values, int turnsLeft, boolean playerXTurn) {
        setValues(values);
        setTurnsLeft(turnsLeft);
        setPlayerXTurn(playerXTurn);
    }
    
    public static State initialState(int turnsLeft, boolean playerXTurn) {
        char[][] board = new char[State.BOARD_SIZE][State.BOARD_SIZE];
        for (int i = 0; i < State.BOARD_SIZE ; i++) {
            for (int j = 0; j < State.BOARD_SIZE ; j++) {
                board[i][j] = BLANK_MARKER;
            }
        }
        board[State.BOARD_SIZE - 2][0] = X_MARKER;
        board[State.BOARD_SIZE - 1][0] = X_MARKER;
        board[State.BOARD_SIZE - 2][1] = X_MARKER;
        board[State.BOARD_SIZE - 1][1] = X_MARKER;
        board[0][State.BOARD_SIZE - 2] = O_MARKER;
        board[0][State.BOARD_SIZE - 1] = O_MARKER;
        board[1][State.BOARD_SIZE - 2] = O_MARKER;
        board[1][State.BOARD_SIZE - 1] = O_MARKER;

        State state = new State(board, turnsLeft, playerXTurn);
        return state;
    }

    /* getter and setter */
    public char[][] getValues() {return this.values;}
    public int getTurnsLeft() {return this.turnsLeft;}
    public boolean getPlayerXTurn() {return this.playerXTurn;}
    public char getValue(int x, int y) {return this.values[x][y];}
    public void setValue(int x, int y, char p) {this.values[x][y] = p;}
    public void setValues(char[][] values) {
        this.values = new char[BOARD_SIZE][BOARD_SIZE];
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
    public double objectiveFunction(char player_marker) throws NoSuchMethodError {
        if (turnsLeft > 0) throw new NoSuchMethodError();
        int xCount = 0;
        int oCount = 0;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                if(values[i][j] == X_MARKER) {
                    xCount++;
                }
                if(values[i][j] == O_MARKER) {
                    oCount++;
                }
            }
        }
        
        if (player_marker == X_MARKER) {
            return xCount - oCount;
        }
        else {
            return oCount - xCount;
        }
    }

    /**
     *
     * @return any state value
     */
    public double objectiveFunctionHeuristic(char player_marker) {
        int opponent_l = 0;
        int opponent_u = 0;
        int player_l = 0;
        int player_u = 0;
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (values[i][j] == BLANK_MARKER) {
                    continue;
                }
                boolean empty = false;
                for (int k = 0; k < 4; k++) {
                    int nx = i + dx[k];
                    int ny = j + dy[k];
                    if (nx < 0 || ny < 0 || nx >= BOARD_SIZE || ny >= BOARD_SIZE) {
                        continue;
                    }
                    if (values[nx][ny] == BLANK_MARKER) {
                        empty = true;
                        break;
                    }
                }

                if (empty) {
                    if (values[i][j] == player_marker) {
                        player_u++;
                    }
                    else {
                        opponent_u++;
                    }
                }
                else {
                    if (values[i][j] == player_marker) {
                        player_l++;
                    }
                    else {
                        opponent_l++;
                    }
                }
            }
        }

        int C = player_u > opponent_u ? 1 : -1;
        return player_l - opponent_l + C * Math.max(Math.abs(player_u-opponent_u)-turnsLeft, 0);
    }

    public void moveThis(int x, int y) {
        turnsLeft -= 1;
        char marker = playerXTurn ? X_MARKER : O_MARKER;
        playerXTurn = !playerXTurn;
        putMarker(x, y, marker);
        if (x > 0 && !(values[x-1][y] == BLANK_MARKER)) {
            putMarker(x-1, y, marker);
        }
        if (y > 0 && !(values[x][y-1] == BLANK_MARKER)) {
            putMarker(x, y-1, marker);
        }
        if (x < BOARD_SIZE - 1 && !(values[x+1][y] == BLANK_MARKER)) {
            putMarker(x+1, y, marker);
        }
        if (y < BOARD_SIZE - 1 && !(values[x][y+1] == BLANK_MARKER)) {
            putMarker(x, y+1, marker);
        }
    }

    public void moveParams() {
        turnsLeft -= 1;
        playerXTurn = !playerXTurn;
    }
    public void undoParams() {
        turnsLeft += 1;
        playerXTurn = !playerXTurn;
    }


    /**
     * simulate real game move on current state
     * @param x row, 0-indexing
     * @param y col, 0-indexing
     * @throws IllegalArgumentException when out of bound or place on non-empty cell
     * @return new State
     */
    public State move(int x, int y) throws IllegalArgumentException {
        if (x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE) throw new IllegalArgumentException();
        if(values[x][y] == X_MARKER || values[x][y] == O_MARKER) throw new IllegalArgumentException();
        State res = new State(valuesCopy(), turnsLeft-1, !playerXTurn);
        char marker = playerXTurn ? X_MARKER : O_MARKER;
        char otherMarker = playerXTurn ? O_MARKER : X_MARKER;
        res.putMarker(x, y, marker);
        if (x > 0 && (values[x-1][y] == otherMarker)) {
            res.putMarker(x-1, y, marker);
        }
        if (y > 0 && (values[x][y-1] == otherMarker)) {
            res.putMarker(x, y-1, marker);
        }
        if (x < BOARD_SIZE - 1 && (values[x+1][y] == otherMarker)) {
            res.putMarker(x+1, y, marker);
        }
        if (y < BOARD_SIZE - 1 && (values[x][y+1] == otherMarker)) {
            res.putMarker(x, y+1, marker);
        }
        return res;
    }

    /**
     * do values[x][y] = marker
     * @param x row, 0-indexing
     * @param y col, 0-indexing
     * @param marker O_MARKER or X_MARKER
     * @throws IllegalArgumentException when out of bound or marker invalid
     */
    public void putMarker(int x, int y, char marker) throws IllegalArgumentException {
        if (!(marker == O_MARKER) && !(marker == X_MARKER)) throw new IllegalArgumentException();
        if (x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE) throw new IllegalArgumentException();

        values[x][y] = marker;
    }

    public int emptyCount() {
        int empty_count = 0;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                if(values[i][j] == BLANK_MARKER) {
                    empty_count++;
                }
            }
        }
        return empty_count;
    }

    public char[][] valuesCopy() {
        char[][] copy = new char[values.length][values.length];
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
                System.out.print(values[i][j] == X_MARKER ? X_MARKER :
                                values[i][j] == O_MARKER ? O_MARKER :
                                "-");
                System.out.print(BLANK_MARKER);
            }
            System.out.println();
        }
    }

    public int[] getScore() {
        int[] scores = new int[2];
        for (int i = 0; i < State.BOARD_SIZE ; i++) {
            for (int j = 0; j < State.BOARD_SIZE ; j++) {
                if(values[i][j] == X_MARKER) {
                    scores[0]++;
                } else if(values[i][j] == O_MARKER) {
                    scores[1]++;
                }
            }
        }
        return scores;
    }
}
