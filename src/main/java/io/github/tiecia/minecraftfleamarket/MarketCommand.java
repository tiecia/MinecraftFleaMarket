package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendMessage;

public class MarketCommand implements CommandExecutor {

    //market that the marketlist will check
    private final MarketManager market;

    public MarketCommand(MarketManager market) {
        assert market != null;
        this.market = market;
    }



    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(strings[0].toLowerCase().equals("list")) {
                if (strings.length > 1) {
                    String searchType = ""; //searchType is called such because it will be used to search the market
                    for (int i = 0; i < strings.length; i++) { //Item types are formatted in uppercase with upperscores
                        if (i != 0)                          //This loop formats the player input thusly
                            searchType += "_";
                        searchType += strings[i];
                    }
                    searchType = searchType.toLowerCase();
                    boolean searchSuccess = false;
                    sendMessage(player, "Current Market Listings: ",true);
                    for (Offer offer : market.offers()) {
                        //   sendMessage(player, "\t" + offer.getItem().getType().toString());
                        //   sendMessage(player, "\t" + searchType);
                        if (searchType.equals(offer.getItem().getType().toString().toLowerCase())) {
                            searchSuccess = true;
                            if (!offer.getMerchant().equals(player))
                                sendMessage(player, offer.print(), false);
                            else
                                sendMessage(player, offer.print() + "(Self)", false); //Useful for not buying your own items
                        }
                    }
                    if (searchSuccess = false) {
                        sendMessage(player, "No items found for" + searchType,true);
                    }
                } else{
                    //No arguments
                    sendMessage(player, "Current Market Listings: ",true);
                    for(Offer offer: market.offers()) {
                        String itemType = "";
                        itemType = offer.getItem().getType().toString().replaceAll("_", " ");
                        if(!offer.getMerchant().equals(player))
                            sendMessage(player, offer.print(), false);
                        else
                            sendMessage(player, offer.print()+"(Self)", false); //Useful for not buying your own items
                    }
                    return true;
                }
            } else if(strings[0].toLowerCase().equals("balance")) {
                Integer balance = market.getBank().get(player.getUniqueId());
                sendMessage(player,"Current Balance: $" + ChatColor.DARK_GREEN + balance, true);
                return true;
            } else if(strings[0].toLowerCase().equals("help")){
                sendMessage(player,"your just being dumb", true);
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