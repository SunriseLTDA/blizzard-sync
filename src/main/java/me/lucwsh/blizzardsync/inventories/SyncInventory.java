package me.lucwsh.blizzardsync.inventories;

import me.lucwsh.blizzardsync.inventories.items.SyncItems;
import me.lucwsh.blizzardsync.managers.FilesManager;
import me.lucwsh.blizzardsync.utils.CustomHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SyncInventory implements Listener {
    public static void open(Player player) {
        Boolean allowInteraction = FilesManager.syncMenu.getBoolean("menu.allow-interaction");
        CustomHolder customHolder = new CustomHolder(allowInteraction);

        String displayName = FilesManager.syncMenu.getString("menu.name").replace("&", "§");
        int rows = FilesManager.syncMenu.getInt("menu.rows");
        Sound sound = Sound.valueOf(FilesManager.syncMenu.getString("menu.open-sound"));

        Inventory inventory = Bukkit.createInventory(customHolder, rows * 9, displayName);
        Location location = player.getLocation();
        player.playSound(location, sound, 0.5F, 0.5F);


        int slotD = FilesManager.syncMenu.getInt("menu.items.discord.slot");
        int slotI = FilesManager.syncMenu.getInt("menu.items.info.slot");
        int slotS= FilesManager.syncMenu.getInt("menu.items.sync.slot");

        inventory.setItem(slotD, SyncItems.discordItem());
        inventory.setItem(slotI, SyncItems.infoItem());
        inventory.setItem(slotS, SyncItems.syncItem());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory() == null || event.getCurrentItem() == null) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        ItemMeta itemMeta = clickedItem.getItemMeta();

        if (itemMeta == null || itemMeta.getDisplayName() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        String inventoryName = event.getView().getTitle();

        String expectedDisplayName = FilesManager.syncMenu.getString("menu.name").replace("&", "§");

        if (inventoryName.equals(expectedDisplayName)) {
            int slot = event.getSlot();

//            if (slot == ) {
//
//            }
        }
    }
}