package rym.maksym.associations.itemset;

import java.util.Objects;

public class AirTemperatureItem implements Item {
    private double value;

    public AirTemperatureItem(double value) {
        this.value = value;
    }

    public AirTemperatureItem(String value) {
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
        AirTemperatureItem that = (AirTemperatureItem) o;
        return Double.compare(value, that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
