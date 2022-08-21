package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendSuccessMessage;

/*
    Usage:
        /buy <quantity> <market ID>
            Buy quantity of market ID
 */

public class BuyCommand implements CommandExecutor {

    /**
     * The MarketManager this command buys from.
     */
    private final MarketManager market;

    /**
     * Creates a new command with a corresponding MarketManager
     *
     * @param market the MarketManager this command buys from
     */
    public BuyCommand(MarketManager market) {
        assert market != null;
        this.market = market;
    }

    /**
     * Triggered whenever this command is executed.
     *
     * @param commandSender The entity who sent the command.
     * @param command The command sent
     * @param s The command sent in string form
     * @param strings The parameters after the root command. Each index is one parameter separated by whitespace.
     *
     * @return true if the syntax was correct; false if syntax was incorrect. false displays the correct syntax to the user.
     */
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
                        sendSuccessMessage(player, ChatColor.GRAY + "Successfully bought " + ChatColor.YELLOW + quantity + " "  + ChatColor.GREEN + offerToBuy.getDisplayName());
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
