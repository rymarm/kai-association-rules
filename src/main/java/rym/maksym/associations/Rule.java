package rym.maksym.associations;

import rym.maksym.associations.itemset.Item;

import java.util.*;
import java.util.stream.Collectors;

public class Rule<T> {
    private final Set<Item<T>> given;
    private final Item<T> have;

    private final Map<Item<T>, Double> itemsSupport;

    private double support;
    private double confidence;
    private double lift;
    private double leverage;

    public Rule(Set<Item<T>> given, Item<T> have) {
        this.given = given;
        this.have = have;

        Map<Item<T>, Double> localItemsSupport = new HashMap<>();
        given.forEach(item -> localItemsSupport.put(item, null));
        localItemsSupport.put(have, null);
        this.itemsSupport = Collections.unmodifiableMap(localItemsSupport);
    }

    public Set<Item<T>> getGiven() {
        return given;
    }

    public Item<T> getHave() {
        return have;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        if (support > 1 || support < 0)
            throw new IllegalArgumentException("Support should be in range [0; 1]");
        this.support = support;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getLift() {
        return lift;
    }

    public void setLift(double lift) {
        this.lift = lift;
    }

    public double getLeverage() {
        return leverage;
    }

    public void setLeverage(double leverage) {
        this.leverage = leverage;
    }

    public Double getItemSupport(Item<T> item) {
        return itemsSupport.get(item);
    }

    public Set<Item<T>> getRuleItems() {
        Set<Item<T>> ruleItems = new HashSet<>(given);
        ruleItems.add(have);
        return ruleItems;
    }

    public String getRuleName() {
        StringBuilder ruleNameBuilder = new StringBuilder();
        ruleNameBuilder.append("Rule ");
        Iterator<Item<T>> givenIterator = given.iterator();
        while (givenIterator.hasNext()) {
            Item<T> item = givenIterator.next();
            String typeShortName = item.getType().getShortName();
            ruleNameBuilder
                    .append(typeShortName)
                    .append("(")
                    .append(item.getOriginalItem())
                    .append(")")
                    .append(" ");
            if (givenIterator.hasNext()) {
                ruleNameBuilder.append("∩ ");
            }
        }
        ruleNameBuilder
                .append("-> ")
                .append(have.getType().getShortName())
                .append("(")
                .append(have.getOriginalItem())
                .append(")");
        return ruleNameBuilder.toString();
    }

    @Override
    public String toString() {
        return getRuleName() +
                " support=" + support +
                ", confidence=" + confidence +
                ", lift=" + lift +
                ", leverage=" + leverage +
                '}';
    }
}
