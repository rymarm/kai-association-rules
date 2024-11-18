package rym.maksym.associations.transactions;

import rym.maksym.associations.itemset.ItemSet;

import java.util.*;

public class Transactions implements Iterable<ItemSet> {
    private ArrayList<ItemSet> transactions = new ArrayList<>();

    public void addItemSet(ItemSet itemSet) {
        transactions.add(itemSet);
    }

    public ArrayList<ItemSet> getTransactions() {
        return transactions;
    }

    public long size() {
        return transactions.size();
    }

    @Override
    public Iterator<ItemSet> iterator() {
        return transactions.iterator();
    }
}
