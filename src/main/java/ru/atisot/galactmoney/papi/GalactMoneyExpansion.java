package ru.atisot.galactmoney.papi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.atisot.galactmoney.GalactMoney;
import ru.atisot.galactmoney.GalactMoneyAPI;

import static java.lang.String.format;

public class GalactMoneyExpansion extends PlaceholderExpansion {

    private GalactMoney plugin;

    public GalactMoneyExpansion(GalactMoney plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "galactmoney";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        // %galactmoney_balance%
        if(identifier.equals("balance")){
            return format("%.0f", GalactMoneyAPI.getBalance(player));
        }

        // %galactmoney_balance_{playername}%
        if(identifier.startsWith("balance_")){
            identifier = PlaceholderAPI.setBracketPlaceholders(player, identifier);
            try {
                String playername = identifier.replace("balance_", "");
                OfflinePlayer player2 = plugin.getServer().getOfflinePlayer(playername);
                if(player2 != null) {
                    return format("%.0f", GalactMoneyAPI.getBalance(player2));
                }
            } catch (Exception e) {
                plugin.logger().warning(e.getMessage());
                e.printStackTrace();
            }
        }

        // %galactmoney_rate%
        if(identifier.equals("rate")){
            return format("%.0f", GalactMoneyAPI.getExchangeRate());
        }

        // %galactmoney_sign%
        if(identifier.equals("sign")){
            return GalactMoneyAPI.getCurrencySign();
        }

        return null;
    }
}
