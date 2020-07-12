package ru.atisot.galactmoney.locale;

import org.bukkit.ChatColor;
import ru.atisot.galactmoney.GalactMoney;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Locale {

    private final HashMap<String, MessageFormat> messageCache = new HashMap<>();
    private final GalactMoney plugin;

    private ResourceBundle lang;
    private java.util.Locale locale;

    public Locale(GalactMoney plugin) {
        this.plugin = plugin;
        locale = java.util.Locale.getDefault();
    }

    /**
     * Инициализация класса. Должно вызываться в первую очередь.
     * В противном случае вы будете получать Key "key" does not exists!
     */
    public void init(String language) {

        locale = java.util.Locale.forLanguageTag(language);
        //this.lang = ResourceBundle.getBundle("lang", locale);
        this.lang = ResourceBundle.getBundle("lang", new UTF8Control());
    }

    /**
     * Получение сообщения из конфигурации
     * Пример сообщения: "There is so many players."
     * Пример вызова: getString("key");
     *
     * @param key ключ сообщения
     * @return сообщение, иначе null
     */
    public String getString(final String key) {
        return this.getString(key, false, "");
    }

    /**
     * Получение сообщения с аргументами из конфигурации
     * Пример сообщения: "There is {0} players: {1}."
     * Пример вызова: getString("key", "2", "You, Me");
     *
     * @param key ключ сообщения
     * @param args аргументы сообщения
     * @return сообщение, иначе null
     */
    public String getString(final String key, final String... args) {
        return this.getString(key, false, args);
    }

    /**
     * Получение сообщения из конфигурации с возможностью фильтрации цвета
     * Пример сообщения: "\u00a76There is so many players."
     * Пример вызова: getString("key", false);
     *
     * @param key ключ сообщения
     * @param removeColors если true, то цвета будут убраны
     * @return сообщение, иначе null
     */
    public String getString(final String key, final boolean removeColors) {
        return this.getString(key, removeColors, "");
    }

    /**
     * Получение сообщения с аргументами из конфигурации с возможностью фильтрации цвета
     * Пример сообщения: "\u00a76There is \u00a7c{0} \u00a76players:\u00a7c {1}."
     * Пример вызова: getString("key", false, "2", "You, Me");
     *
     * @param key ключ сообщения
     * @param removeColors если true, то цвета будут убраны
     * @param args аргументы сообщения
     * @return сообщение, иначе null
     */
    public String getString(final String key, final boolean removeColors, final String... args) {
        String out = this.lang.getString(key);
        if (out == null) {
            return ChatColor.RED + "Key \"" + key + "\" not found!";
        }

        MessageFormat mf = this.messageCache.get(out);
        if (mf == null) {
            mf = new MessageFormat(out);
            this.messageCache.put(out, mf);
        }

        out = mf.format(args);

        if (removeColors) {
            out = ChatColor.stripColor(out);
        }

        return out;
    }
}
