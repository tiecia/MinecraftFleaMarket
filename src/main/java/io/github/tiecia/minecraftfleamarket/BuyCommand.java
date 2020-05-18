package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendMessage;

/*
    Usage:
        /buy list
            Lists all available purchases
        /buy list <item>
            Lists all available purchases of that item
        /buy <quantity> <market ID>
            Buy quantity of market ID
 */

public class BuyCommand implements CommandExecutor {

    //Used to access the items in the market
    private final MarketManager market;

    public BuyCommand(MarketManager market) {
        assert market != null;
        this.market = market;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (strings.length > 0) {
                if (strings[0].equals("list")) {
                    if (strings.length < 1 && strings[1] != null) {
                        //List all available items of that type
                        player.sendMessage(MinecraftFleaMarket.chatTag + "List Items of" + strings[1]);
                    }

                    sendMessage(player, "All Items for Sale:");
                    //List all available items
                    for (Offer offer : market.offers()) {
                        sendMessage(player, "\t" + offer.print());
                    }
                } else {
                    //Attempt to parse a market ID
                    try {
                        int quantity = Integer.parseInt(strings[0]);
                        int marketID = Integer.parseInt(strings[1]);
                        player.sendMessage(MinecraftFleaMarket.chatTag + "Successfully Bought" + quantity + "of" + marketID);
                    } catch (NumberFormatException e) {
                        //Invalid ID Number, respond with correct usage
                        return false;
                    }
                }
            } else {
                //No parameters, respond with correct usage
                return false;
            }

        } else {
            Bukkit.getLogger().info("Server cannot buy");
        }
        return true;
    }
}
