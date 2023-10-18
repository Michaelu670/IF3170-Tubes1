public class MinimaxBot extends Bot{
    protected static final double MAX_DEPTH_CONSTRAINT = 1e6;

    public MinimaxBot(char player_marker) {
       super(player_marker);
    }

    private double maximize(State state, double beta, int depth) throws InterruptedException {
        if(depth == 0) {
            return state.objectiveFunctionHeuristic(this.player_marker);
        }
        if(state.getTurnsLeft() <= 0) {
            return state.objectiveFunction(this.player_marker);
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        double maxValue = -MAX_OBJ_VAL;
        for (int i = 0;i < State.BOARD_SIZE;i++) {
            for (int j = 0;j < State.BOARD_SIZE;j++) {
                try {
                    if (state.getValue(i, j) != State.BLANK_MARKER) {
                        continue;
                    }
                    int[] dx = {1, -1, 0, 0, 0};
                    int[] dy = {0, 0, 1, -1, 0};
                    char[] save = {'u', 'u', 'u', 'u', 'u'};
                    char turn = state.getPlayerXTurn() ? State.X_MARKER : State.O_MARKER;
                    for (int k = 0; k < 5; k++) {
                        int nx = i + dx[k];
                        int ny = j + dy[k];
                        if (nx < 0 || ny < 0 || nx >= State.BOARD_SIZE || ny >= State.BOARD_SIZE) {
                            continue;
                        }
                        save[k] = state.getValue(nx, ny);
                        if (k == 4 || save[k] != State.BLANK_MARKER) {
                            state.setValue(nx, ny, turn);
                        }
                    }
                    state.moveParams();
                    double nextValue = minimize(state, maxValue, depth-1);
                    for (int k = 0; k < 5; k++) {
                        int nx = i + dx[k];
                        int ny = j + dy[k];
                        if (nx < 0 || ny < 0 || nx >= State.BOARD_SIZE || ny >= State.BOARD_SIZE) {
                            continue;
                        }
                        state.setValue(nx, ny, save[k]);
                    }
                    state.undoParams();
                    if(nextValue >= beta) {
                        return nextValue;
                    }

                    if(nextValue > maxValue) {
                        maxValue = nextValue;
                    }
                } catch (InterruptedException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }

        return maxValue;
    }
    
    private double minimize(State state, double alpha, int depth) throws InterruptedException {
        if(depth == 0) {
            return state.objectiveFunctionHeuristic(this.player_marker);
        }
        if(state.getTurnsLeft() <= 0) {
            return state.objectiveFunction(this.player_marker);
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        double minValue = MAX_OBJ_VAL;
        for (int i = 0;i < State.BOARD_SIZE;i++) {
            for (int j = 0;j < State.BOARD_SIZE;j++) {
                try {
                    if (state.getValue(i, j) != State.BLANK_MARKER) {
                        continue;
                    }
                    int[] dx = {1, -1, 0, 0, 0};
                    int[] dy = {0, 0, 1, -1, 0};
                    char[] save = {'u', 'u', 'u', 'u', 'u'};
                    char turn = state.getPlayerXTurn() ? State.X_MARKER : State.O_MARKER;
                    for (int k = 0; k < 5; k++) {
                        int nx = i + dx[k];
                        int ny = j + dy[k];
                        if (nx < 0 || ny < 0 || nx >= State.BOARD_SIZE || ny >= State.BOARD_SIZE) {
                            continue;
                        }
                        save[k] = state.getValue(nx, ny);
                        if (k == 4 || save[k] != State.BLANK_MARKER) {
                            state.setValue(nx, ny, turn);
                        }
                    }
                    state.moveParams();
                    double nextValue = maximize(state, minValue, depth-1);
                    for (int k = 0; k < 5; k++) {
                        int nx = i + dx[k];
                        int ny = j + dy[k];
                        if (nx < 0 || ny < 0 || nx >= State.BOARD_SIZE || ny >= State.BOARD_SIZE) {
                            continue;
                        }
                        state.setValue(nx, ny, save[k]);
                    }
                    state.undoParams();
                    if(nextValue <= alpha) {
                        return nextValue;
                    }

                    if(nextValue < minValue) {
                        minValue = nextValue;
                    }
                } catch (InterruptedException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }

        return minValue;
    }

    @Override
    protected int[] search() throws Exception {

        int[] nextMove = new int[2];
        int emptyTiles = state.emptyCount();
        int maxDepth = 0;
        double tempMove = 1;
        int remainingTurns = state.getTurnsLeft();
        while (remainingTurns > maxDepth && emptyTiles > 0 && tempMove * emptyTiles < MinimaxBot.MAX_DEPTH_CONSTRAINT) {
            tempMove *= emptyTiles;
            emptyTiles--;
            maxDepth++;
        }

//        System.out.println("Max depth : " + maxDepth);

        double maxValue = -MAX_OBJ_VAL;
        for (int i = 0;i < State.BOARD_SIZE;i++) {
            for (int j = 0;j < State.BOARD_SIZE;j++) {
                try {
                    State nextState = this.state.move(i, j);
                    double nextValue = minimize(nextState, -MAX_OBJ_VAL, maxDepth);
                    if(maxValue < nextValue) {
                        nextMove[0] = i;
                        nextMove[1] = j;

                        maxValue = nextValue;
                    }
                } catch (InterruptedException e) {
                    throw e;
                } catch (Exception e) {
                    continue;
                }
            }
        }

        return nextMove;
    }
}
