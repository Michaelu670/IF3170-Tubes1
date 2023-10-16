import java.util.ArrayList;
import java.util.Collections;

public class ChromosomeBuilder {
    int turnsLeft;
    State state;
    public ChromosomeBuilder() {

    }

    public ChromosomeBuilder(int turnsLeft, State state) {
        this.turnsLeft = turnsLeft;
        this.state = state;
    }

    public void setTurnsLeft(int turnsLeft) {
        this.turnsLeft = turnsLeft;
    }

    public void setState(State state) {
        this.state = state;
    }

    /**
     *
     * @return a random valid chromosome
     */
    public Chromosome build() {
        ArrayList<Integer> empty = new ArrayList<>();
        for (int i = 0; i < State.BOARD_SIZE; i++) {
            for (int j = 0; j < State.BOARD_SIZE; j++) {
                if (state.getValue(i, j).isBlank()) {
                    empty.add(i * State.BOARD_SIZE + j);
                }
            }
        }

        Collections.shuffle(empty);
        ArrayList<Integer> genome = new ArrayList<>();
        for (int i = 0; i < turnsLeft; i++) {
            genome.add(empty.get(i));
        }
        return new Chromosome(state, genome);
    }


}
