package rym.maksym.associations.transactions.loader;

import rym.maksym.associations.itemset.ItemType;

public enum PvGisItem implements ItemType {
    PhotovoltaicPower("𝑃𝑝𝑣"), PlaneOfArray("𝐺𝑖"), SunHeightItem("𝐻𝑠𝑢𝑛"), AirTemperature("𝑇2𝑚"),
    WindSpeedItem("𝑊𝑠");

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
