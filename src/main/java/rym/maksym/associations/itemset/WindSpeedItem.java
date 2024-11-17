package rym.maksym.associations.itemset;

import java.util.Objects;

public class WindSpeedItem implements Item {
    private double value;

    public WindSpeedItem(double value) {
        this.value = value;
    }

    @Override
    public String value() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WindSpeedItem that = (WindSpeedItem) o;
        return Double.compare(value, that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
