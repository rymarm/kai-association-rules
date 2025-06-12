package rym.maksym;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rym.maksym.associations.Rule;
import rym.maksym.associations.RuleBuilder;
import rym.maksym.associations.itemset.Item;
import rym.maksym.associations.transactions.Normalization;
import rym.maksym.associations.transactions.Transactions;
import rym.maksym.associations.transactions.loader.PvGisCsvLoader;
import rym.maksym.associations.transactions.loader.PvGisItem;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        outer: while (true) {
            terminal.writer().println("Please, provide PVGIS cvs file name or skip for using built in PVGIS-2020-2021.csv");
            String csvFilePath = reader.readLine("PVGIS file name: ")
                    .trim();

            if (csvFilePath.isBlank()) {
                terminal.writer().println("The built in PVGIS-2020-2021.csv will be used");
                terminal.flush();

                terminal.writer().println("Please, provide desirable minimal support level. The value should be < 1.0");
                terminal.writer().flush();
                double minSupportLevel;
                while (true) {
                    String minSupLevelString = reader.readLine("Minimal support level: ");
                    try {
                        minSupportLevel = Double.parseDouble(minSupLevelString);
                        if (minSupportLevel < 1) {
                            break;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
                start(csvFilePath, minSupportLevel);
            } else if (new File(csvFilePath).isFile()) {
                terminal.writer().println("You entered: " + csvFilePath);
                terminal.flush();

                terminal.writer().println("Please, provide desirable minimal support level. The value should be < 1.0");
                terminal.writer().flush();
                double minSupportLevel;
                while (true) {
                    String minSupLevelString = reader.readLine("Minimal support level: ");
                    try {
                        minSupportLevel = Double.parseDouble(minSupLevelString);
                        if (minSupportLevel < 1) {
                            break;
                        }
                    } catch (NumberFormatException ignored) {}
                }

                start(csvFilePath, minSupportLevel);
            } else if ("exit".equalsIgnoreCase(csvFilePath)) {
                break;
            } else {
                terminal.writer().println("ERROR: Incorrect path");
                continue;
            }

            terminal.writer().println();
            terminal.writer().println("Do you want restart?");
            terminal.flush();

            do {
                String answer = reader.readLine("Yes/No: ")
                        .trim();
                if (answer.equalsIgnoreCase("yes")) {
                    break;
                } else if (answer.equalsIgnoreCase("no")) {
                    break outer;
                }
            } while (true);
        }

        terminal.writer().println("Goodbye!");
        terminal.close();
    }

    private static void start(String csvFilePath, double minSupportLevel) {
        Transactions<Double> transactions = PvGisCsvLoader
                .createTransactionsFrom(csvFilePath,
                true);
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
                .addMinimalSupportLevel(minSupportLevel)
                .build();

        List<Rule<Double>> sunHeightToPOARules = new RuleBuilder<>()
                .addGiven(PvGisItem.SunHeightItem)
                .addHave(PvGisItem.PlaneOfArray)
                .addTransaction(transactions)
                .addMinimalSupportLevel(minSupportLevel)
                .build();

        List<Rule<Double>> poaToPowerRules = new RuleBuilder<>()
                .addGiven(PvGisItem.PlaneOfArray)
                .addHave(PvGisItem.PhotovoltaicPower)
                .addTransaction(transactions)
                .addMinimalSupportLevel(minSupportLevel)
                .build();

        List<Rule<Double>> poaAndSunHeightToPowerRules = new RuleBuilder<>()
                .addGiven(Set.of(PvGisItem.PlaneOfArray, PvGisItem.SunHeightItem))
                .addHave(PvGisItem.PhotovoltaicPower)
                .addTransaction(transactions)
                .addMinimalSupportLevel(minSupportLevel)
                .build();

        List<Rule<Double>> poaAndSunHeightToAirTemperatureRules = new RuleBuilder<>()
                .addGiven(Set.of(PvGisItem.PlaneOfArray, PvGisItem.SunHeightItem))
                .addHave(PvGisItem.AirTemperature)
                .addTransaction(transactions)
                .addMinimalSupportLevel(minSupportLevel)
                .build();

        List<Rule<Double>> windSpeedToAirTemperatureRules = new RuleBuilder<>()
                .addGiven(PvGisItem.PhotovoltaicPower)
                .addHave(PvGisItem.AirTemperature)
                .addTransaction(transactions)
                .addMinimalSupportLevel(minSupportLevel)
                .build();

        List<Rule<Double>> windSpeedAndPOAToPowerRules = new RuleBuilder<>()
                .addGiven(Set.of(PvGisItem.WindSpeedItem, PvGisItem.PlaneOfArray))
                .addHave(PvGisItem.PhotovoltaicPower)
                .addTransaction(transactions)
                .addMinimalSupportLevel(minSupportLevel)
                .build();

        List<Rule<Double>> windSpeedAndPOAToAirTemperatureRules = new RuleBuilder<>()
                .addGiven(Set.of(PvGisItem.WindSpeedItem, PvGisItem.PlaneOfArray))
                .addHave(PvGisItem.AirTemperature)
                .addTransaction(transactions)
                .addMinimalSupportLevel(minSupportLevel)
                .build();

        System.out.println("======================= Rules =======================");
        printRules(powerToAirTemperatureRules);
        printRules(sunHeightToPOARules);
        printRules(poaToPowerRules);
        printRules(poaAndSunHeightToPowerRules);
        printRules(poaAndSunHeightToAirTemperatureRules);
        printRules(windSpeedToAirTemperatureRules);
        printRules(windSpeedAndPOAToPowerRules);
        printRules(windSpeedAndPOAToAirTemperatureRules);
        System.out.println("=====================================================");
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