import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ReservationTree {
    private Map<Integer, ReservationTree> childs;
    private boolean isTerminal;
    private boolean isMaxLevel;
    private double value;

    public ReservationTree(boolean isMaxLevel) {
        this.childs = new HashMap<>();
        this.isTerminal = false;
        this.isMaxLevel = isMaxLevel;
        this.value = isMaxLevel ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
    }

    public double getValue() {
        return value;
    }

    /**
     *
     * @param gene - gene stack; for fast popping
     */
    public void addGenome(Stack<Integer> gene, double objectiveValue) {
        if (gene.isEmpty()) {
            isTerminal = true;
            value = objectiveValue;
            return;
        }
        int nxt = gene.pop();

        // Create new child
        if (childs.containsKey(nxt)) {
            childs.get(nxt).addGenome(gene, objectiveValue);
        }
        else {
            childs.put(nxt, new ReservationTree(!isMaxLevel));
            childs.get(nxt).addGenome(gene, objectiveValue);
        }

        // Update value
        if (isMaxLevel) {
            value = Math.max(value, childs.get(nxt).getValue());
        }
    }

    /**
     *
     * @param gene - gene which fitness is being calculated
     * @param objectiveValue - objective value
     * @return fitness value
     */
    public int getFitness(Stack<Integer> gene, double objectiveValue) {
        return getFitnessRecursive(gene,objectiveValue).getKey();
    }

    private Pair<Integer, Boolean> getFitnessRecursive(Stack<Integer> gene, double objectiveValue) {
        if (isTerminal) return new Pair<>(1, true);
        Pair<Integer, Boolean> rec = childs.get(gene.pop()).getFitnessRecursive(gene, objectiveValue);
        if (!rec.getValue()) {
            return rec;
        }
        else if (value == objectiveValue) {
            return new Pair<>(rec.getKey() + 1, true);
        }
        else {
            return new Pair<>(rec.getKey(), false);
        }
    }
}
