package io.github.tiecia.minecraftfleamarket;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The Offer Object keeps track of player offers on the flea market.
 */
public class Offer implements Comparable<Offer> {
    //Item Stacks
    //Unit Price
    //Player who put offer up.
    private final Player merchant;
    private final int unitPrice;
    private int id;
    private final ItemStack item;

    public Offer(Player inputMerchant, int inputUnitPrice, ItemStack inputItem) {
        this.merchant = inputMerchant;
        this.unitPrice = inputUnitPrice;
        this.item = inputItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemAmount() {
        return this.item.getAmount();
    }

    /**
     *
     * @param amount the amount of items to buy from this offer. Amount must be not be more than the value of getItemAmount()
     */
    public ItemStack buy(int amount){
        assert amount <= getItemAmount();
        ItemStack stack = new ItemStack(this.item.getType(), 0);
        stack.setItemMeta(item.getItemMeta());
        while(stack.getAmount() < amount){ //Put 'amount' items in the new stack from the offer stack.
            item.setAmount(item.getAmount()-1);
            stack.setAmount(stack.getAmount()+1);
        }
        return stack;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public ItemStack getItem() {
        return item;
    }

    public Material getType(){
        return item.getType();
    }

    public Player getMerchant() {
        return merchant;
    }

    public String print() {
        return "ID: " + id + " Unit Price: " + unitPrice + " Amount: " + item.getAmount() + " Seller: " + merchant.getDisplayName();
    }
    //compareTo for offer objects compares the price values
    public int compareTo(Offer o) {
        if (o != null) {
            int compareInt = Integer.compare(this.unitPrice, o.getUnitPrice());
            return compareInt;
        }
        else
            return -1;
    }
}
