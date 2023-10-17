import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    public Chromosome buildGood() {
        ArrayList<Pair<Double, Integer>> emptyAndVal = new ArrayList<>();
        ArrayList<Integer> empty = new ArrayList<>();
        double curValue = state.objectiveFunctionHeuristic();

        // get all empty positions
        for (int i = 0; i < State.BOARD_SIZE; i++) {
            for (int j = 0; j < State.BOARD_SIZE; j++) {
                if (state.getValue(i, j).isBlank()) {
                    double nxtValue = state.move(i, j).objectiveFunctionHeuristic();
                    emptyAndVal.add(new Pair<>(nxtValue-curValue,i * State.BOARD_SIZE + j));
                    empty.add(i * State.BOARD_SIZE + j);
                }
            }
        }

        // choose good first move; using double random
        emptyAndVal.sort((a, b) -> (int) (b.getKey()-a.getKey()));
        int firstMove = emptyAndVal.get(
                ThreadLocalRandom.current().nextInt(
                        ThreadLocalRandom.current().nextInt(emptyAndVal.size()) + 1
                )).getValue();
        empty.removeAll(List.of(firstMove));

        // choose turnsLeft-1 other move
        Collections.shuffle(empty);
        ArrayList<Integer> genome = new ArrayList<>();
        genome.add(firstMove);
        for (int i = 0; i < turnsLeft - 1; i++) {
            genome.add(empty.get(i));
        }
        return new Chromosome(state, genome);
    }


}
