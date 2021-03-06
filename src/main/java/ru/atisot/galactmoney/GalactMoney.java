package ru.atisot.galactmoney;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.atisot.galactmoney.locale.Locale;
import ru.atisot.galactmoney.papi.GalactMoneyExpansion;

import java.util.logging.Logger;

import static java.lang.String.format;

public class GalactMoney extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Locale locale;
    private static Config config;
    private static Economy economy = null;

    @Override
    public void onEnable() {
        config = new Config(this);
        locale = new Locale(this);
        locale.init(Config.getLocale());

        if (!setupEconomy() ) {
            log.severe(format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }

        this.getCommand("galact").setExecutor(new CommandManager(this));

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new GalactMoneyExpansion(this).register();
        } else {
            logger().info("Could not find PlaceholderAPI! Plugin placeholders can not work without it!");
        }

        logger().info("[" + this.getDescription().getName() + "]: Plugin has been initialized.");
    }

    @Override
    public void onDisable() {
        log.info(format("[%s] has been disabled!", this.getDescription().getName()));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    public Logger logger() {
        return log;
    }

    public Locale getLocale() {
        return locale;
    }

    public Config config() {
        return config;
    }

    public void setConfig(Config cfg) {
        config = cfg;
    }
}
