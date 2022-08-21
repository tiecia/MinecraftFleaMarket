package io.github.tiecia.minecraftfleamarket;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The Offer Object keeps track of player offers on the flea market.
 */
public class Offer implements Comparable<Offer> {
    //Item Stacks
    //Unit Price
    //Player who put offer up.
    private final UUID merchant;
    private final int unitPrice;
    private String merchantName;
    private int id;
    private final ItemStack item;
    private String displayName;

    public Offer(UUID inputMerchant, int inputUnitPrice, ItemStack inputItem) {
        this.merchant = inputMerchant;
        this.unitPrice = inputUnitPrice;
        this.item = inputItem;
        this.displayName = item.getType().name().toLowerCase().replace("_"," ");
        displayName = WordUtils.capitalizeFully(displayName);
        this.merchantName = Bukkit.getOfflinePlayer(inputMerchant).getName();
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

    @Override
    public String toString() {
        return displayName;
    }

    public Material getType(){
        return item.getType();
    }

    public String getDisplayName() {
        return displayName;
    }

    public UUID getMerchant() {
        return merchant;
    }

    public String format() {
        return ChatColor.GRAY + "["+ ChatColor.WHITE + id + ChatColor.GRAY + "]: " + ChatColor.AQUA + this.merchantName + ChatColor.GRAY + " is selling " + ChatColor.YELLOW + item.getAmount() + ChatColor.GREEN + " "+ displayName + ChatColor.GRAY + " for $" + ChatColor.DARK_GREEN + unitPrice + ChatColor.GRAY + " each";
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
