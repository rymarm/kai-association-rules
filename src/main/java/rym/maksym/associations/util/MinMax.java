package rym.maksym.associations.util;

public class MinMax {
    private double min;
    private double max;

    public MinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public void update(double value) {
        if (value < min) {
            min = value;
        } if (value > max) {
            max = value;
        }
    }
}
