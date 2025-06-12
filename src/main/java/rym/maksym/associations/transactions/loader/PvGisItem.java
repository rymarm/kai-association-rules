package rym.maksym.associations.transactions.loader;

import rym.maksym.associations.itemset.ItemType;

public enum PvGisItem implements ItemType {
    PhotovoltaicPower("Ppv"), PlaneOfArray("Gi"), SunHeightItem("Hsun"), AirTemperature("T2m"),
    WindSpeedItem("Ws");

    private String shortName;

    PvGisItem(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    @Override
    public String getShortName() {
        return shortName;
    }
}
