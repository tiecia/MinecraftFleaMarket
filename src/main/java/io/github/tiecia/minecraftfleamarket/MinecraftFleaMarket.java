package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
     * Called when Spigot enables this plugin.
     */
    @Override
    public void onEnable() {
        //Will load from file later. Probably an XML file.
        this.marketManager = new MarketManager();
        //Register commands
        this.getCommand("buy").setExecutor(new BuyCommand(marketManager));
        this.getCommand("sell").setExecutor(new SellCommand(marketManager));
        this.getCommand("marketlist").setExecutor(new MarketCommand(marketManager));

        getLogger().info("Plugin Successfully Enabled");
    }

    /**
     * Registers any events this plugin will be receiving. Currently it only registers an event that
     * is triggered whenever a player logs on.
     */
    private void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new JoinEvent(), this);
    }

    /**
     * Called when Spigot disables this plugin.
     */
    @Override
    public void onDisable() {
        getLogger().info("Plugin Successfully Enabled");
    }
}
