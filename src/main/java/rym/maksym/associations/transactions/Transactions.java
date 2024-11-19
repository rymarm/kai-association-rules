package rym.maksym.associations.transactions;

import rym.maksym.associations.itemset.ItemSet;
import rym.maksym.associations.itemset.ItemSetBuilder;

import java.util.*;

public class Transactions<T> implements Iterable<ItemSet<T>> {
    private final ArrayList<ItemSet<T>> transactions = new ArrayList<>();

    public Transactions<T> addItemSet(ItemSet<T> itemSet) {
        transactions.add(itemSet);
        return this;
    }

    public ArrayList<ItemSet<T>> getTransactions() {
        return transactions;
    }

    public int size() {
        return transactions.size();
    }

    public ItemSetBuilder<T> createItemSetBuilder() {
        return new ItemSetBuilder<>();
    }

    @Override
    public Iterator<ItemSet<T>> iterator() {
        return transactions.iterator();
    }
}
