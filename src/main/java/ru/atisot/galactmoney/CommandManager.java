package ru.atisot.galactmoney;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.atisot.galactmoney.base.DataBase;

import static java.lang.String.format;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class CommandManager implements CommandExecutor {
    private final GalactMoney plugin;

    public CommandManager(GalactMoney plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if(command.getLabel().equals("galact") || command.getLabel().equals("gal")) {

            String cmd = (args.length > 0) ? args[0] : "help";
            cmd = (cmd.equalsIgnoreCase("bal")) ? "balance" : cmd;
            cmd = (cmd.equalsIgnoreCase("exc")) ? "exchange" : cmd;

            String arg = (args.length > 1) ? args[1] : "";

            switch (cmd) {
                case "reload":
                    reloadConfig(sender);
                    break;
                case "setrate":
                    setExchangeRate(sender, arg);
                    break;
                case "setsign":
                    setCurrencySign(sender, arg);
                    break;
                case "balance":
                    viewBalance(sender, arg);
                    break;
                case "exchange":
                    exchangeCurrency(sender, arg);
                    break;
                case "rate":
                    viewExchangeRate(sender);
                    break;
                default:
                    helpCommand(sender);
            }

            return true;
        } else {
            return false;
        }
    }

    private void helpCommand(CommandSender sender) {

        String[] help = {
                plugin.getLocale().getString("help.balance"),
                plugin.getLocale().getString("help.rate"),
                plugin.getLocale().getString("help.exchange"),
                plugin.getLocale().getString("help.reload"),
                plugin.getLocale().getString("help.setrate"),
                plugin.getLocale().getString("help.setsign")
        };

        sendMessage(sender, help);
    }

    private boolean viewBalance(CommandSender sender, String arg) {

        if(arg.isEmpty()) {
            if(!(sender instanceof Player)) {
                plugin.logger().info("[" + plugin.getDescription().getName() + "]: " + "Only players can execute this command for yourself!");
                return true;
            }

            Player player = (Player) sender;
            sendMessage(player, plugin.getLocale().getString("balance.you", format("%.2f", DataBase.getBalance(player.getName())), Config.getCurrencySign()));
        } else if((sender instanceof Player) && arg.equalsIgnoreCase(sender.getName())) {
            sendMessage(sender, plugin.getLocale().getString("balance.you", format("%.2f", DataBase.getBalance(sender.getName())), Config.getCurrencySign()));
        } else if(!(sender instanceof Player) || sender.hasPermission("galactmoney.balance.other")) {
            OfflinePlayer player2 = plugin.getServer().getOfflinePlayer(arg);
            if(player2 != null) {
                sendMessage(sender, plugin.getLocale().getString("balance.other", player2.getName(), format("%.2f", DataBase.getBalance(player2.getName())), Config.getCurrencySign()));
            } else {
                sendMessage(sender, plugin.getLocale().getString("player.not_found", arg));
            }
        } else {
            sendMessage(sender, plugin.getLocale().getString("error.not_permission"));
        }

        return true;
    }

    private boolean viewExchangeRate(CommandSender sender) {
        sendMessage(sender, plugin.getLocale().getString("exchange.rate", Config.getCurrencySign(), format("%.2f", Config.getExchangeRate())));
        return true;
    }

    private boolean exchangeCurrency(CommandSender sender, String arg) {
        if(!(sender instanceof Player)) {
            plugin.logger().info("[" + plugin.getDescription().getName() + "]: " + "Only players can execute this command for yourself!");
            return true;
        }

        if(sender.hasPermission("galactmoney.exchange")) {
            if(arg.isEmpty()) {
                sendMessage(sender, plugin.getLocale().getString("exchange.get_amount"));
            } else {
                if(arg.matches("^\\d{1,3}(\\.\\d{1,2})?$")) {
                    double amount = Double.parseDouble(arg);
                    double balance = DataBase.getBalance(sender.getName());
                    amount = Math.min(balance, amount);

                    if(amount > 0) {
                        double excAmount = amount * Config.getExchangeRate();

                        if(DataBase.reduceBalance(sender.getName(), amount)) {
                            Player player = (Player) sender;
                            EconomyResponse r = plugin.getEconomy().depositPlayer(player, excAmount);
                            if(r.transactionSuccess()) {
                                sendMessage(player, plugin.getLocale().getString("exchange.done", Config.getCurrencySign() + amount, plugin.getEconomy().format(r.balance)));
                                plugin.logger().info("[" + plugin.getDescription().getName() + "]: " + format("Player %s exchanged %s%.2f.", player.getDisplayName(), Config.getCurrencySign(), amount));
                            } else {
                                sendMessage(player, plugin.getLocale().getString("error.exchange", r.errorMessage));
                                plugin.logger().info("[" + plugin.getDescription().getName() + "]: " + format("Аn error occurred while trying to exchange a player %s: %s", player.getDisplayName(), r.errorMessage));
                            }
                        } else {
                            sendMessage(sender, plugin.getLocale().getString("error.unknown"));
                        }
                    } else {
                        sendMessage(sender, plugin.getLocale().getString("error.invalid_number"));
                    }
                } else {
                    sendMessage(sender, plugin.getLocale().getString("error.invalid_number"));
                }
            }
        } else {
            sendMessage(sender, plugin.getLocale().getString("error.not_permission"));
        }

        return true;
    }

    private void reloadConfig(CommandSender sender) {
        if(sender.hasPermission("galactmoney.reload")) {
            plugin.config().reload();
            sendMessage(sender, plugin.getLocale().getString("plugin.reload"));
        } else sendMessage(sender, plugin.getLocale().getString("error.not_permission"));
    }

    private void setExchangeRate(CommandSender sender, String arg) {
        if(!(sender instanceof Player) || sender.hasPermission("galactmoney.setrate")) {
            if(arg.matches("^\\d+(\\.\\d{1,2})?$")) {
                double rate = Double.parseDouble(arg);
                Config.setExchangeRate(rate);
                plugin.config().save();
                sendMessage(sender, plugin.getLocale().getString("exchange.rate.set", format("%.2f", rate)));
            } else sendMessage(sender, plugin.getLocale().getString("error.invalid_number"));
        } else sendMessage(sender, plugin.getLocale().getString("error.not_permission"));
    }

    private void setCurrencySign(CommandSender sender, String arg) {
        if(!(sender instanceof Player) || sender.hasPermission("galactmoney.setsign")) {
            String sign = arg.trim();
            Config.setCurrencySign(sign);
            plugin.config().save();
            sendMessage(sender, plugin.getLocale().getString("currency.sign.set", format("%s", sign)));
        } else sendMessage(sender, plugin.getLocale().getString("error.not_permission"));
    }

    private void sendMessage(CommandSender sender, String msg)
    {
        sender.sendMessage(translateAlternateColorCodes('&', Config.getPrefix() + msg));
    }

    private void sendMessage(CommandSender sender, String[] msg)
    {
        for(int i=0; i<msg.length; i++) {
            msg[i] = translateAlternateColorCodes('&', Config.getPrefix() + msg[i]);
        }
        sender.sendMessage(msg);
    }
}
