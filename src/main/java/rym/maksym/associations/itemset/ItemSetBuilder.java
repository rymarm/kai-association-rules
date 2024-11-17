package rym.maksym.associations.itemset;

import java.time.LocalDateTime;
import java.util.*;

public class ItemSetBuilder {
    private Set<Item> items = new HashSet<>();
    private LocalDateTime transactionTime;

    public ItemSetBuilder addItem(Item... items) {
        Collections.addAll(this.items, items);
        return this;
    }
    public ItemSetBuilder addTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
        return this;
    }

    public ItemSet build() {
        return new ItemSet(items, transactionTime);
    }
}
