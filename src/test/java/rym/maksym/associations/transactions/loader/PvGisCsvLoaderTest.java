package rym.maksym.associations.transactions.loader;

import org.junit.jupiter.api.Test;
import rym.maksym.Application;
import rym.maksym.associations.transactions.Transactions;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class PvGisCsvLoaderTest {
    private static final URL CSV_FILE = Application.class.getClassLoader().getResource("PVGIS-2020-2021.csv");
    @Test
    void parseCsvFile() {
        Transactions<Double> transactions = PvGisCsvLoader.createTransactionsFrom(CSV_FILE.getPath());
        assertEquals(17544, transactions.size(), "Transactions amount in csv file is 17544, but parsed another amount");
    }
}