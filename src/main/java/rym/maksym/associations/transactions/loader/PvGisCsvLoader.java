package rym.maksym.associations.transactions.loader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import rym.maksym.associations.itemset.*;
import rym.maksym.associations.transactions.Transactions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PvGisCsvLoader {
    private static final Map<String, ItemType> HEADER_TO_ITEM_TYPE = Map.of(
            "P", ItemType.PhotovoltaicPower,
            "G(i)", ItemType.PlaneOfArray,
            "H_sun", ItemType.SunHeightItem,
            "T2m", ItemType.AirTemperature,
            "WS10m", ItemType.WindSpeedItem
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
            String itemValue = csvRecord.get(header);
            Item item = new Item(itemValue, itemType);
            itemSetBuilder.addItem(item);
        });
        return itemSetBuilder.build();
    }
}
