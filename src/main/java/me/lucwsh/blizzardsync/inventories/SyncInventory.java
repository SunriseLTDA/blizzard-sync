package me.lucwsh.blizzardsync.inventories;

import me.lucwsh.blizzardsync.apis.SyncAPI;
import me.lucwsh.blizzardsync.inventories.items.SyncItems;
import me.lucwsh.blizzardsync.managers.FilesManager;
import me.lucwsh.blizzardsync.utils.CustomHolder;
import me.lucwsh.blizzardsync.utils.SyncUtils;
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

import java.util.List;

public class SyncInventory implements Listener {

    SyncAPI syncAPI = new SyncAPI();

    public static void open(Player player) {
        CustomHolder customHolder = new CustomHolder(false);

        String displayName = FilesManager.syncMenu.getString("menu.name").replace("&", "§");
        int rows = FilesManager.syncMenu.getInt("menu.rows");
        Sound sound = Sound.valueOf(FilesManager.syncMenu.getString("menu.open-sound"));

        Inventory inventory = Bukkit.createInventory(customHolder, rows * 9, displayName);
        Location location = player.getLocation();
        player.playSound(location, sound, 0.5F, 0.5F);

        int slotD = FilesManager.syncMenu.getInt("menu.items.discord.slot");
        int slotI = FilesManager.syncMenu.getInt("menu.items.info.slot");
        int slotS = FilesManager.syncMenu.getInt("menu.items.sync.slot");

        SyncAPI api = new SyncAPI();
        Boolean synced = api.isUserSynced(player);
        String username = api.getAccount(player);

        inventory.setItem(slotD, SyncItems.discordItem());

        ItemStack infoItem = SyncItems.infoItem();
        ItemMeta infoMeta = infoItem.getItemMeta();
        List<String> lore = infoMeta.getLore();
        lore.replaceAll(line -> line.replace("{discord}", username));
        infoMeta.setLore(lore);
        infoItem.setItemMeta(infoMeta);

        inventory.setItem(slotI, infoItem);

        inventory.setItem(slotS, synced ? SyncItems.unSyncItem() : SyncItems.syncItem());

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
            int discordSlot = FilesManager.syncMenu.getInt("menu.items.discord.slot");
            int syncSlot = FilesManager.syncMenu.getInt("menu.items.sync.slot");

            if (slot == discordSlot) {
                player.getOpenInventory().close();
                player.performCommand("discord");
            } else if (slot == syncSlot) {
                if (!syncAPI.isUserSynced(player)) {
                    player.getOpenInventory().close();
                    String securityCode = SyncUtils.generateSecurityCode(player, 5);
                    player.sendMessage("Seu código de verificação é: " + securityCode);
                } else {
                    player.getOpenInventory().close();
                    player.sendMessage("To fazeno ainda!");
                }
            }
        }
    }
}
