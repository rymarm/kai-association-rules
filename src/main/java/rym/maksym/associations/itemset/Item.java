package rym.maksym.associations.itemset;

import java.util.Objects;

public class Item {
    private double numericValue;
    private final Object item;

    private final boolean isNumeric;

    public Item(Object item) {
        this.item = item;
        this.isNumeric = false;
    }

    public Item(Object item, double numericValue) {
        this.item = item;
        this.isNumeric = true;
        this.numericValue = numericValue;
    }

    public static Item of(Object item) {
        return new Item(item);
    }
    public double getValue() {
        return numericValue;
    }

    public Object getOriginalItem() {
        return item;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item1 = (Item) o;
        if (isNumeric) {
            return Double.compare(numericValue, item1.numericValue) == 0 && Objects.equals(item, item1.item);
        } else {
            return Objects.equals(item, item1.item);
        }
    }

    @Override
    public int hashCode() {
        if (isNumeric) {
            return Objects.hash(numericValue, item);
        } else {
            return Objects.hash(item);
        }
    }
}
