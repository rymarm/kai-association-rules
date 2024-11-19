package rym.maksym;

import org.junit.jupiter.api.Test;
import rym.maksym.associations.itemset.CharItemType;
import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSetBuilder;
import rym.maksym.associations.transactions.Transactions;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AprioriTest {

    @Test
    void generalSimpleTest() {
        //given
        Transactions<Character> transactions = createTransactionsOfLetters("ABC",
                "ABD",
                "ABD",
                "BCE",
                "EF",
                "F");
        Apriori apriori = new Apriori(0.5);

        //sut
        Map<Set<Item<Character>>, Double> itemGroupsSupport = apriori.calculateItemGroupsSupport(transactions);

        /*
        verification. Should be:
        A
        B
        AB
         */
        assertEquals(3, itemGroupsSupport.size());
    }

    private Transactions<Character> createTransactionsOfLetters(String... strings) {
        Transactions<Character> transactions = new Transactions<>();
        for (String itemSetString : strings) {
            ItemSetBuilder<Character> itemSetBuilder = new ItemSetBuilder<>();
            for (char item : itemSetString.toCharArray()) {
                CharItemType itemType = new CharItemType(item);
                itemSetBuilder.addItem(Item.of(itemType, item));
            }
            transactions.addItemSet(itemSetBuilder.build());
        }
        return transactions;
    }
}