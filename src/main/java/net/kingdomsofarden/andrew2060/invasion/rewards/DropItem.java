package net.kingdomsofarden.andrew2060.invasion.rewards;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DropItem {


    private final int lower;
    private final int diff;
    private final double chance;
    private final boolean eliteOnly;
    private final int levelReqMin;
    private final int levelReqMax;
    private final Item item;

    public DropItem(int lower, int diff, double chance, boolean eliteOnly, int levelReqMin, int levelReqMax, Item item) {
        this.lower = lower;
        this.diff = diff;
        this.chance = chance;
        this.eliteOnly = eliteOnly;
        this.levelReqMin = levelReqMin;
        this.levelReqMax = levelReqMax;
        this.item = item;
    }

    public double getChance() {
        return chance;
    }

    public int getDiff() {
        return diff;
    }

    public int getLower() {
        return lower;
    }

    public Item getItem() {
        return item;
    }

    public boolean isEliteOnly() {
        return eliteOnly;
    }

    public int getLevelReqMin() {
        return levelReqMin;
    }

    public int getLevelReqMax() {
        return levelReqMax;
    }

    public ItemStack getDrop(double roll) {
        return this.item.toItemStack((int) Math.round(this.lower + roll * this.diff));
    }


}
