package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendMessage;


public class MarketCommand implements CommandExecutor {

    //market that the marketlist will check
    private final MarketManager market;

    public MarketCommand(MarketManager market) {
        assert market != null;
        this.market = market;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            if(args.length == 0)
                return false;
                
            Player player = (Player) commandSender;
            if(args[0].toLowerCase().equals("list")) {
                String searchTerm = "";
                if(args.length > 1) {
                    for (int i = 1; i<args.length; i++) {
                        searchTerm += args[i].toLowerCase() + " ";
                    }
                    searchTerm = searchTerm.strip();
                    sendMessage(player, "Search Result: ",true);
                } else {
                    sendMessage(player, "Current Market Listings: ",true);
                }
                for(Offer offer : market.offers(searchTerm)) {
                    sendMessage(player, offer.format(), false);
                }
                return true;
            } else if(args[0].toLowerCase().equals("balance")) {
                Integer balance = market.getBank().get(player.getUniqueId());
                sendMessage(player,"Current Balance: $" + ChatColor.DARK_GREEN + balance, true);
                return true;
            } else if(args[0].toLowerCase().equals("help")){
                TextComponent component = new TextComponent(ChatColor.AQUA + "Click here for help!");
                component.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/tiecia/MinecraftFleaMarket/blob/master-release/README.md#usage"));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Help Page").create()));
                sendMessage(player, component, true);
                return true;
            }
            return false;
        }
        else {
            //Server cannot search the marketlist
            Bukkit.getLogger().info("Server cannot see marketlist");
        return false; }
    }
}