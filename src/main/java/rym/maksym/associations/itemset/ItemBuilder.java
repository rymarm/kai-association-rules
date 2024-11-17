package rym.maksym.associations.itemset;

public class ItemBuilder {
    public static Item build(ItemType type, String value) {
        switch (type) {
            case PhotovoltaicPower:
                return new PhotovoltaicPowerItem(value);
            case PlaneOfArray:
                return new PlaneOfArrayItem(value);
            case SunHeightItem:
                return new SunHeightItem(value);
            case AirTemperature:
                return new AirTemperatureItem(value);
            case WindSpeedItem:
                return new WindSpeedItem(value);
            default:
                throw new RuntimeException(String.format("No implementation for '%s' type", type));
        }
    }
}
