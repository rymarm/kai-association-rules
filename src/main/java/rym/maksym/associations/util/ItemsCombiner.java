package rym.maksym.associations.util;

import rym.maksym.associations.itemset.Item;

import java.util.*;
import java.util.stream.Collectors;

public class ItemsCombiner {

    public static <T> List<Set<Item<T>>> combine(Collection<Item<T>> items, int combinationSize) {
        List<Set<Item<T>>> combinations = new ArrayList<>();

        List<Item<T>> itemsList = new ArrayList<>(items);
        combiner(itemsList, 0, combinations, combinationSize, new HashSet<Item<T>>());
        return combinations;
    }
    private static <T> void combiner(List<Item<T>> items, int itemsListStart, List<Set<Item<T>>> combinatonsList, int combinationSize, Set<Item<T>> currentCombination) {
        if (!allItemsHaveUniqueType(currentCombination)) {
            return;
        }
        if (currentCombination.size() == combinationSize) {
            combinatonsList.add(new HashSet<>(currentCombination));
            return;
        }

        // Iterate over the remaining elements
        for (int itemIndex = itemsListStart; itemIndex < items.size(); itemIndex++) {
            currentCombination.add(items.get(itemIndex));
            combiner(items, itemIndex + 1, combinatonsList, combinationSize, currentCombination);
            currentCombination.remove(items.get(itemIndex)); // Backtrack
        }
    }

    public static <T> boolean allItemsHaveUniqueType(Collection<Item<T>> items) {
        int uniqItemTypesCount = items.stream()
                .map(Item::getType)
                .collect(Collectors.toSet())
                .size();
        return items.size() == uniqItemTypesCount;
    }
}
