package rym.maksym;

import org.junit.jupiter.api.Test;
import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSet;
import rym.maksym.associations.itemset.ItemSetBuilder;
import rym.maksym.associations.transactions.Transactions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AprioriTest {

    @Test
    void generalSimpleTest() {
        //given
        Transactions transactions = createTransactionsOfLetters("ABC",
                "ABD",
                "ABD",
                "BCE",
                "EF",
                "F");
        Apriori apriori = new Apriori(0.5);

        //sut
        Map<Set<Item>, Double> itemGroupsSupport = apriori.calculateItemGroupsSupport(transactions);

        /*
        verification. Should be:
        A
        B
        AB
         */
        assertEquals(3, itemGroupsSupport.size());
    }

    private Transactions createTransactionsOfLetters(String... strings) {
        Transactions transactions = new Transactions();
        for (String itemSetString : strings) {
            ItemSetBuilder itemSetBuilder = new ItemSetBuilder();
            for (int charIndex = 0; charIndex < itemSetString.length(); charIndex++) {
                char charItem = itemSetString.charAt(charIndex);
                itemSetBuilder.addItem(Item.of(charItem));
            }
            transactions.addItemSet(itemSetBuilder.build());
        }
        return transactions;
    }
}