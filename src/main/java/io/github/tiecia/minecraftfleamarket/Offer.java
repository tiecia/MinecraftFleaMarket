package io.github.tiecia.minecraftfleamarket;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The Offer Object keeps track of player offers on the flea market.
 * */
public class Offer {
    //Item Stacks
    //Unit Price
    //Player who put offer up.
    private Player merchant;
    private int unitPrice;
    ItemStack item;

    public Offer(Player inputMerchant, int inputUnitPrice, ItemStack inputItem) {
        this.merchant = inputMerchant;
        this.unitPrice = inputUnitPrice;
        this.item = inputItem;
    }

    public int getItemAmount() {
        return this.item.getAmount();
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public ItemStack getItem() {
        return item;
    }

    public Player getMerchant() {
        return merchant;
    }
}
