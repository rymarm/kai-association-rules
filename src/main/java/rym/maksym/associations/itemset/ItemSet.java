package rym.maksym.associations.itemset;

import java.time.LocalDateTime;
import java.util.*;

public class ItemSet implements Iterable<Item> {
    private Set<Item> itemSet;
    private LocalDateTime transactionTime;

    public ItemSet(Set<Item> itemSet, LocalDateTime transactionTime) {
        this.itemSet = itemSet;
        this.transactionTime = transactionTime;
    }

    public Set<Item> getItemSet() {
        return Set.copyOf(itemSet);
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public boolean containsAll(Set<Item> itemGroup) {
        return itemSet.containsAll(itemGroup);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemSet itemSet1 = (ItemSet) o;
        return Objects.equals(itemSet, itemSet1.itemSet) && Objects.equals(transactionTime, itemSet1.transactionTime);
    }

    @Override
    public Iterator<Item> iterator() {
        return itemSet.iterator();
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemSet, transactionTime);
    }
}
