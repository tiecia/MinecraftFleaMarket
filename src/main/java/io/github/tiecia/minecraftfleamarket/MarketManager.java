package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.log;
import static io.github.tiecia.minecraftfleamarket.MinecraftFleaMarket.sendFailureMessage;

public class MarketManager {

    /**
     * A map that stores all player's bank balance. Player's are distinguished by their UUID which is unique to all players.
     */
    private final Map<UUID, Integer> bank;

    /**
     * A map that stores all current offers on the market and relates them to their market ID.
     */
    private final Map<Integer, Offer> market;

    private final int startAmount = 2000;

    private String bankPath;

    private String marketPath;

    /**
     * Creates a fresh new market.
     */
    public MarketManager() {
        //Will load from file later. Probably an XML file.
        bank = new HashMap<UUID, Integer>();

        //Add all players currently online to the bank
        for (Player p : Bukkit.getOnlinePlayers()) {
            bank.put(p.getUniqueId(), startAmount);
        }

        market = new HashMap<Integer, Offer>();

        this.bankPath = "plugins/MinecraftFleaMarket/bankSave.txt";
        this.marketPath = "plugins/MinecraftFleaMarket/marketSave.txt";
    }

    /**
     * Loads the marketManager from the specified filePath
     *
     * @param bankPath the path to the saved bank
     * @param marketPath the path to the saved market
     */
    public MarketManager(String bankPath, String marketPath) {
        this.bankPath = bankPath;
        this.marketPath = marketPath;
        Map<UUID, Integer> loadedBank = new HashMap<UUID, Integer>();
        Map<Integer, Offer> loadedMarket = new HashMap<Integer, Offer>();

        try {
            Scanner scanBank = new Scanner(new File(bankPath));
            while (scanBank.hasNextLine()) {
                Scanner parseLine = new Scanner(scanBank.nextLine());
                String uuid = parseLine.next();
                UUID inputUUID = UUID.fromString(uuid);
                int balance = parseLine.nextInt();
                loadedBank.put(inputUUID, balance);
                parseLine.close();
            }
            scanBank.close();
        } catch (Exception e) {

        }

        try {
            Scanner scanMarket = new Scanner(new File(marketPath));
            while (scanMarket.hasNextLine()) {
                Scanner scanLine = new Scanner(scanMarket.nextLine());
                int offerID = Integer.parseInt(scanLine.next());
                UUID playerUUID = UUID.fromString(scanLine.next());
                int unitPrice = Integer.parseInt(scanLine.next());
                scanLine.close();

                String itemStack = scanMarket.nextLine();
                String input = scanMarket.nextLine();
                while(!input.equals("[!]")) {
                    itemStack = itemStack + "\n" + input;
                    input = scanMarket.nextLine();
                }

                ItemStack trueItemStack = stringToItemStack(itemStack);
                Offer inputOffer = new Offer(playerUUID, unitPrice, trueItemStack);
                inputOffer.setId(offerID);
                loadedMarket.put(offerID, inputOffer);
            }

            scanMarket.close();
            log("Market Successfully Loaded");


        } catch (FileNotFoundException e) {
            log("Saved files cannot be found!");
        }
        this.bank = loadedBank;
        this.market = loadedMarket;
    }

    /**
     * Saves the marketManager to a file designated in the config file.
     */
    public void save() {
        try {
            File saveBank = new File(this.bankPath);
            File saveMarket = new File(this.marketPath);

            PrintStream bankStream = new PrintStream(saveBank);
            for (UUID playerID : bank.keySet()) {
                bankStream.println(playerID.toString() + " " + bank.get(playerID));
            }
            bankStream.close();

            PrintStream marketStream = new PrintStream(saveMarket);
            Offer current;
            for (Integer offerID : market.keySet()) {
                current = market.get(offerID);
                marketStream.println(offerID + " " + current.getMerchant() + " " + current.getUnitPrice() + "\n" + itemStackToString(current.getItem()) + "[!]");
            }
            marketStream.close();
            log("Saving successful");
        } catch (Exception e) {
            log("Saving failed!");
        }
    }

