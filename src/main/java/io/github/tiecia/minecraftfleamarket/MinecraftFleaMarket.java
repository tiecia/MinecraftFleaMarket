package io.github.tiecia.minecraftfleamarket;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MinecraftFleaMarket extends JavaPlugin {

    private Map<UUID, Integer> bank;
    public static final String chatTag = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "FleaMarket"
            + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;


    @Override
    public void onEnable() {
        //Will load from file later. Probably an XML file.
        bank = new HashMap<UUID, Integer>();

        //Register commands
        this.getCommand("buy").setExecutor(new BuyCommand(this));

        getLogger().info("Plugin Successfully Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Successfully Enabled");
    }
}
