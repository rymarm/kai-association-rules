package rym.maksym;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rym.maksym.associations.Rule;
import rym.maksym.associations.RuleBuilder;
import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.itemset.ItemSet;
import rym.maksym.associations.transactions.Normalization;
import rym.maksym.associations.transactions.Transactions;
import rym.maksym.associations.transactions.loader.PvGisCsvLoader;
import rym.maksym.associations.transactions.loader.PvGisItem;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        URL csvFile = Application.class.getClassLoader().getResource("PVGIS-2020-2021.csv");
        Transactions<Double> transactions = PvGisCsvLoader
                .createTransactionsFrom(csvFile.getPath(),
                false);
        transactions = Normalization.minMax(transactions);
        Apriori apriori = new Apriori(0.4);
        Set<Item<Double>> itemsWithEnoughSupport = apriori.getItemsWithEnoughSupport(transactions);

        Set<Item<Double>> photovoltaicPowerItems = filterPvGisItems(itemsWithEnoughSupport, PvGisItem.PhotovoltaicPower);
        Set<Item<Double>> planeOfArrayItems = filterPvGisItems(itemsWithEnoughSupport, PvGisItem.PlaneOfArray);
        Set<Item<Double>> sunHeightItemItems = filterPvGisItems(itemsWithEnoughSupport, PvGisItem.SunHeightItem);
        Set<Item<Double>> airTemperatureItems = filterPvGisItems(itemsWithEnoughSupport, PvGisItem.AirTemperature);
        Set<Item<Double>> windSpeedItemItems = filterPvGisItems(itemsWithEnoughSupport, PvGisItem.WindSpeedItem);

        List<Rule<Double>> powerToAirTemperatureRules = new RuleBuilder<>()
                .addGiven(PvGisItem.PhotovoltaicPower)
                .addHave(PvGisItem.AirTemperature)
                .addTransaction(transactions)
                .addMinimalSupportLevel(0.4)
                .build();

        List<Rule<Double>> sunHeightToPOARules = new RuleBuilder<>()
                .addGiven(PvGisItem.SunHeightItem)
                .addHave(PvGisItem.PlaneOfArray)
                .addTransaction(transactions)
                .addMinimalSupportLevel(0.4)
                .build();

        List<Rule<Double>> poaToPowerRules = new RuleBuilder<>()
                .addGiven(PvGisItem.PlaneOfArray)
                .addHave(PvGisItem.PhotovoltaicPower)
                .addTransaction(transactions)
                .addMinimalSupportLevel(0.4)
                .build();

        List<Rule<Double>> poaAndSunHeightToPowerRules = new RuleBuilder<>()
                .addGiven(Set.of(PvGisItem.PlaneOfArray, PvGisItem.SunHeightItem))
                .addHave(PvGisItem.PhotovoltaicPower)
                .addTransaction(transactions)
                .addMinimalSupportLevel(0.4)
                .build();

        List<Rule<Double>> poaAndSunHeightToAirTemperatureRules = new RuleBuilder<>()
                .addGiven(Set.of(PvGisItem.PlaneOfArray, PvGisItem.SunHeightItem))
                .addHave(PvGisItem.AirTemperature)
                .addTransaction(transactions)
                .addMinimalSupportLevel(0.4)
                .build();

        List<Rule<Double>> windSpeedToAirTemperatureRules = new RuleBuilder<>()
                .addGiven(PvGisItem.PhotovoltaicPower)
                .addHave(PvGisItem.AirTemperature)
                .addTransaction(transactions)
                .addMinimalSupportLevel(0.4)
                .build();

        List<Rule<Double>> windSpeedAndPOAToPowerRules = new RuleBuilder<>()
                .addGiven(Set.of(PvGisItem.WindSpeedItem, PvGisItem.PlaneOfArray))
                .addHave(PvGisItem.PhotovoltaicPower)
                .addTransaction(transactions)
                .addMinimalSupportLevel(0.4)
                .build();

        List<Rule<Double>> windSpeedAndPOAToAirTemperatureRules = new RuleBuilder<>()
                .addGiven(Set.of(PvGisItem.WindSpeedItem, PvGisItem.PlaneOfArray))
                .addHave(PvGisItem.AirTemperature)
                .addTransaction(transactions)
                .addMinimalSupportLevel(0.4)
                .build();

        printRules(powerToAirTemperatureRules);
        printRules(sunHeightToPOARules);
        printRules(poaToPowerRules);
        printRules(poaAndSunHeightToPowerRules);
        printRules(poaAndSunHeightToAirTemperatureRules);
        printRules(windSpeedToAirTemperatureRules);
        printRules(windSpeedAndPOAToPowerRules);
        printRules(windSpeedAndPOAToAirTemperatureRules);
    }

    private static <T> void printRules(Collection<Rule<T>> rules) {
        rules.forEach(rule -> System.out.println(rule.toString()));
    }

    private static <T> Set<Item<T>> filterPvGisItems(Set<Item<T>> items, PvGisItem type) {
        Set<Item<T>> filteredItems = items.stream().filter(item -> item.getType().equals(type)).collect(Collectors.toSet());
        logger.info("Has {} items for {}.", filteredItems.size(), type);
        return filteredItems;
    }
}