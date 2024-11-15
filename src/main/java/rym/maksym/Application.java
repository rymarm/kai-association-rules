package rym.maksym;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Application {
    public static void main(String[] args) {
        List<Set<String>> transactions = Arrays.asList(
                Set.of("Milk", "Bread"),
                Set.of("Milk", "Diaper", "Beer", "Eggs"),
                Set.of("Milk", "Diaper", "Beer", "Cola"),
                Set.of("Diaper", "Beer", "Cola"),
                Set.of("Milk", "Bread", "Diaper", "Beer"),
                Set.of("Milk", "Bread", "Diaper", "Cola")
        );

        List<Set<String>> demoTransactions = Arrays.asList(
                Set.of("A", "B", "C"),
                Set.of("B", "C", "D"),
                Set.of("D", "E"),
                Set.of("A", "B", "D"),
                Set.of("A", "B", "C", "E"),
                Set.of("A", "B", "C", "D")
        );

        double minSupport = 0.5;
        double minConfidence = 0.7;

        Apriori apriori = new Apriori(minSupport);

        Map<Set<String>, Double> frequentItemSets = apriori.apriori(demoTransactions);
        apriori.generateAssociationRules(frequentItemSets, demoTransactions, minConfidence);
    }
}