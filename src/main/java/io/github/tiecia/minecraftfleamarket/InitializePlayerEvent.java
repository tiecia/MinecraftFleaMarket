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

    /**
     * The market manager this is tied to
     */
    private final MarketManager marketManager;

    /**
     * Creates a new player event
     *
     * @param marketManager the MarketManager this event is tied to
     */
    public InitializePlayerEvent(MarketManager marketManager) {
        this.marketManager = marketManager;
    }

    /**
     * Called whenever a player joins the server. If the player is not in the Flea Market system it will add
     * them to the bank and set their balance to the starter balance.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!marketManager.getBank().keySet().contains(event.getPlayer().getUniqueId())) {
            marketManager.registerNewPlayer(event.getPlayer());
        }
        sendMessage(event.getPlayer(), "Current balance $" + marketManager.getBank().get(event.getPlayer().getUniqueId()));
    }
}