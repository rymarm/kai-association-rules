package rym.maksym;

import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSet;
import rym.maksym.associations.transactions.Normalization;
import rym.maksym.associations.transactions.Transactions;

import java.util.*;

public class Apriori {
    private final double minSupport;

    public Apriori(double minSupport) {
        this.minSupport = minSupport;
    }

    public Map<Set<Item>, Double> calculateItemGroupsSupport(Transactions transactions) {
        return calculateItemGroupsSupport(transactions, true);
    }

    public Map<Set<Item>, Double> calculateItemGroupsSupport(Transactions transactions, boolean normalizeItemsValues) {
        if (normalizeItemsValues) {
            transactions = Normalization.minMax(transactions);
        }

        Map<Set<Item>, Double> allItemGroupsSupport = new HashMap<>();
        Map<Set<Item>, Double> itemGroupsSupport = calculateSingleItemGroupSupport(transactions);

        while (!itemGroupsSupport.isEmpty()) {
            allItemGroupsSupport.putAll(itemGroupsSupport);
            itemGroupsSupport = combineAndPrune(itemGroupsSupport.keySet(), transactions);
        }
        return allItemGroupsSupport;
    }

    private Map<Set<Item>, Double> calculateSingleItemGroupSupport(Transactions transactions) {
        final int transactionCount = transactions.size();

        Map<Item, Integer> itemsFrequencyCount = new HashMap<>();
        for (ItemSet itemSet : transactions) {
            for (Item item : itemSet) {
                int  itemFrequencyCount = itemsFrequencyCount.getOrDefault(item, 0) + 1;
                itemsFrequencyCount.put(item, itemFrequencyCount);
            }
        }

        Map<Set<Item>, Double> singleItemGroupsSupport = new HashMap<>();
        itemsFrequencyCount.forEach((item, itemFrequencyCount) -> {
            double support = (double) itemFrequencyCount / transactionCount;
            if (support >= minSupport) {
                singleItemGroupsSupport.put(Set.of(item), support);
            }

        });
        return singleItemGroupsSupport;
    }

    private Map<Set<Item>, Double> combineAndPrune(Set<Set<Item>> itemGroupsSet, Transactions transactions) {
        Set<Set<Item>> combinedItemGroups = combine(itemGroupsSet);
        return prune(combinedItemGroups, transactions);
    }

    private Set<Set<Item>> combine(Set<Set<Item>> itemGroupsSet) {
        List<Set<Item>> itemGroupsList = List.copyOf(itemGroupsSet);

        int presentItemGroupSize = itemGroupsList.get(0).size();
        final int THE_NEXT_ITEM_GROUP_SIZE = presentItemGroupSize + 1;

        Set<Set<Item>> nextItemGroups = new HashSet<>();

        for (int itemGroupIndex = 0; itemGroupIndex < itemGroupsList.size(); itemGroupIndex++) {
            for (int nextItemGroupIndex = itemGroupIndex + 1; nextItemGroupIndex < itemGroupsList.size(); nextItemGroupIndex++) {
                Set<Item> newItemCombinationGroup = new HashSet<>(itemGroupsList.get(itemGroupIndex));
                newItemCombinationGroup.addAll(itemGroupsList.get(nextItemGroupIndex));

                if (newItemCombinationGroup.size() == THE_NEXT_ITEM_GROUP_SIZE) {
                    nextItemGroups.add(newItemCombinationGroup);
                }
            }
        }
        return nextItemGroups;
    }

    private Map<Set<Item>, Double> prune(Set<Set<Item>> itemGroupsList, Transactions transactions) {
        Map<Set<Item>, Double> itemGroupsSupport = new HashMap<>();
        for (Set<Item> itemGroup : itemGroupsList) {
            double support = calculateSupport(itemGroup, transactions);
            if (support >= minSupport) {
                itemGroupsSupport.put(itemGroup, support);
            }
        }

        return itemGroupsSupport;
    }

    private double calculateSupport(Set<Item> itemGroup, Transactions transactions) {
        int frequencyCount = 0;

        for (ItemSet itemSet : transactions) {
            if (itemSet.containsAll(itemGroup)) {
                frequencyCount++;
            }
        }
        return (double) frequencyCount / transactions.size();
    }

    public void generateAssociationRules(Map<Set<Item>, Double> itemGroupsSupport, Transactions transactions, double minConfidence) {
        for (Set<Item> itemsGroup : itemGroupsSupport.keySet()) {
            if (itemsGroup.size() < 2) {
                continue;
            }
            for (Item item : itemsGroup) {
                Set<Item> itemGroup = new HashSet<>(itemsGroup);
                itemGroup.remove(item);
                Set<Item> singleItem = Set.of(item);

                double itemGroupSupport = calculateSupport(itemGroup, transactions);
                double itemSetSupport = itemGroupsSupport.get(itemsGroup);
                double confidence = itemSetSupport / itemGroupSupport;

                double itemSupport = itemGroupsSupport.get(Set.of(item));
                double itemLift = confidence / itemSupport;

                if (confidence >= minConfidence) {
                    System.out.printf("Rule: %15s -> %10s (confidence: %.2f, support: %.2f, lift: %.2f)\n",
                            itemGroup, singleItem, confidence, itemSupport, itemLift);
                }
            }
        }
    }
}