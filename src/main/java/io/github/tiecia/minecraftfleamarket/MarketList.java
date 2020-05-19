package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendMessage;

public class MarketList implements CommandExecutor {

    private final MarketManager market;

    public MarketList(MarketManager market) {
        assert market != null;
        this.market = market;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (strings.length > 0) {
                strings[0] = strings[0].toLowerCase();
                for (Offer offer : market.offers()) {
                    if (strings[0].equals(offer.getItem().getItemMeta().getLocalizedName())) {
                        sendMessage(player, "\t" + offer.print());
                    }
                }
            } else {
                return false;
            }
            return true;
        }
        else {
        return false; }
    }
}