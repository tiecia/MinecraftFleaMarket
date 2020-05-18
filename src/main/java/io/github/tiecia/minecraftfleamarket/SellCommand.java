package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.*;

/*
    Usage:
        /sell <amount> <price>
 */

public class SellCommand implements CommandExecutor {

    /**
     * The market that this command will sell to.
     */
    private final MarketManager market;

    /**
     * Creates a new sell command.
     *
     * @param market the market that this command will sell to.
     */
    public SellCommand(MarketManager market) {
        assert market != null;
        this.market = market;
    }

    /**
     * This method is triggered whenever the "/sell" command is called.
     *
     * @param commandSender The object that sent the message. Can be player or server.
     * @param command       The command that was send.
     * @param s             The string of the first word of the command. Should always be "/sell"
     * @param strings       The parameters passed in to the command. Each item represents parameter separated by whitespace in the command.
     * @return true if command syntax is correct; false if command syntax is incorrect. The server prints out the usage syntax from the plugin.yml file if false is returned.
     */
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 2) {  //Make sure there are enough parameters
                try {
                    //Get parameter contents
                    int amount = Integer.parseInt(strings[0]);
                    int price = Integer.parseInt(strings[1]);

                    //Get attributes of item to sell from player
                    PlayerInventory playerInventory = player.getInventory();
                    ItemStack holdingStack = playerInventory.getItemInMainHand();
                    Material itemType = holdingStack.getType();

                    //Collect items from inventory
                    ItemStack itemsToSell = fillStackFromInventory(new ItemStack(itemType, 0), playerInventory, amount);


                    if (itemsToSell.getAmount() < 1) { //Check to make sure items were found.
                        sendFailureMessage(player, "Item's cannot be found in inventory");
                        return true;
                    }

                    if (!market.sell(player, itemsToSell, price)) { //Check to make sure items were put to market
                        sendFailureMessage(player, "Unable put items to market");
                        return true;
                    }


                    if (itemsToSell.getAmount() < amount) { //Check to make sure all requested items were put to market
                        sendWarningMessage(player, "Only " + itemsToSell.getAmount() + " items were put to market " + "because not enough items were found in your inventory.");
                    } else { //Successfully put to market
                        sendSuccessMessage(player, itemsToSell.getAmount() + " items were successfully put to market.");
                    }

                } catch (NumberFormatException e) {
                    //Invalid command parameters, return usage.
                    Bukkit.getLogger().info("Invalid Parameters");
                    return false;
                }
            } else {
                //Not enough parameters, return usage
                return false;
            }
        } else {
            Bukkit.getLogger().info("Server cannot sell");
        }
        return true;
    }

    /**
     * Fills the given stack with "ammount" items where the item is the item in the main hand of the {@link PlayerInventory}
     *
     * @param stackToSell     the ItemStack to fill with items.
     * @param playerInventory the inventory to search.
     * @param amount          the number of items to take from the player and add to the ItemStack.
     * @return the filled {@link ItemStack}.
     */
    private ItemStack fillStackFromInventory(ItemStack stackToSell, PlayerInventory playerInventory, int amount) {
        for (int i = 0; i < playerInventory.getSize(); i++) {
            ItemStack itemInSlot = playerInventory.getItem(i);
            if (itemInSlot != null && itemInSlot.getType().equals(stackToSell.getType())) {
                while (stackToSell.getAmount() < amount && itemInSlot.getAmount() > 0) {
                    itemInSlot.setAmount(itemInSlot.getAmount() - 1);
                    stackToSell.setAmount(stackToSell.getAmount() + 1);
                }
            }
        }
        return stackToSell;
    }
}
