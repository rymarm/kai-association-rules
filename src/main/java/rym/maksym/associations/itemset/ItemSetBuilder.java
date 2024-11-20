package rym.maksym.associations.itemset;

import java.time.LocalDateTime;
import java.util.*;

public class ItemSetBuilder<T> {
    private Set<Item<T>> items = new HashSet<>();
    private LocalDateTime transactionTime;

    public ItemSetBuilder<T> addItem(Item<T> newItem) {
        Optional<Item<T>> sameTypeItem = items.stream()
                .filter(item -> newItem.getType().equals(item))
                .findFirst();
        if (sameTypeItem.isEmpty()) {
            this.items.add(newItem);
        }
        return this;
    }
    public ItemSetBuilder<T> addTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
        return this;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public ItemSet<T> build() {
        return new ItemSet<T>(items, transactionTime);
    }
}
