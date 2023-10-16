import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class GeneticAlgorithmBot extends Bot {

    private static final double MUTATION_RATE = 0.02;
    private static final int POOL_SIZE = 200;
    private static final int GENERATE_COUNT = 300;
    private static final int GENERATION_COUNT = 100;
    private static final int PARENT_JOIN_POOL_COUNT = 20;

    /**
     * Use genetic minimax to get next move
     * @return int[2] - next move
     */
    @Override
    protected int[] search() throws Exception {
        // Generate initial population
        ArrayList<Chromosome> genePool = new ArrayList<>();
        ChromosomeBuilder builder = new ChromosomeBuilder(state.getTurnsLeft(), state);

        // add sorted gene
        for (int i = 0; i < POOL_SIZE; i++) {
            genePool.add(builder.build());
        }
        genePool = topChromosomes(genePool, POOL_SIZE);

        for (int i = 0; i < GENERATION_COUNT; i++) {
            genePool = createNextGeneration(genePool);
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
//            System.out.println(genePool.get(0).getGenome());
//            System.out.println(Arrays.deepToString(genePool.get(0).toState().getValues()));
//            System.out.println(genePool.get(0).objectiveFunction());
        }

//        System.out.println(Arrays.deepToString(genePool.get(0).toState().getValues()));
//        System.out.println(genePool.get(0).objectiveFunction());
        int boardPos = genePool.get(0).getGenome().get(0);

        return new int[]{boardPos / State.BOARD_SIZE, boardPos % State.BOARD_SIZE};
    }

    private ArrayList<Chromosome> createNextGeneration(ArrayList<Chromosome> genePool) {
        // Create next generation:
        ArrayList<Chromosome> newGenePool = new ArrayList<>();

        //     Count fitness value
        int[] fitnessValues = new int[genePool.size()];
        ReservationTree reservationTree = new ReservationTree(true);

        for (Chromosome chromosome: genePool) {
            reservationTree.addGenome(chromosome.getGeneStack(), chromosome.objectiveFunction());
        }

        int ctr = 0;
        for (Chromosome chromosome: genePool) {
            fitnessValues[ctr] = reservationTree.getFitness(chromosome.getGeneStack(), chromosome.objectiveFunction()) * 100 + (int)chromosome.objectiveFunction();
            if (ctr > 0) {
                fitnessValues[ctr] += fitnessValues[ctr-1];
            }
            ++ctr;
        }

        //     Crossover based on fitness value
        int totalValues = fitnessValues[fitnessValues.length-1];

        Random random = new Random();
        for (int i = 0; i < GENERATE_COUNT / 2; i++) {
            int parent1rand = random.nextInt(1, totalValues+1);
            int parent2rand = random.nextInt(1, totalValues+1);
            int parent1 = Arrays.binarySearch(fitnessValues, parent1rand);
            int parent2 = Arrays.binarySearch(fitnessValues, parent2rand);
            if (parent1 < 0) parent1 = -parent1 - 1;
            if (parent2 < 0) parent2 = -parent2 - 1;

            try {
                newGenePool.addAll(Chromosome.crossover(genePool.get(parent1), genePool.get(parent2)));
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

        //     Mutate at certain rate
        for (Chromosome chromosome : newGenePool) {
            chromosome.mutate(MUTATION_RATE);
        }

        //     Include some of best parent genes
        newGenePool.addAll(genePool.stream().limit(PARENT_JOIN_POOL_COUNT).toList());

        //     Select best genes?
        newGenePool = topChromosomes(newGenePool, POOL_SIZE);


        return newGenePool;
    }

    /**
     *
     * @param genePool
     * @param limit number of chromosomes
     * @return top chromosomes from genePool
     */
    private ArrayList<Chromosome> topChromosomes(ArrayList<Chromosome> genePool, int limit) {
        ArrayList<Pair<Double, Chromosome>> pairs = new ArrayList<>();
        ReservationTree reservationTree = new ReservationTree(true);

        for (Chromosome chromosome: genePool) {
            reservationTree.addGenome(chromosome.getGeneStack(), chromosome.objectiveFunction());
        }

        for (Chromosome chromosome: genePool) {
            pairs.add(new Pair<>(reservationTree.getFitness(chromosome.getGeneStack(), chromosome.objectiveFunction()) * 1.0, chromosome));
        }

        // TODO remove after debug
//        for (Pair<Double, Chromosome> pair: pairs) {
//            System.out.println(pair);
//            System.out.println(pair.getValue().objectiveFunction());
//            pair.getValue().toState().printState();
//        }
//        System.out.println();

        return pairs.stream()
                .sorted((o1, o2) -> Double.compare(o2.getKey(), o1.getKey()))
                .limit(limit)
                .map(Pair::getValue)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
