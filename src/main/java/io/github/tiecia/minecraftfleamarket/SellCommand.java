package io.github.tiecia.minecraftfleamarket;

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

                    if (holdingStack.getAmount() < 1) {
                        //Not holding an item
                        sendFailureMessage(player, "Please hold the item you want to sell");
                        return true;
                    } else if (quantityOfItems(playerInventory, itemType) < amount) {
                        //Player is asking to sell more than they have
                        sendFailureMessage(player, "Not enough items in inventory");
                        return true;
                    } else if (amount < 1) {
                        //Quantity parameter is too small
                        sendFailureMessage(player, "Quantity to low");
                        return true;
                    } else if (price < 1) {
                        //Price parameter is too small
                        sendFailureMessage(player, "Price to low");
                        return true;
                    }

                    //Collect items from inventory
                    ItemStack newOfferItems = new ItemStack(itemType, 0);
                    newOfferItems.setItemMeta(holdingStack.getItemMeta());
                    ItemStack itemsToSell = fillStackFromInventory(newOfferItems, playerInventory, amount);

                    if (!market.sell(player, itemsToSell, price)) {
                        //Check to make sure items were put to market. In theory should never fail.
                        sendFailureMessage(player, "Unable to put items to market");
                        //If sell fails give player items back
                        market.addToInventory(player, itemsToSell);
                        return true;
                    }

                    sendSuccessMessage(player, itemsToSell.getAmount() + " items put to market");


                } catch (NumberFormatException e) {
                    //Invalid command parameters, return usage.
                    return false;
                }
            } else {
                //Not enough parameters, return usage
                return false;
            }
        } else {
            log("Server cannot sell");
        }
        return true;
    }

    /**
     * Finds the total number of items in a players inventory.
     *
     * @param playerInventory The inventory to search
     * @param material The material to search for
     * @return The number of items with the corresponding {@link Material} in the player's inventory.
     */
    private int quantityOfItems(PlayerInventory playerInventory, Material material) {
        int quantity = 0;
        for (int i = 0; i < playerInventory.getSize(); i++) {
            ItemStack currentSlot = playerInventory.getItem(i);
            if (currentSlot != null && currentSlot.getType().equals(material)) {
                quantity += currentSlot.getAmount();
            }
        }
        return quantity;
    }

    /**
     * Fills the given stack with "amount" items where the item is the item in the main hand of the {@link PlayerInventory}
     *
     * @param stackToSell     the ItemStack to fill with items.
     * @param playerInventory the inventory to search.
     * @param amount          the number of items to take from the player and add to the ItemStack.
     * @return the filled {@link ItemStack}.
     */
    private ItemStack fillStackFromInventory(ItemStack stackToSell, PlayerInventory playerInventory, int amount) {
        for (int i = 0; i < playerInventory.getSize(); i++) {
            ItemStack itemInSlot = playerInventory.getItem(i);
            /* Note: the not in the last 2 parts of this if statement make no sense. I want the statement to be true if the two equal.
            But for some reason that statement produces false if they are equal and true if they are different so the not has to be there
            to fix this. The compareTo method in ItemMeta and ItemStack might be backwards. */
            if (itemInSlot != null && itemInSlot.getType().equals(stackToSell.getType()) && itemInSlot.getItemMeta().equals(stackToSell.getItemMeta())) {
                while (stackToSell.getAmount() < amount && itemInSlot.getAmount() > 0) {
                    itemInSlot.setAmount(itemInSlot.getAmount() - 1);
                    stackToSell.setAmount(stackToSell.getAmount() + 1);
                }
            }
        }
        return stackToSell;
    }
}
