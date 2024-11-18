package rym.maksym.associations.transactions.loader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import rym.maksym.associations.itemset.*;
import rym.maksym.associations.transactions.Transactions;
import rym.maksym.associations.util.GeneralNumericItem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PvGisCsvLoader {
    private static final Map<String, PvGisItem> HEADER_TO_ITEM_TYPE = Map.of(
            "P", PvGisItem.PhotovoltaicPower,
            "G(i)", PvGisItem.PlaneOfArray,
            "H_sun", PvGisItem.SunHeightItem,
            "T2m", PvGisItem.AirTemperature,
            "WS10m", PvGisItem.WindSpeedItem
    );

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd:HHmm");
    private static final String TIME_HEADER = "time";

    public static Transactions createTransactionsFrom(String csvFilePath) {
        CSVParser csvParser = parseCsvFile(csvFilePath);

        Transactions transactions = new Transactions();
        csvParser.stream().forEach(csvRecord -> {
            ItemSet itemSet = parseTransaction(csvRecord);
            transactions.addItemSet(itemSet);
        });

        return transactions;
    }

    private static CSVParser parseCsvFile(String csvFilePath) {
        try {
            FileReader csvFile = new FileReader(csvFilePath);
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .build();
            return new CSVParser(csvFile, csvFormat);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Csv file %s is not found", csvFilePath), e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file.", e);
        }
    }

    private static ItemSet parseTransaction(CSVRecord csvRecord) {
        ItemSetBuilder itemSetBuilder = new ItemSetBuilder();

        String recordTimeString = csvRecord.get(TIME_HEADER);
        LocalDateTime recordTime = LocalDateTime.parse(recordTimeString, TIME_FORMAT);
        itemSetBuilder.addTransactionTime(recordTime);

        HEADER_TO_ITEM_TYPE.forEach((header, itemType) -> {
            String stringItemValue = csvRecord.get(header);
            double itemValue = Double.valueOf(stringItemValue);

            GeneralNumericItem pvGisItem = new GeneralNumericItem(itemType.toString(), itemValue);

            Item item = new Item(pvGisItem, itemValue);
            itemSetBuilder.addItem(item);
        });
        return itemSetBuilder.build();
    }
}
