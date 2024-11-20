package rym.maksym.associations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rym.maksym.Apriori;
import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemType;
import rym.maksym.associations.transactions.Transactions;
import rym.maksym.associations.util.ItemsCombiner;
import rym.maksym.associations.util.Math;

import java.util.*;
import java.util.stream.Collectors;

public class RuleBuilder<T> {
    private static final Logger logger = LoggerFactory.getLogger(RuleBuilder.class);
    private Set<ItemType> givenItemTypes;
    private ItemType havingItemType;
    private double minimalSupportLevel;
    private Transactions<T> transactions;
    private Apriori apriori;
    private List<Rule<T>> rules;

    public RuleBuilder addGiven(Collection<ItemType> items) {
        givenItemTypes = Set.copyOf(items);
        return this;
    }

    public RuleBuilder addGiven(ItemType item) {
        givenItemTypes = Set.of(item);
        return this;
    }

    public RuleBuilder addHave(ItemType item) {
        havingItemType = item;
        return this;
    }

    public RuleBuilder addMinimalSupportLevel(double threshold) {
        if (threshold > 1 || threshold < 0)
            throw new IllegalArgumentException("Support should be in range [0; 1]");
        minimalSupportLevel = threshold;
        apriori = new Apriori(minimalSupportLevel);
        return this;
    }

    public RuleBuilder addTransaction(Transactions<T> transactions) {
        this.transactions = transactions;
        return this;
    }

    public List<Rule<T>> build() {
        Set<Item<T>> itemsWithEnoughSupport = apriori.getItemsWithEnoughSupport(transactions);

        Set<ItemType> ruleItems = new HashSet<>(givenItemTypes);
        ruleItems.add(havingItemType);

        Set<Item<T>> items = filterItems(itemsWithEnoughSupport, ruleItems);

        if (items.isEmpty()) {
            return Collections.emptyList();
        }
        Set<ItemType> missingRuleItems = findMissingItemTypes(items, ruleItems);
        if (!missingRuleItems.isEmpty()) {
            logger.info("No items with {} support for types {}", minimalSupportLevel, missingRuleItems);
            return Collections.emptyList();
        }

        createRules(items, ruleItems);

        return rules;
    }

    private void createRules(Set<Item<T>> items, Set<ItemType> ruleItems) {
        rules = new ArrayList<>();

        List<Set<Item<T>>> ruleItemsCombinations = ItemsCombiner.combine(items, ruleItems.size());
        for (Set<Item<T>> newRuleItems : ruleItemsCombinations) {
            Set<Item<T>> given = new HashSet<>(givenItemTypes.size());
            Item<T> have = null;
            for (ItemType givenItemType : givenItemTypes) {
                for (Item<T> item : newRuleItems) {
                    if (item.getType().equals(givenItemType)) {
                        given.add(item);
                    } else {
                        assert have == null;
                        have = item;
                    }
                }
            }
            Rule<T> newRule = new Rule<>(given, have);
            setRuleSupport(newRule);
            setRuleConfidence(newRule);
            setRuleLift(newRule, newRule.getConfidence());
            setRuleLeverage(newRule, newRule.getSupport());
            rules.add(newRule);
        }

    }

    private void setRuleSupport(Rule<T> rule) {
        int ruleItemsFrequencyCount = apriori.calculateItemsGroupFrequencyCount(rule.getRuleItems(), transactions);
        double ruleSupport = (double) ruleItemsFrequencyCount / transactions.size();
        ruleSupport = Math.round(ruleSupport, 2);
        rule.setSupport(ruleSupport);
    }

    private void setRuleConfidence(Rule<T> rule) {
        int ruleItemsFrequencyCount = apriori.calculateItemsGroupFrequencyCount(rule.getRuleItems(), transactions);
        int havingItemFrequencyCount = apriori.calculateItemFrequencyCount(rule.getHave(), transactions);
        double ruleConfidence = (double) ruleItemsFrequencyCount / havingItemFrequencyCount;
        ruleConfidence = Math.round(ruleConfidence, 2);
        rule.setConfidence(ruleConfidence);
    }


    private void setRuleLift(Rule<T> rule, double ruleConfidence) {
        int havingItemFrequencyCount = apriori.calculateItemFrequencyCount(rule.getHave(), transactions);
        double havingSupport = (double) havingItemFrequencyCount / transactions.size();
        double ruleLift = ruleConfidence / havingSupport;
        ruleLift = Math.round(ruleLift, 2);
        rule.setLift(ruleLift);
    }

    private void setRuleLeverage(Rule<T> rule, double ruleSupport) {
        int givenItemsFrequencyCount = apriori.calculateItemsGroupFrequencyCount(rule.getGiven(), transactions);
        int havingItemFrequencyCount = apriori.calculateItemFrequencyCount(rule.getHave(), transactions);

        double givenSupport = (double) givenItemsFrequencyCount / transactions.size();
        double havingSupport = (double) havingItemFrequencyCount / transactions.size();
        double ruleLeverage = ruleSupport - (givenSupport * havingSupport);
        ruleLeverage = Math.round(ruleLeverage, 2);
        rule.setLeverage(ruleLeverage);
    }

    private static <T> Set<Item<T>> filterItems(Set<Item<T>> items, ItemType type) {
        Set<Item<T>> filteredItems = items.stream().filter(item -> item.getType().equals(type)).collect(Collectors.toSet());
        logger.info("Has {} items for {}.", filteredItems.size(), type);
        return filteredItems;
    }

    private static <T> boolean eachItemTypeIsPresent(Set<Item<T>> items, Set<ItemType> types) {
        int uniqItemTypes = items.stream().map(item -> item.getType())
                .collect(Collectors.toSet())
                .size();
        return uniqItemTypes == types.size();
    }

    private static <T> Set<ItemType> findMissingItemTypes(Set<Item<T>> items, Set<ItemType> types) {
        Set<ItemType> missingTypes = new HashSet<>();

        for (ItemType type : types) {
            boolean found = false;

            for (Item<T> item : items) {
                if (item.getType().equals(type)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                missingTypes.add(type);
            }
        }
        return missingTypes;
    }

    private static <T> Set<Item<T>> filterItems(Set<Item<T>> items, Set<ItemType> types) {
        Set<Item<T>> filteredItems = items.stream()
                .filter(item -> types.contains(item.getType()))
                .collect(Collectors.toSet());
        logger.info("Has {} items for {}.", filteredItems.size(), types);
        return filteredItems;
    }
}
