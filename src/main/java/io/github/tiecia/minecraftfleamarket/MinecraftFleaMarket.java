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
        try {
            Map<UUID, Integer> bank = this.marketManager.getBank();
            Map<Integer, Offer> market = this.marketManager.getMarket();

            File saveBank = new File("plugins/MinecraftFleaMarket/bankSave.txt");
            File saveMarket = new File("plugins/MinecraftFleaMarket/marketSave.txt");

            PrintStream bankStream = new PrintStream(saveBank);
            for (UUID playerID : bank.keySet()) {
                bankStream.println(playerID.toString() + " " + bank.get(playerID));
            }
            bankStream.close();

            PrintStream marketStream = new PrintStream(saveMarket);
            Offer current;
            for (Integer offerID : market.keySet()) {
                current = market.get(offerID);
                marketStream.println(offerID + " " + current.getMerchant() + " " + current.getUnitPrice() + "\n" + itemStackToString(current.getItem()) + "[!]");
            }
            marketStream.close();

        } catch (Exception e) {
            getLogger().info("Failed in saving.");
        }
    }

    /**
     * Loads a saved MarketManager.
     */
    public void load() {
        Map<UUID, Integer> loadedBank = new HashMap<UUID, Integer>();
        Map<Integer, Offer> loadedMarket = new HashMap<Integer, Offer>();

        try {
            Scanner scanBank = new Scanner(new File("plugins/MinecraftFleaMarket/bankSave.txt"));
            while (scanBank.hasNextLine()) {
                Scanner parseLine = new Scanner(scanBank.nextLine());
                String uuid = parseLine.next();
                UUID inputUUID = UUID.fromString(uuid);
                int balance = parseLine.nextInt();
                loadedBank.put(inputUUID, balance);
                parseLine.close();
            }
            scanBank.close();
            getLogger().info("bank loaded");
        } catch (Exception e) {

        }

        try {
            Scanner scanMarket = new Scanner(new File("plugins/MinecraftFleaMarket/marketSave.txt"));
            while (scanMarket.hasNextLine()) {
                Scanner scanLine = new Scanner(scanMarket.nextLine());
                int offerID = Integer.parseInt(scanLine.next());
                UUID playerUUID = UUID.fromString(scanLine.next());
                int unitPrice = Integer.parseInt(scanLine.next());
                scanLine.close();

                String itemStack = scanMarket.nextLine();
                String input = scanMarket.nextLine();
                while(!input.equals("[!]")) {
                    itemStack = itemStack + "\n" + input;
                    input = scanMarket.nextLine();
                }

                ItemStack trueItemStack = stringToItemStack(itemStack);
                Offer inputOffer = new Offer(playerUUID, unitPrice, trueItemStack);
                /*getLogger().info("raw uuid: " + playerUUID);
                getLogger().info("playerUUID: " + Bukkit.getPlayer(playerUUID));
                getLogger().info("OfflinePlayerUUID " + Bukkit.getOfflinePlayer(playerUUID).getPlayer());
                getLogger().info("unitPrice: " + unitPrice);
                getLogger().info("trueItemStack: " + trueItemStack.toString());
                getLogger().info(inputOffer.print()); */
                loadedMarket.put(offerID, inputOffer);
            }

            scanMarket.close();
            getLogger().info("market Loaded");

            this.marketManager = new MarketManager(loadedBank, loadedMarket);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method heavily based on Hellgast23's solution for getting an ItemStack to String. From: https://www.spigotmc.org/threads/serializing-itemstack-to-string.80233/?__cf_chl_jschl_tk__=936b757011f08dd92347f848029cc26a01264780-1590526252-0-AZGgc91rW5BpfVUuFppoJ-wxhB-Zl2w1pmTfdC4CKYJnlfcY_c3XRvWLA5VPvUpJ9HQCU96PL5A0z6_NKplKzimtgnT5wgLZZwKv07I878NQ3ADtFRJEAv2EzJFmI4PxqcRt6KJC4iKaaEJPdAiuDvJFGg9nhB2iyAc0XygneYXx5T4Ee6f9u7w8UC1W8-pRTUnmwmQFsuNlwkqM3bSXfzCt5ZQg-O1vDsETnrMDe3r79g63HKOf0JsrbKx6vt_ddQ3g0K1jQO-SzXvrdecPJCqm1eJFMLqm0sIfF0mdLAoz3npv13u2v6yTbs0nn2cx-mieaRdoGGMAiFFhiEOY1Eg
     * Converts an ItemStack to a YamlConfig String with all associated data.
     * @param item
     * @return A String representation of the ItemStack.
     */
    public String itemStackToString(ItemStack item) {
        YamlConfiguration currentItem = new YamlConfiguration();
        currentItem.set("item", item);
        return currentItem.saveToString();
    }

    /**
     * Decode method for the above ItemStack to String conversion. Again heavily based on the same solution as above.
     * @param inputString - The String to be converted to an ItemStack
     * @return the saved ItemStack
     */
    public ItemStack stringToItemStack(String inputString) {
        YamlConfiguration currentItem = new YamlConfiguration();
            try {
                currentItem.loadFromString(inputString);
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        return currentItem.getItemStack("item", null);
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

        //Will load previously saved marketmanager.
        final File marketSave = new File("plugins/MinecraftFleaMarket/marketSave.txt");
        if ((marketSave.exists())) {
            try {
                load();
                getLogger().info("Succeeded in loading.");
            } catch (Exception e) {
                getLogger().info("Failed in loading.");
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

