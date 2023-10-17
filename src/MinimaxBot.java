public class MinimaxBot extends Bot{
    protected static final double MAX_DEPTH_CONSTRAINT = 1e18;

    public MinimaxBot(String player_marker) {
       super(player_marker);
    }

    private double maximize(State state, double beta, int depth) throws InterruptedException {
        if(depth == 0) {
            return state.objectiveFunctionHeuristic(this.player_marker);
        }
        if(state.getTurnsLeft() <= 0) {
            return state.objectiveFunction();
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        double maxValue = -MAX_OBJ_VAL;
        for (int i = 0;i < State.BOARD_SIZE;i++) {
            for (int j = 0;j < State.BOARD_SIZE;j++) {
                try {
                    State nextState = state.move(i, j);
                    double nextValue = minimize(nextState, maxValue, depth-1);
                    if(nextValue > beta) {
                        return nextValue;
                    }

                    if(nextValue > maxValue) {
                        maxValue = nextValue;
                    }
                } catch (InterruptedException e) {
                    throw e;
                } catch (Exception e) {
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
            return state.objectiveFunction();
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        double minValue = MAX_OBJ_VAL;
        for (int i = 0;i < State.BOARD_SIZE;i++) {
            for (int j = 0;j < State.BOARD_SIZE;j++) {
                try {
                    State nextState = state.move(i, j);
                    double nextValue = maximize(nextState, minValue, depth-1);
                    if(nextValue < alpha) {
                        return nextValue;
                    }

                    if(nextValue < minValue) {
                        minValue = nextValue;
                    }
                } catch (InterruptedException e) {
                    throw e;
                } catch (Exception e) {
                    continue;
                }
            }
        }

        return minValue;
    }

    @Override
    protected int[] search() throws Exception {
        double maxValue = -MAX_OBJ_VAL;

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
