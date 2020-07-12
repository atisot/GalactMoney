package ru.atisot.galactmoney;

import org.bukkit.OfflinePlayer;
import ru.atisot.galactmoney.base.DataBase;

public class GalactMoneyAPI {
    public static double getBalance(OfflinePlayer player) {
        return DataBase.getBalance(player.getName());
    }
    public static String getCurrencySign() {
        return Config.getCurrencySign();
    }
    public static double getExchangeRate() {
        return Config.getExchangeRate();
    }
}
