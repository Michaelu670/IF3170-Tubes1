import javafx.scene.control.Button;

public class StateAdapter {
    private State state;
    public StateAdapter(Button[][] buttons, int roundsLeft, boolean playerXTurn) {
        String[][] stringVal = new String[State.BOARD_SIZE][State.BOARD_SIZE];
        for (int i = 0; i < State.BOARD_SIZE; i++) {
            for (int j = 0; j < State.BOARD_SIZE; j++) {
                stringVal[i][j] = buttons[i][j].getText();
            }
        }
        state = new State(stringVal, roundsLeft, playerXTurn);
    }
    public StateAdapter(State state, int roundsLeft) {
        try {
            this.state = (State) state.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        this.state.setTurnsLeft(roundsLeft);
    }
    public State getState() {
        return state;
    }
}
