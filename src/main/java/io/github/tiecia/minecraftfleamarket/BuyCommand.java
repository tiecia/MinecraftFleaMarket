package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendMessage;
import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendSuccessMessage;

/*
    Usage:
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

            if(strings.length != 2) {
                return false;
            } else {
                //Attempt to parse a market ID
                try {
                    int quantity = Integer.parseInt(strings[0]);
                    int marketID = Integer.parseInt(strings[1]);
                    Offer offerToBuy = market.getOffer(marketID);
                    if(market.buy(player, marketID, quantity)){
                        sendSuccessMessage(player, "Successfully Bought " + quantity + " " + offerToBuy.getItem().getItemMeta().getDisplayName());
                    }
                } catch (NumberFormatException e) {
                    //Invalid parameters, respond with correct usage
                    return false;
                }
            }



        } else {
            Bukkit.getLogger().info("Server cannot buy");
        }
        return true;
    }
}
