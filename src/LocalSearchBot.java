public class LocalSearchBot extends Bot{
    public LocalSearchBot(String player_marker) {
        super(player_marker);
    }

    @Override
    protected int[] search() throws Exception {
        double maxVal = -MAX_OBJ_VAL;
        int[] nextMove = new int[2];
        for (int i = 0; i < State.BOARD_SIZE ; i++) {
            for (int j = 0; j < State.BOARD_SIZE ; j++) {
                try {
                    State nextState = this.state.move(i, j);
                    double heuristicValue = nextState.objectiveFunctionHeuristic(this.player_marker);
                    if(heuristicValue >= maxVal) {
                        nextMove[0] = i;
                        nextMove[1] = j;
                        maxVal = heuristicValue;
                    }

                } catch (Exception e) {
                    continue;
                }
            }
        }

        return nextMove;
    }
}
