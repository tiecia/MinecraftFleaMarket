package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;

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
     * @param message The message to send
     * @param tag When true the plugin tag, "[FleaMarket]" is appended to the beginning of the message.
     */
    public static void sendMessage(Player player, String message, Boolean tag) {
        if(tag) {
            player.sendMessage(chatTag + message);
        } else {
            player.sendMessage(message);
        }
    }
    
    /**
     * Sends a general grey message to the player.
     *
     * @param player  Player to send message to
     * @param message The message to send
     * @param tag When true the plugin tag, "[FleaMarket]" is appended to the beginning of the message.
     */
    public static void sendMessage(Player player, TextComponent message, Boolean tag) {
        if(tag) {
            message.setText(chatTag + message.getText());
        }
        player.spigot().sendMessage(message);
    }
    /**
     * Sends a green message to the player.
     *
     * @param player  Player to send message to
     * @param message The message to send.
     */
    public static void sendSuccessMessage(Player player, String message) {
        sendMessage(player, ChatColor.GREEN + message,true);
    }

    /**
     * Sends a yellow message to the player.
     *
     * @param player  Player to send message to
     * @param message The message to send.
     */
    public static void sendWarningMessage(Player player, String message) {
        sendMessage(player, ChatColor.YELLOW + message,true);
    }

    /**
     * Sends a red message to the player.
     *
     * @param player  Player to send message to
     * @param message The message to send.
     */
    public static void sendFailureMessage(Player player, String message) {
        sendMessage(player, ChatColor.RED + message,true);
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
                this.marketManager = new MarketManager("plugins/MinecraftFleaMarket/bankSave.txt", "plugins/MinecraftFleaMarket/marketSave.txt");
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
        this.getCommand("market").setExecutor(new MarketCommand(marketManager));

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
        this.marketManager.save();
        getLogger().info("Plugin Successfully Disabled");
    }
}

