public class MinimaxBot extends Bot{
    protected static final double MAX_DEPTH_CONSTRAINT = 1e18;

    // alpha value, the lower bound of the search value
    private double lowerBound;
    // beta value, the upper bound of the search value
    private double upperBound;

    public MinimaxBot(String player_marker) {
        super(player_marker);
    }

    private double maximize(State state, int depth) throws InterruptedException {
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
                    double nextValue = minimize(nextState, depth-1);
                    if(nextValue > upperBound) {
                        // kandidat nilai pada maximize lebih besar dari upper bound parent
                        // (pasti tidak terpilih pada minimize parent)
                        // pencarian selanjutnya akan menghasilkan nilai lebih besar, prune
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

        if(maxValue < upperBound) {
            upperBound = maxValue;
        }
        return maxValue;
    }
    
    private double minimize(State state, int depth) throws InterruptedException {
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
                    double nextValue = maximize(nextState, depth-1);
                    if(nextValue < lowerBound) {
                        // kandidat nilai pada minimize lebih kecil dari lower bound parent
                        // (pasti tidak terpilih pada maximize parent)
                        // pencarian selanjutnya akan menghasilkan nilai lebih kecil, prune
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

        if(minValue > lowerBound) {
            lowerBound = minValue;
        }
        return minValue;
    }

    @Override
    protected int[] search() throws Exception {
        this.lowerBound = -MAX_OBJ_VAL;
        this.upperBound = MAX_OBJ_VAL;

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
        System.out.println("Max depth = " + maxDepth + "\n");

        double maxValue = -MAX_OBJ_VAL;
        for (int i = 0;i < State.BOARD_SIZE;i++) {
            for (int j = 0;j < State.BOARD_SIZE;j++) {
                try {
                    State nextState = this.state.move(i, j);
                    double nextValue = minimize(nextState, maxDepth);
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

        // Reset alpha & beta value used for pruning to 0, just in case
        this.lowerBound = -MAX_OBJ_VAL;
        this.upperBound = MAX_OBJ_VAL;
        return nextMove;
    }
}
