package rym.maksym.associations.transactions;

import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSet;
import rym.maksym.associations.itemset.ItemSetBuilder;
import rym.maksym.associations.itemset.ItemType;
import rym.maksym.associations.util.MinMax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Normalization {
    public static Transactions minMax(Transactions transactions) {
        Map<ItemType, MinMax> itemsMinMax = findMinMax(transactions);
        Transactions normalizedTransactions = new Transactions();
        for (ItemSet itemSet : transactions) {
            ItemSetBuilder normalizedItemSetBuilder = new ItemSetBuilder();
            normalizedItemSetBuilder.addTransactionTime(itemSet.getTransactionTime());
            for (Item item : itemSet) {
                Item normalizedItem = normalizeItemValue(item, itemsMinMax);
                normalizedItemSetBuilder.addItem(normalizedItem);
            }
            normalizedTransactions.addItemSet(normalizedItemSetBuilder.build());
        }
        return normalizedTransactions;
    }

    private static Item normalizeItemValue(Item item, Map<ItemType, MinMax> itemsMinMax) {
        MinMax itemMinMaxValues = itemsMinMax.get(item.getType());
        double normalizedValue = (item.getValue() - itemMinMaxValues.getMin()) /
                (itemMinMaxValues.getMax() - itemMinMaxValues.getMin());
        normalizedValue = new BigDecimal(normalizedValue)
                .setScale(4, RoundingMode.HALF_UP)
                .doubleValue();
        return new Item(normalizedValue, item.getType());
    }

    private static Map<ItemType, MinMax> findMinMax(Transactions transactions) {
        Map<ItemType, MinMax> itemsMinMax = new HashMap<>();
        for (ItemSet itemSet : transactions) {
            for(Item item : itemSet) {
                MinMax itemMinMaxValue = itemsMinMax.get(item.getType());
                if (itemMinMaxValue == null) {
                    itemMinMaxValue = new MinMax(0, 0);
                    itemsMinMax.put(item.getType(), itemMinMaxValue);
                }
                itemMinMaxValue.update(item.getValue());
            }
        }
        return itemsMinMax;
    }
}
