package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class MinecraftFleaMarket extends JavaPlugin {

    /**
     * Appends the [FleaMarket] tag to a message. Makes all text after grey.
     */
    public static final String chatTag = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "FleaMarket"
            + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    /**
     * The market.
     */
    MarketManager marketManager;

    /**
     * Sends a general grey message to the player.
     *
     * @param player  Player to send message to
     * @param message The message to send.
     */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(chatTag + message);
    }

    /**
     * Sends a green message to the player.
     *
     * @param player  Player to send message to
     * @param message The message to send.
     */
    public static void sendSuccessMessage(Player player, String message) {
        sendMessage(player, ChatColor.GREEN + message);
    }

    /**
     * Sends a yellow message to the player.
     *
     * @param player  Player to send message to
     * @param message The message to send.
     */
    public static void sendWarningMessage(Player player, String message) {
        sendMessage(player, ChatColor.YELLOW + message);
    }

    /**
     * Sends a red message to the player.
     *
     * @param player  Player to send message to
     * @param message The message to send.
     */
    public static void sendFailureMessage(Player player, String message) {
        sendMessage(player, ChatColor.RED + message);
    }

    /**
     * Logs a message to the server console at an "info" level.
     *
     * @param message the string to be logged.
     */
    public static void log(String message) {
        Bukkit.getLogger().info("[MinecraftFleaMarket] " + message);
    }

    /**
     * Saves the market to the plugin files.
     *
     * Bank is saved in the format: "[PLAYER] [CURRENCY AMOUNT]"
     *
     * Market is saved in the format "[MARKET ID] [MERCHANT PLAYER] [UNIT PRICE] [ITEM]"
     *
     */
    public void save() {
        this.marketManager.save();
    }

    /**
     * Loads a saved MarketManager.
     */
    public void load() {
        this.marketManager = new MarketManager("plugins/MinecraftFleaMarket/bankSave.txt", "plugins/MinecraftFleaMarket/marketSave.txt");
    }



    /**
     * Called when Spigot enables this plugin.
     */
    @Override
    public void onEnable() {
        //Creates Plugin file if not already there. This is where settings and the market will be saved.
        File directory = new File("plugins/MinecraftFleaMarket");
        if (!directory.exists()) {
            directory.mkdir();
        }

        //Will load previously saved MarketManager.
        final File marketSave = new File("plugins/MinecraftFleaMarket/marketSave.txt");
        if ((marketSave.exists())) {
            try {
                load();
            } catch (Exception e) {
                log("Failed To Load Market");
                e.printStackTrace();
            }
        } else {
            //Creates fresh marketManager for the first time
            this.marketManager = new MarketManager();
        }

        //Register commands
        this.getCommand("buy").setExecutor(new BuyCommand(marketManager));
        this.getCommand("sell").setExecutor(new SellCommand(marketManager));
        this.getCommand("marketlist").setExecutor(new MarketCommand(marketManager));

        registerEvents();

        getLogger().info("Plugin Successfully Enabled");
    }

    /**
     * Registers any events this plugin will be receiving. Currently it only registers an event that
     * is triggered whenever a player logs on.
     */
    private void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new InitializePlayerEvent(this.marketManager), this);
    }

    /**
     * Called when Spigot disables this plugin.
     */
    @Override
    public void onDisable() {
        save();
        getLogger().info("Plugin Successfully Disabled");
    }
}

