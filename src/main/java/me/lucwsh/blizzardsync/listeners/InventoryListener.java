package me.lucwsh.blizzardsync.listeners;

import me.lucwsh.blizzardsync.utils.CustomHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof CustomHolder) {
            CustomHolder customHolder = (CustomHolder) holder;
            if (!customHolder.isAllowInteraction()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof CustomHolder) {
            CustomHolder customHolder = (CustomHolder) holder;
            if (!customHolder.isAllowInteraction()) {
                event.setCancelled(true);
            }
        }
    }
}
