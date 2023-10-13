public class MinimaxBot extends Bot{
    // for a-b pruning
    private double alpha;
    private double beta;

    private double maximize(State state) {
        if(state.getTurnsLeft() <= 0) {
            return state.objectiveFunction();
        }
        double maxValue = -MAX_OBJ_VAL;
        for (int i = 0;i < State.BOARD_SIZE;i++) {
            for (int j = 0;j < State.BOARD_SIZE;j++) {
                try {
                    State nextState = state.move(i, j);
                    double nextValue = minimize(nextState);
                    if(nextValue > beta) {
                        return nextValue;
                    }

                    if(nextValue > maxValue) {
                        maxValue = nextValue;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        if(maxValue < beta) {
            beta = maxValue;
        }
        return maxValue;
    }
    
    private double minimize(State state) {
        if(state.getTurnsLeft() <= 0) {
            return state.objectiveFunction();
        }
        double minValue = MAX_OBJ_VAL;
        for (int i = 0;i < State.BOARD_SIZE;i++) {
            for (int j = 0;j < State.BOARD_SIZE;j++) {
                try {
                    State nextState = state.move(i, j);
                    double nextValue = maximize(nextState);
                    if(nextValue < alpha) {
                        return nextValue;
                    }

                    if(nextValue < minValue) {
                        minValue = nextValue;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        if(minValue > alpha) {
            alpha = minValue;
        }
        return minValue;
    }

    @Override
    protected int[] search() throws Exception {
        alpha = -MAX_OBJ_VAL;
        beta = MAX_OBJ_VAL;
        double maxValue = -MAX_OBJ_VAL;
        int[] nextMove = new int[2];

        for (int i = 0;i < State.BOARD_SIZE;i++) {
            for (int j = 0;j < State.BOARD_SIZE;j++) {
                try {
                    State nextState = this.state.move(i, j);
                    double nextValue = maximize(nextState);
                    if(maxValue < nextValue) {
                        nextMove[0] = i;
                        nextMove[1] = j;

                        maxValue = nextValue;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        return nextMove;
    }
}
