package rym.maksym;

import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSetBuilder;
import rym.maksym.associations.transactions.Transactions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Application {
    public static void main(String[] args) {
        Transactions demoTransactions = createTransactionsOfLetters("ABC",
                "ABD",
                "ABD",
                "BCE",
                "EF",
                "F");

        double minSupport = 0.5;
        double minConfidence = 0.7;

        Apriori apriori = new Apriori(minSupport);

        Map<Set<Item>, Double> frequentItemSets = apriori.calculateItemGroupsSupport(demoTransactions);
        apriori.generateAssociationRules(frequentItemSets, demoTransactions, minConfidence);
    }

    private static Transactions createTransactionsOfLetters(String... strings) {
        Transactions transactions = new Transactions();
        for (String itemSetString : strings) {
            ItemSetBuilder itemSetBuilder = new ItemSetBuilder();
            for (int charIndex = 0; charIndex < strings.length; charIndex++) {
                itemSetBuilder.addItem(Item.of(charIndex));
            }
            transactions.addItemSet(itemSetBuilder.build());
        }
        return transactions;
    }
}