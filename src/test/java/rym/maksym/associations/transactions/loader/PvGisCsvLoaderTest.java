package rym.maksym.associations.transactions.loader;

import org.junit.jupiter.api.Test;
import rym.maksym.associations.transactions.Transactions;

import static org.junit.jupiter.api.Assertions.*;

class PvGisCsvLoaderTest {
    @Test
    void parseCsvFile() {
        Transactions<Double> transactions = PvGisCsvLoader.createTransactionsFrom("/c/Users/rumarm/Desktop/KAI/Кравченко/Input Data/Дані PVGIS за 2020-2021 рік.csv");
        assertEquals(17544, transactions.size(), "Transactions amount in csv file is 17544, but parsed another amount");
    }
}