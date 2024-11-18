package rym.maksym.associations.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSet;
import rym.maksym.associations.transactions.loader.PvGisCsvLoader;

class NormalizationTest {
    @Test
    void allValuesAreBiggerThanZeroAndLowerThanOne() {
        //given
        Transactions transactions = PvGisCsvLoader.createTransactionsFrom("/c/Users/rumarm/Desktop/KAI/Кравченко/Input Data/Дані PVGIS за 2020-2021 рік.csv");

        //sut
        Transactions normilizedTransactions = Normalization.minMax(transactions);

        //verification
        Assertions.assertEquals(17544, normilizedTransactions.size());
        for(ItemSet itemSet : normilizedTransactions) {
            for (Item item : itemSet) {
                double itemValue = item.getValue();
                Assertions.assertTrue(itemValue <= 1 && itemValue >= 0, "Actual value is " + itemValue);
            }
        }
    }
}