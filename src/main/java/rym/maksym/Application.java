package rym.maksym;

import rym.maksym.associations.itemset.CharItemType;
import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSetBuilder;
import rym.maksym.associations.transactions.Transactions;
import rym.maksym.associations.transactions.loader.PvGisCsvLoader;

import java.util.Map;
import java.util.Set;

public class Application {
    public static void main(String[] args) {
        Transactions<Double> transactions = PvGisCsvLoader.createTransactionsFrom("/c/Users/rumarm/Desktop/KAI/Кравченко/Input Data/Дані PVGIS за 2020-2021 рік.csv");
        Apriori apriori = new Apriori(0.5);
        Map<Set<Item<Double>>, Double> setDoubleMap = apriori.calculateItemGroupsSupport(transactions);
        System.out.println("GG");
    }

}