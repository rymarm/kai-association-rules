package rym.maksym.associations.itemset;

import java.util.Objects;

public class CharItemType implements ItemType{
    private char item;

    public CharItemType(char item) {
        this.item = item;
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public String getShortName() {
        return Character.toString(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharItemType that = (CharItemType) o;
        return item == that.item;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }
}
