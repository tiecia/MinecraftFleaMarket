package io.github.tiecia.minecraftfleamarket;

import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftFleaMarket extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}
