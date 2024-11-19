package rym.maksym.associations.itemset;

import java.util.Objects;

public class Item<T> {
    private double numericValue;
    private final T item;

    private final ItemType type;

    public Item(ItemType type, T item) {
        this.type = type;
        this.item = item;
    }

    public Item(ItemType type, T item, double numericValue) {
        this.type = type;
        this.item = item;
        this.numericValue = numericValue;
    }

    public static <E> Item<E> of(ItemType type, E item) {
        return new Item<>(type, item);
    }
    public double getValue() {
        return numericValue;
    }

    public ItemType getType() {
        return type;
    }

    public T getOriginalItem() {
        return item;
    }

    public boolean isNumeric() {
        return type.isNumeric();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item<?> item = (Item<?>) o;
        return Double.compare(numericValue, item.numericValue) == 0 && Objects.equals(type, item.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numericValue, type);
    }
}
