package rym.maksym.associations.util;

import java.util.Objects;

public class GeneralNumericItem {
    private String description;
    private double value;

    public GeneralNumericItem(String description, double value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralNumericItem that = (GeneralNumericItem) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
