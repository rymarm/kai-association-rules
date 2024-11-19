package rym.maksym.associations.transactions;

import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSet;
import rym.maksym.associations.itemset.ItemSetBuilder;
import rym.maksym.associations.itemset.ItemType;
import rym.maksym.associations.util.Math;
import rym.maksym.associations.util.MinMax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Normalization {
    public static <T> Transactions<T> minMax(Transactions<T> transactions) {
        Map<ItemType, MinMax> itemsMinMax = findMinMax(transactions);
        Transactions<T> normalizedTransactions = new Transactions<>();
        for (ItemSet<T> itemSet : transactions) {
            ItemSetBuilder<T> normalizedItemSetBuilder = new ItemSetBuilder<>();
            normalizedItemSetBuilder.addTransactionTime(itemSet.getTransactionTime());

            for (Item<T> item : itemSet) {
                if (item.isNumeric()) {
                    Item<T> normalizedItem = normalizeItemValue(item, itemsMinMax);
                    normalizedItemSetBuilder.addItem(normalizedItem);
                }
            }
            ItemSet<T> newItemSet = normalizedItemSetBuilder.build();
            if (normalizedItemSetBuilder.isEmpty()) {
                newItemSet = itemSet;
            }
            normalizedTransactions.addItemSet(newItemSet);
        }
        return normalizedTransactions;
    }

    private static <T> Item<T> normalizeItemValue(Item<T> item, Map<ItemType, MinMax> itemsMinMax) {
        MinMax itemMinMaxValues = itemsMinMax.get(item.getType());
        double normalizedValue = (item.getValue() - itemMinMaxValues.getMin()) /
                (itemMinMaxValues.getMax() - itemMinMaxValues.getMin());
        normalizedValue = Math.round(normalizedValue, 4);
        return new Item<>(item.getType(), item.getOriginalItem(), normalizedValue);
    }

    private static <T> Map<ItemType, MinMax> findMinMax(Transactions<T> transactions) {
        Map<ItemType, MinMax> itemsMinMax = new HashMap<>();
        for (ItemSet<T> itemSet : transactions) {
            for(Item<T> item : itemSet) {
                if (item.isNumeric()) {
                    MinMax itemMinMaxValue = itemsMinMax.computeIfAbsent(item.getType(), (type) -> new MinMax(0, 0));
                    itemMinMaxValue.update(item.getValue());
                }
            }
        }
        return itemsMinMax;
    }
}
