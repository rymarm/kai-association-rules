package rym.maksym.associations.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rym.maksym.Application;
import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSet;
import rym.maksym.associations.transactions.loader.PvGisCsvLoader;

import java.net.URL;

class NormalizationTest {
    private static final URL CSV_FILE = Application.class.getClassLoader().getResource("PVGIS-2020-2021.csv");

    @Test
    void allValuesAreBiggerThanZeroAndLowerThanOne() {
        //given
        Transactions<?> transactions = PvGisCsvLoader.createTransactionsFrom(CSV_FILE.getPath());

        //sut
        Transactions<?> normilizedTransactions = Normalization.minMax(transactions);

        //verification
        Assertions.assertEquals(17544, normilizedTransactions.size());
        for(ItemSet<?> itemSet : normilizedTransactions) {
            for (Item<?> item : itemSet) {
                double itemValue = item.getValue();
                Assertions.assertTrue(itemValue <= 1 && itemValue >= 0, "Actual value is " + itemValue);
            }
        }
    }
}