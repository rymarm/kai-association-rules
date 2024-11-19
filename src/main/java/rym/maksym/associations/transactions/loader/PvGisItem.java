package rym.maksym.associations.transactions.loader;

import rym.maksym.associations.itemset.ItemType;

public enum PvGisItem implements ItemType {
    PhotovoltaicPower, PlaneOfArray, SunHeightItem, AirTemperature, WindSpeedItem;

    @Override
    public boolean isNumeric() {
        return true;
    }
}
