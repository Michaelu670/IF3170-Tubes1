import java.util.ArrayList;
import java.util.Random;

/**
 * Chromosome used in GeneticAlgorithmBot
 * Dependent on current state:
 * board with R remaining moves have Chromosome length of R
 *
 *
 */
public class Chromosome {
    private State initialState;
    private ArrayList<Integer> genome;

    public Chromosome(State initialState, ArrayList<Integer> genome) {
        this.initialState = initialState;
        this.genome = genome;
    }

    /**
     * mutate a chromosome with certain rate.
     * using swap as mutation
     * @param rate - mutation rate
     */
    public void mutate(double rate) {
        Random random = new Random();
        if (random.nextDouble() > rate) {
            return;
        }

        // if size < 2; no mutation possible
        if (genome.size() < 2) {
            return;
        }

        // pick two index to swap uniformly
        int index1 = random.nextInt(genome.size());
        int index2 = random.nextInt(genome.size() - 1);
        if (index2 >= index1) {
            index2++;
        }

        // swap the values on two index
        int temp = genome.get(index1);
        genome.set(index1, genome.get(index2));
        genome.set(index2, temp);
    }

    /**
     * calculate objective function for corresponding set of moves
     * @return double - objective value
     */
    public double objectiveFunction() {
        return toState().objectiveFunction();
    }

    public State toState() {
        try {
            State p = (State) initialState.clone();
            for (int pos: genome) {
                p = p.move(pos / 8, pos % 8);
            }
            return p;
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }


}
