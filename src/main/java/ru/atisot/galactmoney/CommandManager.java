package ru.atisot.galactmoney;

import net.milkbowl.vault.economy.EconomyResponse;
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
        if(!(sender instanceof Player)) {
            plugin.logger().info("[" + plugin.getDescription().getName() + "]: " + "Only players can execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if(command.getLabel().equals("galact") || command.getLabel().equals("gal")) {

            String cmd = (args.length > 0) ? args[0] : "help";
            cmd = (cmd.equalsIgnoreCase("bal")) ? "balance" : cmd;
            cmd = (cmd.equalsIgnoreCase("exc")) ? "exchange" : cmd;

            String arg = (args.length > 1) ? args[1] : "";

            switch (cmd) {
                case "balance":
                    viewBalance(player, arg);
                    break;
                case "exchange":
                    exchangeCurrency(player, arg);
                    break;
                case "course":
                    viewCourse(player);
                    break;
                default:
                    helpCommand(player);
            }

            return true;
        } else {
            return false;
        }
    }

    private void helpCommand(Player player) {

        String[] help = {
                plugin.getLocale().getString("help.balance"),
                plugin.getLocale().getString("help.course"),
                plugin.getLocale().getString("help.exchange")
        };

        sendMessage(player, help);
    }

    private void viewBalance(Player player, String arg) {
        if(arg.isEmpty()) {
            sendMessage(player, plugin.getLocale().getString("balance.you", format("%.2f", DataBase.getBalance(player.getName())), Config.getCurrencySign()));
        } else if(arg.equalsIgnoreCase(player.getName())) {
            sendMessage(player, plugin.getLocale().getString("balance.you", format("%.2f", DataBase.getBalance(player.getName())), Config.getCurrencySign()));
        } else if(player.hasPermission("galactmoney.balance.other")) {
            Player player2 = plugin.getServer().getPlayer(arg);
            if(!player2.isEmpty()) {
                sendMessage(player, plugin.getLocale().getString("balance.other", player2.getDisplayName(), format("%.2f", DataBase.getBalance(player.getName())), Config.getCurrencySign()));
            } else {
                sendMessage(player, plugin.getLocale().getString("player.not_found", arg));
            }
        } else {
            sendMessage(player, plugin.getLocale().getString("error.not_permission"));
        }
    }

    private void viewCourse(Player player) {
        sendMessage(player, plugin.getLocale().getString("course", Config.getCurrencySign(), format("%d", Config.getCurrencyCourse())));
    }

    private void exchangeCurrency(Player player, String arg) {
        if(player.hasPermission("galactmoney.exchange")) {
            if(arg.isEmpty()) {
                sendMessage(player, plugin.getLocale().getString("exchange.get_amount"));
            } else {
                if(arg.matches("^\\d{1,3}(\\.\\d{1,2})?$")) {
                    double amount = Double.parseDouble(arg);
                    double balance = DataBase.getBalance(player.getName());
                    amount = Math.min(balance, amount);

                    if(amount > 0) {
                        double excAmount = amount * Config.getCurrencyCourse();

                        if(DataBase.reduceBalance(player.getName(), amount)) {
                            EconomyResponse r = plugin.getEconomy().depositPlayer(player, excAmount);
                            if(r.transactionSuccess()) {
                                sendMessage(player, plugin.getLocale().getString("exchange.done", Config.getCurrencySign() + amount, plugin.getEconomy().format(r.balance)));
                                plugin.logger().info("[" + plugin.getDescription().getName() + "]: " + format("Player %s exchanged %s%.2f.", player.getDisplayName(), Config.getCurrencySign(), amount));
                            } else {
                                sendMessage(player, plugin.getLocale().getString("error.exchange", r.errorMessage));
                                plugin.logger().info("[" + plugin.getDescription().getName() + "]: " + format("Аn error occurred while trying to exchange a player %s: %s", player.getDisplayName(), r.errorMessage));
                            }
                        } else {
                            sendMessage(player, plugin.getLocale().getString("error.unknown"));
                        }
                    } else {
                        sendMessage(player, plugin.getLocale().getString("error.invalid_number"));
                    }
                } else {
                    sendMessage(player, plugin.getLocale().getString("error.invalid_number"));
                }
            }
        } else {
            sendMessage(player, plugin.getLocale().getString("error.not_permission"));
        }
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