    /**
     * Method heavily based on Hellgast23's solution for getting an ItemStack to String. From: https://www.spigotmc.org/threads/serializing-itemstack-to-string.80233/?__cf_chl_jschl_tk__=936b757011f08dd92347f848029cc26a01264780-1590526252-0-AZGgc91rW5BpfVUuFppoJ-wxhB-Zl2w1pmTfdC4CKYJnlfcY_c3XRvWLA5VPvUpJ9HQCU96PL5A0z6_NKplKzimtgnT5wgLZZwKv07I878NQ3ADtFRJEAv2EzJFmI4PxqcRt6KJC4iKaaEJPdAiuDvJFGg9nhB2iyAc0XygneYXx5T4Ee6f9u7w8UC1W8-pRTUnmwmQFsuNlwkqM3bSXfzCt5ZQg-O1vDsETnrMDe3r79g63HKOf0JsrbKx6vt_ddQ3g0K1jQO-SzXvrdecPJCqm1eJFMLqm0sIfF0mdLAoz3npv13u2v6yTbs0nn2cx-mieaRdoGGMAiFFhiEOY1Eg
     * Converts an ItemStack to a YamlConfig String with all associated data.
     * @param item
     * @return A String representation of the ItemStack.
     */
    private String itemStackToString(ItemStack item) {
        YamlConfiguration currentItem = new YamlConfiguration();
        currentItem.set("item", item);
        return currentItem.saveToString();
    }

    /**
     * Decode method for the above ItemStack to String conversion. Again heavily based on the same solution as above.
     * @param inputString - The String to be converted to an ItemStack
     * @return the saved ItemStack
     */
    private ItemStack stringToItemStack(String inputString) {
        YamlConfiguration currentItem = new YamlConfiguration();
        try {
            currentItem.loadFromString(inputString);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        return currentItem.getItemStack("item", null);
    }

    /**
     * Registers a buy on an offer given the market ID.
     *
     * Before a buy can happen the offer must exist, the offer must have all the items requested, 
     * the buyer must have enough money, the buyer must have enough room in their inventory for the items. If any of these
     * conditions are not met the buy operation does not occur and this method returns false.
     *
     * @param player   player who is buying
     * @param marketID market item the player wants to buy
     * @param quantity The number of items to buy.
     * @return true if buy operation successful; false if buy operation failed.
     */
    public boolean buy(Player player, int marketID, int quantity){
        Offer offerToBuy = market.get(marketID);
        if(offerToBuy == null){
            //Check to make sure offer exists
            sendFailureMessage(player, "Offer not found");
            return false;
        } else if(quantity <= 0) {
            //Ensures the quantity is greater than 0
            sendFailureMessage(player, "Invalid Quantity");
            return false;
        } else if (offerToBuy.getItemAmount() < quantity) {
            //Make sure quantity user inputted in not too big.
            sendFailureMessage(player, "Not enough items in offer");
            return false;
        } else if (player.getUniqueId().equals(offerToBuy.getMerchant())) { //Comment statement for self testing
           //Make sure user cannot buy from him/herself.
            sendFailureMessage(player, "Cannot buy from yourself");
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

        this.save();
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
        this.save();
        return true;
    }

    /**
     * Adds a new player to the market manager.
     *
     * @param player
     */
    public void registerNewPlayer(Player player){

        bank.put(player.getUniqueId(), startAmount);
        log(player.getDisplayName() + " was added to the system");
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
     * Gets all offers with the given search term in it's display name.
     *
     * @param searchTerm the string segment to search for.
     * @return a min queue where the offer with the lowest unit price is the head.
     */
    public PriorityQueue<Offer> offers(String searchTerm) {
        PriorityQueue<Offer> returnQueue = new PriorityQueue<Offer>();
        for (Offer offer : offers()) {
            if(offer.getDisplayName().contains(searchTerm.toLowerCase())) {
                returnQueue.add(offer);
            }
        }
        return returnQueue;
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

    /**
     * @return the map that stores all offer data in this manager
     */
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
        PriorityQueue<Integer> idList = new PriorityQueue<Integer>();
        for(int listing : market.keySet()) {
            idList.add(listing);
        }
        int id = lowestFind(idList);
        offer.setId(id);
        return id;
    }


    private int lowestFind(PriorityQueue<Integer> idListing){
        int current = 1;
        for(Integer id : idListing){
            if(current == id){
                current++;
            }
            else{
                return current;
            }
        }
        return current++;
    }
}
