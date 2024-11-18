package rym.maksym.associations.itemset;

import java.util.Objects;

public class Item {
    private double value;
    private final ItemType type;

    public Item(double value, ItemType type) {
        this.value = value;
        this.type = type;
    }

    public Item(String value, ItemType type) {
        this.value = Double.valueOf(value);
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(value, item.value) == 0 && type == item.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }
}
