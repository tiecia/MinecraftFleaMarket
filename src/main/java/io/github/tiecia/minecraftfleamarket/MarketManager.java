package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.util.*;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.log;
import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendFailureMessage;
import static org.bukkit.Bukkit.getLogger;

public class MarketManager {

    /**
     * A map that stores all player's bank balance. Player's are distinguished by their UUID which is unique to all players.
     */
    private Map<UUID, Integer> bank;

    /**
     * A map that stores all current offers on the market and relates them to their market ID.
     */
    private Map<Integer, Offer> market;

    private final int startAmount = 2000;

    /**
     * Creates a fresh new market.
     */
    public MarketManager() {
        //Will load from file later. Probably an XML file.
        bank = new HashMap<UUID, Integer>();

        //Add all players currently online to the bank
        for(Player p : Bukkit.getOnlinePlayers()){
            bank.put(p.getUniqueId(), startAmount);
        }

        market = new HashMap<Integer, Offer>();
    }

    public MarketManager(Map<UUID, Integer> inputBank, Map<Integer, Offer> inputMarket) {
        this.bank = inputBank;
        this.market = inputMarket;
    }

    /**
     * Registers a buy on an offer given the market ID.
     *
     * Before a buy can happen the offer must exist, the offer must have all the items requested, 
     * the buyer must have enough money, the buyer must have enough room in their inventory for the items. If any of these
     * conditions are not met the buy does not occur and this method returns false.
     *
     * @param player   player who is buying
     * @param marketID market item the player wants to buy
     * @param quantity The number of items to buy. Must be greater than 0
     * @return true if buy operation successful; false if buy operation failed.
     */
    public boolean buy(Player player, int marketID, int quantity){
        assert quantity > 0;
        Offer offerToBuy = market.get(marketID);
        if(offerToBuy == null){
            //Check to make sure offer exists
            sendFailureMessage(player, "Offer not found");
            return false;
        } else if(offerToBuy.getItemAmount() < quantity) {
            //Make sure quantity user inputted in not too big.
            sendFailureMessage(player, "Not enough items in offer");
            return false;
        }

        int totalCost = offerToBuy.getUnitPrice()*quantity;
        if(totalCost > bank.get(player.getUniqueId())){
            //Verify you have enough money
            sendFailureMessage(player, "You don't have enough money!");
            return false;
        }
        int newBuyerBalance = bank.get(player.getUniqueId()) - totalCost;
        bank.put(player.getUniqueId(), newBuyerBalance);
        if(!hasRoomInInventory(player, offerToBuy.getItem())) {
            //Make sure player has enough room in inventory
            sendFailureMessage(player, "Not enough room in inventory");
            return false;
        }

        //Give seller money
        UUID seller = offerToBuy.getMerchant();
        bank.put(seller, bank.get(seller) + totalCost);

        //Give to player
        addToInventory(player, offerToBuy.buy(quantity));

        if(offerToBuy.getItemAmount() == 0) {
            //Remove offer from market if empty
            market.remove(marketID);
        }
        return true;
    }

    /**
     * Ensures there is enough empty slots for items to be added.
     *
     * @param player The player to check inventory on.
     * @param items The items to check for room for.
     *
     * @return true if there is room; false if there is no room.
     * 
     * @// TODO: 5/23/2020 Check for stacking 
     */
    private boolean hasRoomInInventory(Player player, ItemStack items){
        PlayerInventory inventory = player.getInventory();
        int availableSpaces = 0;
        for (int i = 0; i < inventory.getSize() && i != -1; i++) {
            ItemStack currentSlot = inventory.getItem(i);
            if(currentSlot == null){
                availableSpaces += items.getMaxStackSize();
            } else if(currentSlot.getType().equals(items.getType())){
                availableSpaces += items.getMaxStackSize()-currentSlot.getAmount();
            }

            if(availableSpaces >= items.getAmount()){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the ItemStack to the inventory of the player given.
     *
     * @param player the player to give items. The player must have room in it's inventory to add items.
     *               This method will not check to make sure there is enough room.
     *               Bad things can happen if you try to add more items then there is room for.
     * @param items the items to give
     */
    public void addToInventory(Player player, ItemStack items){
        PlayerInventory inventory = player.getInventory();
        int stackSize = items.getType().getMaxStackSize();
        for (int i = 0; i < inventory.getSize() && items.getAmount() > 0; i++) {
            ItemStack currentSlot = inventory.getItem(i);
            if(currentSlot == null){
                currentSlot =  new ItemStack(items.getType(), 0);
                currentSlot.setItemMeta(items.getItemMeta());
            }
            if(currentSlot.getType().equals(items.getType())){
                while(currentSlot.getAmount() < stackSize && items.getAmount() != 0){
                    items.setAmount(items.getAmount() - 1);
                    currentSlot.setAmount(currentSlot.getAmount() + 1);
                }
            }
            inventory.setItem(i, currentSlot);
        }
    }

    /**
     * Registers a new market item
     *
     * @param player player who is selling the item
     * @param items  stack that contains the items to sell
     * @return true if successfully put the {@link ItemStack} to market; false if unsuccessful. Failure means an {@link Offer} linked to an existing market ID tried to be overridden.
     */
    public boolean sell(Player player, ItemStack items, int price) {
        Offer newOffer = new Offer(player.getUniqueId(), price, items);
        int id = makeID(newOffer);
        if (market.keySet().contains(id)) {
            //Check to make sure new offer does not override an old offer. In theory should never happen.
            return false;
        }
        market.put(id, newOffer);
        return true;
    }

    /**
     * Adds a new player to the market manager.
     *
     * @param player
     */
    public void registerNewPlayer(Player player){

        bank.put(player.getUniqueId(), startAmount);
        log(" " + player.getDisplayName() + " was added to the system");
    }

    /**
     * Gets a all the offers currently on the market.
     *
     * @return
     */
    public Collection<Offer> offers() {
        return market.values();
    }

    /**
     * Gets the offer with the corresponding id.
     *
     * @param id the id of the offer to return.
     * @return the offer the id correlates to; null if no offer correlates with the id.
     */
    public Offer getOffer(int id){
        return market.get(id);
    }

    /**
     * Gets all offers that are selling the given material.
     *
     * @param material the material to search for.
     * @return a min queue where the offer with the lowest unit price is the head.
     */
    public PriorityQueue<Offer> offers(Material material) {
        PriorityQueue<Offer> returnQueue = new PriorityQueue<Offer>();
        if(!market.isEmpty()) {
            for(Offer offer : this.offers()) {
                if(material.equals(offer.getItem().getType())) {
                    returnQueue.add(offer);
                }
            }
        }
        return returnQueue;
    }

    /**
     * @return the bank for this {@link MarketManager}
     */
    public Map<UUID, Integer> getBank() {
        return this.bank;
    }

    public Map<Integer, Offer> getMarket() {
        return this.market;
    }

    /**
     * Helper method to make the offer ID when a new offer is created. We need to change this to me more user friendly.
     *
     * @param offer
     * @return
     * @// TODO: 5/18/2020   Make more user friendly
     */
    private int makeID(Offer offer) {
        int id = offer.hashCode();
        offer.setId(id);
        return id;
    }
}
