public class MinimaxBot extends Bot{
    private double maximize(State state, double beta) throws InterruptedException {
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
                    double nextValue = minimize(nextState, maxValue);
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
    
    private double minimize(State state, double alpha) throws InterruptedException {
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
                    double nextValue = maximize(nextState, minValue);
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

        for (int i = 0;i < State.BOARD_SIZE;i++) {
            for (int j = 0;j < State.BOARD_SIZE;j++) {
                try {
                    State nextState = this.state.move(i, j);
                    double nextValue = maximize(nextState, MAX_OBJ_VAL);
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
