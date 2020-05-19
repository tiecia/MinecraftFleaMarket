package io.github.tiecia.minecraftfleamarket;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.UUID;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.log;
import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendMessage;

public class InitializePlayerEvent implements Listener {

    private final MarketManager marketManager;

    public InitializePlayerEvent(MarketManager marketManager) {
        this.marketManager = marketManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Map<UUID, Integer> bank = marketManager.getBank();
        if (!bank.keySet().contains(event.getPlayer().getUniqueId())) {
            log(event.getPlayer().getDisplayName() + " was added to the system");
            bank.put(event.getPlayer().getUniqueId(), 2000);
        }
        sendMessage(event.getPlayer(), "Current balance $" + bank.get(event.getPlayer().getUniqueId()));
    }
}
