package rym.maksym.associations.transactions;

import rym.maksym.associations.itemset.*;
import rym.maksym.associations.transactions.loader.PvGisItem;
import rym.maksym.associations.util.MinMax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Normalization {
    public static Transactions minMax(Transactions transactions) {
        Map<Object, MinMax> itemsMinMax = findMinMax(transactions);
        Transactions normalizedTransactions = new Transactions();
        for (ItemSet itemSet : transactions) {
            ItemSetBuilder normalizedItemSetBuilder = new ItemSetBuilder();
            normalizedItemSetBuilder.addTransactionTime(itemSet.getTransactionTime());

            for (Item item : itemSet) {
                if (item.isNumeric()) {
                    Item normalizedItem = normalizeItemValue(item, itemsMinMax);
                    normalizedItemSetBuilder.addItem(normalizedItem);
                }
            }
            ItemSet newItemSet = normalizedItemSetBuilder.build();
            if (normalizedItemSetBuilder.isEmpty()) {
                newItemSet = itemSet;
            }
            normalizedTransactions.addItemSet(newItemSet);
        }
        return normalizedTransactions;
    }

    private static Item normalizeItemValue(Item item, Map<Object, MinMax> itemsMinMax) {
        MinMax itemMinMaxValues = itemsMinMax.get(item.getOriginalItem());
        double normalizedValue = (item.getValue() - itemMinMaxValues.getMin()) /
                (itemMinMaxValues.getMax() - itemMinMaxValues.getMin());
        normalizedValue = new BigDecimal(normalizedValue)
                .setScale(4, RoundingMode.HALF_UP)
                .doubleValue();
        return new Item(item.getOriginalItem(), normalizedValue);
    }

    private static Map<Object, MinMax> findMinMax(Transactions transactions) {
        Map<Object, MinMax> itemsMinMax = new HashMap<>();
        for (ItemSet itemSet : transactions) {
            for(Item item : itemSet) {
                if (item.isNumeric()) {
                    MinMax itemMinMaxValue = itemsMinMax.get(item.getOriginalItem());
                    if (itemMinMaxValue == null) {
                        itemMinMaxValue = new MinMax(0, 0);
                        itemsMinMax.put(item.getOriginalItem(), itemMinMaxValue);
                    }
                    itemMinMaxValue.update(item.getValue());
                }
            }
        }
        return itemsMinMax;
    }
}
