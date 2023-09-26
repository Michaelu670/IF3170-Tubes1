public class LocalSearchBot extends Bot{
    @Override
    protected int[] search() throws Exception {
        final int MAX_OBJ_VAL = 100;
        double maxVal = -MAX_OBJ_VAL;
        int[] nextMove = new int[2];
        for (int i = 0; i < State.BOARD_SIZE ; i++) {
            for (int j = 0; j < State.BOARD_SIZE ; j++) {
                try {
                    State nextState = this.state.move(i, j);
                    double heu = nextState.objectiveFunctionHeuristic();
                    if(nextState.objectiveFunctionHeuristic() > maxVal) {
                        nextMove[0] = i;
                        nextMove[1] = j;
                        maxVal = nextState.objectiveFunctionHeuristic();
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        return nextMove;
    }
}
