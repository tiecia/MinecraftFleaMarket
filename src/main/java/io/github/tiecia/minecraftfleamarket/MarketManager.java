package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class MarketManager {

    /**
     * A map that stores all player's bank balance. Player's are distinguished by their UUID which is unique to all players.
     */
    private Map<UUID, Integer> bank;

    /**
     * A map that stores all current offers on the market and relates them to their market ID.
     */
    private Map<Integer, Offer> market;

    /**
     * Creates a fresh new market.
     */
    public MarketManager() {
        //Will load from file later. Probably an XML file.
        bank = new HashMap<UUID, Integer>();
        market = new HashMap<Integer, Offer>();
    }

    /**
     * Opens a market from a save file.
     *
     * @param saveFile the file that contains a valid MarketManager object.
     * @// TODO: 5/18/2020 Implement save and restore
     */
    public MarketManager(File saveFile) {

    }

    /**
     * Registers a buy on an offer given the market ID.
     *
     * @param player   player who is buying
     * @param marketID market item the player wants to buy
     * @return true if buy operation successful; false if buy operation failed.
     * @// TODO: 5/18/2020 Implement buy
     */
    public boolean buy(Player player, int marketID){
        return false;
    }

    /**
     * Registers a new market item
     *
     * @param player player who is selling the item
     * @param items  stack that contains the items to sell
     * @return true if successfully put the {@link ItemStack} to market; false if unsuccessful. Failure means an {@link Offer} linked to an existing market ID tried to be overridden.
     */
    public boolean sell(Player player, ItemStack items, int price) {
        Offer newOffer = new Offer(player, price, items);
        int id = makeID(newOffer);
        if (market.keySet().contains(id)) { //If new offer will override an old one. In theory should never happen.
            return false;
        }
        market.put(id, newOffer);
        return true;
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
     * Gets all offers that are selling the given material.
     *
     * @param material the material to search for.
     * @return a min queue where the lowest offer comes out first.
     * @// TODO: 5/18/2020  Make a searching algorithm
     */
    public PriorityQueue<Offer> offers(Material material) {
        return null;
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
