package rym.maksym;

import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSet;
import rym.maksym.associations.transactions.Normalization;
import rym.maksym.associations.transactions.Transactions;
import rym.maksym.associations.util.Math;

import java.util.*;

public class Apriori {
    private final double minSupport;

    public Apriori(double minSupport) {
        this.minSupport = minSupport;
    }

    public <T> Map<Set<Item<T>>, Double> calculateItemGroupsSupport(Transactions<T> transactions) {
        return calculateItemGroupsSupport(transactions, true);
    }

    public <T> Map<Set<Item<T>>, Double> calculateItemGroupsSupport(Transactions<T> transactions, boolean normalizeItemsValues) {
        if (normalizeItemsValues) {
            transactions = Normalization.minMax(transactions);
        }

        Map<Set<Item<T>>, Double> allItemGroupsSupport = new HashMap<>();
        Map<Set<Item<T>>, Double> itemGroupsSupport = calculateSingleItemGroupSupport(transactions);

        while (!itemGroupsSupport.isEmpty()) {
            allItemGroupsSupport.putAll(itemGroupsSupport);
            itemGroupsSupport = combineAndPrune(itemGroupsSupport.keySet(), transactions);
        }
        return allItemGroupsSupport;
    }

    public <T> Map<Set<Item<T>>, Double> calculateSingleItemGroupSupport(Transactions<T> transactions) {
        Map<Set<Item<T>>, Double> singleItemGroupsSupport = new HashMap<>();

        Map<Item<T>, Integer> itemsFrequencyCount = calculateItemFrequencyCount(transactions);

        itemsFrequencyCount.forEach((item, itemFrequencyCount) -> {
            double support = (double) itemFrequencyCount / transactions.size();
            support = Math.round(support, 4);
            if (support >= minSupport) {
                singleItemGroupsSupport.put(Set.of(item), support);
            }

        });
        return singleItemGroupsSupport;
    }

    public <T> Set<Item<T>> getItemsWithEnoughSupport(Transactions<T> transactions) {
        Map<Item<T>, Integer> itemsFrequencyCount = calculateItemFrequencyCount(transactions);

        Set<Item<T>> items = new HashSet<>();
        itemsFrequencyCount.forEach((item, itemFrequencyCount) -> {
            double support = (double) itemFrequencyCount / transactions.size();
            support = Math.round(support, 4);
            if (support >= minSupport) {
                items.add(item);
            }

        });
        return items;
    }

    private <T> Map<Set<Item<T>>, Double> combineAndPrune(Set<Set<Item<T>>> itemGroupsSet, Transactions<T> transactions) {
        Set<Set<Item<T>>> combinedItemGroups = combine(itemGroupsSet);
        return prune(combinedItemGroups, transactions);
    }

    public <T> Set<Set<Item<T>>> combine(Set<Set<Item<T>>> itemGroupsSet) {
        List<Set<Item<T>>> itemGroupsList = List.copyOf(itemGroupsSet);

        int presentItemGroupSize = itemGroupsList.get(0).size();
        final int THE_NEXT_ITEM_GROUP_SIZE = presentItemGroupSize + 1;

        Set<Set<Item<T>>> nextItemGroups = new HashSet<>();

        for (int itemGroupIndex = 0; itemGroupIndex < itemGroupsList.size(); itemGroupIndex++) {
            for (int nextItemGroupIndex = itemGroupIndex + 1; nextItemGroupIndex < itemGroupsList.size(); nextItemGroupIndex++) {
                Set<Item<T>> newItemCombinationGroup = new HashSet<>(itemGroupsList.get(itemGroupIndex));
                newItemCombinationGroup.addAll(itemGroupsList.get(nextItemGroupIndex));

                if (newItemCombinationGroup.size() == THE_NEXT_ITEM_GROUP_SIZE) {
                    nextItemGroups.add(newItemCombinationGroup);
                }
            }
        }
        return nextItemGroups;
    }

    private <E> void combine(List<Item<E>> items, int itemsListStart, Set<Item<E>> currentCombination,
                             int combinationSize, List<Set<Item<E>>> combinatonsList) {
        if (currentCombination.size() == combinationSize) {
            combinatonsList.add(new HashSet<>(currentCombination));
            return;
        }

        // Iterate over the remaining elements
        for (int itemIndex = itemsListStart; itemIndex < items.size(); itemIndex++) {
            currentCombination.add(items.get(itemIndex));
            combine(items, itemIndex + 1, currentCombination, combinationSize, combinatonsList);
            currentCombination.remove(items.get(itemIndex)); // Backtrack
        }
    }

    private <T> Map<Set<Item<T>>, Double> prune(Set<Set<Item<T>>> itemGroupsList, Transactions<T> transactions) {
        Map<Set<Item<T>>, Double> itemGroupsSupport = new HashMap<>();
        for (Set<Item<T>> itemGroup : itemGroupsList) {
            double support = calculateSupport(itemGroup, transactions);
            if (support >= minSupport) {
                itemGroupsSupport.put(itemGroup, support);
            }
        }

        return itemGroupsSupport;
    }

    private <T> double calculateSupport(Set<Item<T>> itemsGroup, Transactions<T> transactions) {
        int frequencyCount = calculateItemsGroupFrequencyCount(itemsGroup, transactions);
        double support = (double) frequencyCount / transactions.size();
        return Math.round(support, 4);
    }

    private <T> Map<Item<T>, Integer> calculateItemFrequencyCount(Transactions<T> transactions) {
        Map<Item<T>, Integer> itemsFrequencyCount = new HashMap<>();
        for (ItemSet<T> itemSet : transactions) {
            for (Item<T> item : itemSet) {
                int itemFrequencyCount = itemsFrequencyCount.getOrDefault(item, 0) + 1;
                itemsFrequencyCount.put(item, itemFrequencyCount);
            }
        }
        return itemsFrequencyCount;
    }

    public <T> int calculateItemsGroupFrequencyCount(Set<Item<T>> itemsGroup, Transactions<T> transactions) {
        int frequencyCount = 0;

        for (ItemSet<T> itemSet : transactions) {
            if (itemSet.containsAll(itemsGroup)) {
                frequencyCount++;
            }
        }
        return frequencyCount;
    }

    public <T> int calculateItemFrequencyCount(Item<T> item, Transactions<T> transactions) {
        int frequencyCount = 0;

        for (ItemSet<T> itemSet : transactions) {
            if (itemSet.contains(item)) {
                frequencyCount++;
            }
        }
        return frequencyCount;
    }

    public <T> void generateAssociationRules(Map<Set<Item<T>>, Double> itemGroupsSupport, Transactions<T> transactions, double minConfidence) {
        for (Set<Item<T>> itemsGroup : itemGroupsSupport.keySet()) {
            if (itemsGroup.size() < 2) {
                continue;
            }
            for (Item<T> item : itemsGroup) {
                Set<Item<T>> itemGroup = new HashSet<>(itemsGroup);
                itemGroup.remove(item);
                Set<Item<T>> singleItem = Set.of(item);

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