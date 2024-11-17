package rym.maksym.associations.itemset;

import java.util.Objects;

public class PlaneOfArrayItem implements Item {
    private double value;

    public PlaneOfArrayItem(double value) {
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
        PlaneOfArrayItem that = (PlaneOfArrayItem) o;
        return Double.compare(value, that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
