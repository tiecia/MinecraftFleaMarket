package io.github.tiecia.minecraftfleamarket;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.UUID;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.log;
import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendMessage;
import static org.bukkit.Bukkit.getLogger;

public class InitializePlayerEvent implements Listener {

    private final MarketManager marketManager;

    public InitializePlayerEvent(MarketManager marketManager) {
        this.marketManager = marketManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!marketManager.getBank().keySet().contains(event.getPlayer().getUniqueId())) {
            marketManager.registerNewPlayer(event.getPlayer());
        }
        sendMessage(event.getPlayer(), "Current balance $" + marketManager.getBank().get(event.getPlayer().getUniqueId()));
    }
}
