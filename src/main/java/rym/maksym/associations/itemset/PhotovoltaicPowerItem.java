package rym.maksym.associations.itemset;

import java.util.Objects;

public class PhotovoltaicPowerItem implements Item {
    private double value;

    public PhotovoltaicPowerItem(double value) {
        this.value = value;
    }

    public PhotovoltaicPowerItem(String value) {
        this.value = Double.valueOf(value);
    }

    @Override
    public String value() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotovoltaicPowerItem that = (PhotovoltaicPowerItem) o;
        return Double.compare(value, that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
