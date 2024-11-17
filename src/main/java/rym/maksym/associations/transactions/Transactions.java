package rym.maksym.associations.transactions;

import rym.maksym.associations.itemset.ItemSet;

import java.util.*;

public class Transactions {
    private ArrayList<ItemSet> transactions = new ArrayList<>();

    public void addItemSet(ItemSet itemSet) {
        transactions.add(itemSet);
    }

    public long size() {
        return transactions.size();
    }
}
