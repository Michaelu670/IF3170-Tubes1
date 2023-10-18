import java.util.*;

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
    char player_marker;

    public Chromosome(State initialState, ArrayList<Integer> genome, char player_marker) {
        this.initialState = initialState;
        this.genome = genome;
        this.player_marker = player_marker;
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
        return toState().objectiveFunctionHeuristic(player_marker);
    }

    public State toState() {
        try {
            State p = (State) initialState.clone();
            for (int pos: genome) {
                p.moveThis(pos / 8, pos % 8);
            }
            return p;
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stack<Integer> getGeneStack() {
        Stack<Integer> res = new Stack<>();
        for (int i = genome.size() - 1; i >= 0; i--) {
            res.push(genome.get(i));
        }
        return res;
    }

    public ArrayList<Integer> getGenome() {
        return genome;
    }

    /**
     *
     * @param parent1 - parent chromosome 1
     * @param parent2 - parent chromosome 2
     * @return Chromosome[2] - result of crossover
     */
    public static ArrayList<Chromosome> crossover(Chromosome parent1, Chromosome parent2) throws Exception {
        ArrayList<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(parent1.clone());
        chromosomes.add(parent2.clone());
        Random random = new Random();
        int point1 = random.nextInt(parent1.genome.size());
        int point2 = random.nextInt(parent1.genome.size());

        // Make sure point1 <= point 2
        if (point1 > point2) {
            int tmp = point1;
            point1 = point2;
            point2 = tmp;
        }

        // Swap from point1 to point2 (inclusive)
        // Maintain mapping relationship
        Map<Integer, Integer> forwardSwap = new HashMap<>();
        Map<Integer, Integer> backwardSwap = new HashMap<>();

        for (int i = point1; i <= point2; i++) {
            if (parent1.genome.get(i).equals(parent2.genome.get(i))) continue;
            // Note the swapping relation
            forwardSwap.put(parent1.genome.get(i), parent2.genome.get(i));
            backwardSwap.put(parent2.genome.get(i), parent1.genome.get(i));

            // Swap!
            chromosomes.get(0).genome.set(i, parent2.genome.get(i));
            chromosomes.get(1).genome.set(i, parent1.genome.get(i));
        }

        Map<Integer, Integer> swaps = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        for (int i = point1; i <= point2; i++) {
            if (visited.contains(parent1.genome.get(i))) continue;
            boolean isLooped = false;
            int fwdkey = parent1.genome.get(i);
            int backey = parent1.genome.get(i);
            visited.add(fwdkey);
            while(forwardSwap.containsKey(fwdkey)) {
                fwdkey = forwardSwap.get(fwdkey);
                if (visited.contains(fwdkey)) {
                    isLooped = true;
                    break;
                }
                visited.add(fwdkey);
            }
            while(backwardSwap.containsKey(backey)) {
                backey = backwardSwap.get(backey);
                if (visited.contains(backey)) {
                    isLooped = true;
                    break;
                }
                visited.add(backey);
            }

            if (isLooped) continue;
            swaps.put(fwdkey, backey);
            swaps.put(backey, fwdkey);
        }

        // Legalize chromosome
        for (int i = 0; i < parent1.genome.size(); i++) {
            if (point1 <= i && i <= point2) continue;
            if (swaps.containsKey(chromosomes.get(0).genome.get(i))) {
                chromosomes.get(0).genome.set(i, swaps.get(chromosomes.get(0).genome.get(i)));
            }
            if (swaps.containsKey(chromosomes.get(1).genome.get(i))) {
                chromosomes.get(1).genome.set(i, swaps.get(chromosomes.get(1).genome.get(i)));
            }
        }

        return chromosomes;
    }

    @Override
    public String toString() {
        return genome.toString();
    }

    @Override
    protected Chromosome clone() throws CloneNotSupportedException {
        Chromosome clone = new Chromosome(initialState, (ArrayList<Integer>) genome.clone(), player_marker);
        return clone;
    }
}
