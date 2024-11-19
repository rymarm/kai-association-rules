package rym.maksym.associations.itemset;

import java.time.LocalDateTime;
import java.util.*;

public class ItemSet<T> implements Iterable<Item<T>> {
    private Set<Item<T>> itemSet;
    private LocalDateTime transactionTime;

    public ItemSet(Set<Item<T>> itemSet, LocalDateTime transactionTime) {
        this.itemSet = itemSet;
        this.transactionTime = transactionTime;
    }

    public Set<Item<T>> getItemSet() {
        return Set.copyOf(itemSet);
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public boolean containsAll(Set<Item<T>> itemGroup) {
        return itemSet.containsAll(itemGroup);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemSet<?> other = (ItemSet<?>) o;
        return Objects.equals(itemSet, other.itemSet) && Objects.equals(transactionTime, other.transactionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemSet, transactionTime);
    }

    @Override
    public Iterator<Item<T>> iterator() {
        return itemSet.iterator();
    }
}
