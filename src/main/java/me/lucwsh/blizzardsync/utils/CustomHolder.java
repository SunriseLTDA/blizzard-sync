package me.lucwsh.blizzardsync.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CustomHolder implements InventoryHolder {
    private final boolean allowInteraction;

    public CustomHolder(boolean allowInteraction) {
        this.allowInteraction = allowInteraction;
    }

    public boolean isAllowInteraction() {
        return allowInteraction;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
