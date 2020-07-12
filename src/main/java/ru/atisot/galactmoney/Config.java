package ru.atisot.galactmoney;

public class Config {
    private GalactMoney plugin;
    private static String   dbHost = "127.0.0.1";
    private static short    dbPort = 3306;
    private static String   dbUsername = "root";
    private static String   dbPassword = "";
    private static String   dbName = "test";
    private static String   dbTable = "test";
    private static String   dbBalanceColumn = "balance";
    private static String   dbUsernameColumn = "name";
    private static String   currencySign = "G";
    private static int      currencyCourse = 100;
    private static String   locale = "en_US";
    private static String   prefix;

    public Config(GalactMoney plugin) {
        this.plugin = plugin;
        this.init();
        prefix = "[" + plugin.getDescription().getName() + "] ";
        this.plugin.logger().info("[" + plugin.getDescription().getName() + "]: Plugin has been initialized.");
    }

    private void init() {
        if(!plugin.getConfig().isSet("prefix")) {
            plugin.getConfig().set("prefix", prefix);
        } else {
            prefix = plugin.getConfig().getString("prefix");
        }
        if(!plugin.getConfig().isSet("locale")) {
            plugin.getConfig().set("locale", locale);
        } else {
            locale = plugin.getConfig().getString("locale");
        }
        if(!plugin.getConfig().isSet("connect.host")) {
            plugin.getConfig().set("connect.host", dbHost);
        } else {
            dbHost = plugin.getConfig().getString("connect.host");
        }
        if(!plugin.getConfig().isSet("connect.port")) {
            plugin.getConfig().set("connect.port", dbPort);
        } else {
            dbPort = (short) plugin.getConfig().getInt("connect.port");
        }
        if(!plugin.getConfig().isSet("connect.username")) {
            plugin.getConfig().set("connect.username", dbUsername);
        } else {
            dbUsername = plugin.getConfig().getString("connect.username");
        }
        if(!plugin.getConfig().isSet("connect.password")) {
            plugin.getConfig().set("connect.password", dbPassword);
        } else {
            dbPassword = plugin.getConfig().getString("connect.password");
        }
        if(!plugin.getConfig().isSet("connect.dbname")) {
            plugin.getConfig().set("connect.dbname", dbName);
        } else {
            dbName = plugin.getConfig().getString("connect.dbname");
        }
        if(!plugin.getConfig().isSet("connect.table")) {
            plugin.getConfig().set("connect.table", dbTable);
        } else {
            dbTable = plugin.getConfig().getString("connect.table");
        }
        if(!plugin.getConfig().isSet("connect.balance_column")) {
            plugin.getConfig().set("connect.balance_column", dbBalanceColumn);
        } else {
            dbBalanceColumn = plugin.getConfig().getString("connect.balance_column");
        }
        if(!plugin.getConfig().isSet("connect.username_column")) {
            plugin.getConfig().set("connect.username_column", dbUsernameColumn);
        } else {
            dbUsernameColumn = plugin.getConfig().getString("connect.username_column");
        }

        if(!plugin.getConfig().isSet("currency.sign")) {
            plugin.getConfig().set("currency.sign", currencySign);
        } else {
            currencySign = plugin.getConfig().getString("currency.sign");
        }
        if(!plugin.getConfig().isSet("currency.course")) {
            plugin.getConfig().set("currency.course", currencyCourse);
        } else {
            currencyCourse = plugin.getConfig().getInt("currency.course");
        }

        plugin.saveConfig();
    }

    public static String getPrefix() {
        return prefix;
    }

    public static String getLocale() {
        return locale;
    }

    public static String getHost() {
        return dbHost;
    }

    public static short getPort() {
        return dbPort;
    }

    public static String getUsername() {
        return dbUsername;
    }

    public static String getPassword() {
        return dbPassword;
    }

    public static String getDataBaseName() {
        return dbName;
    }

    public static String getDataBaseTable() {
        return dbTable;
    }

    public static String getBalanceColumn() {
        return dbBalanceColumn;
    }

    public static String getUsernameColumn() {
        return dbUsernameColumn;
    }

    public static String getCurrencySign() {
        return currencySign;
    }

    public static int getCurrencyCourse() {
        return currencyCourse;
    }
}