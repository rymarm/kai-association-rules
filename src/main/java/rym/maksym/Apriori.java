package rym.maksym;

import java.util.*;

public class Apriori {
    private final double minSupport;

    public Apriori(double minSupport) {
        this.minSupport = minSupport;
    }

    /**
     * Calculate support of item sets for transactions
     * @param transactions list of item transactions
     */
    public Map<Set<String>, Double> apriori(List<Set<String>> transactions) {
        Map<Set<String>, Double> allFrequentItemSets = new HashMap<>();
        Map<Set<String>, Double> itemsSupport = calculateSingleItemSetOccurrence(transactions);

        while (!itemsSupport.isEmpty()) {
            allFrequentItemSets.putAll(itemsSupport);
            itemsSupport = combineAndPrune(itemsSupport.keySet(), transactions);
        }
        return allFrequentItemSets;
    }

    /**
     * Calculate  items support from the transactions and filter out the lower than {@link #minSupport}
     * @param transactions - list of transactions
     * @return map of items with their support value
     */
    private Map<Set<String>, Double> calculateSingleItemSetOccurrence(List<Set<String>> transactions) {
        final int totalTransactions = transactions.size();

        Map<String, Integer> itemsOccurrenceFrequency = new HashMap<>();
        for (Set<String> transaction : transactions) {
            for (String item : transaction) {
                itemsOccurrenceFrequency.put(item, itemsOccurrenceFrequency.getOrDefault(item, 0) + 1);
            }
        }

        Map<Set<String>, Double> itemsSupport = new HashMap<>();
        for (Map.Entry<String, Integer> entry : itemsOccurrenceFrequency.entrySet()) {
            double support = (double) entry.getValue() / totalTransactions;
            if (support >= minSupport) {
                itemsSupport.put(Set.of(entry.getKey()), support);
            }
        }
        return itemsSupport;
    }

    private Map<Set<String>, Double> combineAndPrune(Set<Set<String>> itemSetsList, List<Set<String>> transactions) {
        Set<Set<String>> newItemSets = combine(List.copyOf(itemSetsList));
        return prune(newItemSets, transactions);
    }

    /**
     * Combine the item sets into new sets with +1 size
     * @param itemSetsList list of item sets to combine
     */
    private Set<Set<String>> combine(List<Set<String>> itemSetsList) {
        int presentItemSetSize = itemSetsList.get(0).size();
        final int THE_NEXT_ITEM_SET_SIZE = presentItemSetSize + 1;

        Set<Set<String>> nextItemSets = new HashSet<>();

        for (int itemSetNumber = 0; itemSetNumber < itemSetsList.size(); itemSetNumber++) {
            for (int nextItemSetNumber = itemSetNumber + 1; nextItemSetNumber < itemSetsList.size(); nextItemSetNumber++) {
                Set<String> newItemCombinationSet = new HashSet<>(itemSetsList.get(itemSetNumber));
                newItemCombinationSet.addAll(itemSetsList.get(nextItemSetNumber));

                if (newItemCombinationSet.size() == THE_NEXT_ITEM_SET_SIZE) {
                    nextItemSets.add(newItemCombinationSet);
                }
            }
        }
        return nextItemSets;
    }

    /**
     * Calculate combination sets support and prune sets with lower than {@link #minSupport} threshold
     * @param itemSetsList list of item sets to prune
     * @param transactions transactions list the item sets are belong
     */
    private Map<Set<String>, Double> prune(Set<Set<String>> itemSetsList, List<Set<String>> transactions) {
        Map<Set<String>, Double> itemSetsSupport = new HashMap<>();
        for (Set<String> itemSet : itemSetsList) {
            double support = calculateSupport(itemSet, transactions);
            if (support >= minSupport) {
                itemSetsSupport.put(itemSet, support);
            }
        }

        return itemSetsSupport;
    }


    private double calculateSupport(Set<String> itemSet, List<Set<String>> transactions) {
        int count = calculateSetOccurrence(itemSet, transactions);
        return (double) count / transactions.size();
    }

    private int calculateSetOccurrence(Set<String> itemSet, List<Set<String>> transactions) {
        int count = 0;
        for (Set<String> transaction : transactions) {
            if (transaction.containsAll(itemSet)) {
                count++;
            }
        }
        return count;
    }

    public void generateAssociationRules(Map<Set<String>, Double> frequentItemSets, List<Set<String>> transactions, double minConfidence) {
        for (Set<String> itemSet : frequentItemSets.keySet()) {
            if (itemSet.size() < 2) {
                continue;
            }
            for (String item : itemSet) {
                Set<String> itemGroup = new HashSet<>(itemSet);
                itemGroup.remove(item);
                Set<String> singleItem = Set.of(item);

                double itemGroupSupport = calculateSupport(itemGroup, transactions);
                double itemSetSupport = frequentItemSets.get(itemSet);
                double confidence = itemSetSupport / itemGroupSupport;

                double itemSupport = frequentItemSets.get(Set.of(item));
                double itemLift = confidence / itemSupport;

                if (confidence >= minConfidence) {
                    System.out.printf("Rule: %15s -> %10s (confidence: %.2f, support: %.2f, lift: %.2f)\n",
                            itemGroup, singleItem, confidence, itemSupport, itemLift);
                }
            }
        }
    }
}