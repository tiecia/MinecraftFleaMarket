package io.github.tiecia.minecraftfleamarket;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MarketManager {

    private Map<UUID, Integer> bank;

    //A hash
    private MarketItem[] market;

    public MarketManager(){
        //Will load from file later. Probably an XML file.
        bank = new HashMap<UUID, Integer>();

    }

    public MarketManager(File saveFile){

    }

    /**
     *
     * @param player player who is buying
     * @param marketID market item the player wants to buy
     * @return true if buy operation successful; false if buy operation failed.
     */
    public boolean buy(Player player, int marketID){

    }

    /**
     * Registers a new market item
     *
     * @param player player who is selling the item
     * @param items stack that contains the items to sell
     */
    public void sell(Player player, ItemStack items){

    }
}
