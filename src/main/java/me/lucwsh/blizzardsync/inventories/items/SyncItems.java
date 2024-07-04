package me.lucwsh.blizzardsync.inventories.items;

import me.lucwsh.blizzardsync.apis.SyncAPI;
import me.lucwsh.blizzardsync.managers.FilesManager;
import me.lucwsh.blizzardsync.utils.SkullBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SyncItems {

    public static ItemStack discordItem() {

        boolean useHead = FilesManager.syncMenu.getBoolean("menu.items.discord.use-heads");
        String value = FilesManager.syncMenu.getString("menu.items.discord.value");
        String material = FilesManager.syncMenu.getString("menu.items.discord.material");
        int data = FilesManager.syncMenu.getInt("menu.items.discord.data");
        String display = FilesManager.syncMenu.getString("menu.items.discord.display").replace("&", "§");
        List<String> lore = FilesManager.syncMenu.getStringList("menu.items.discord.lore");
        lore.replaceAll(line -> line.replace("&", "§"));

        if (useHead) {
            ItemStack item = SkullBuilder.fromBase64(SkullBuilder.Type.BLOCK, value);

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(display);
            meta.setLore(lore);

            item.setItemMeta(meta);

            return item;
        } else {
            ItemStack item = new ItemStack(Material.getMaterial(material), 1, (short) data);

            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(display));
            meta.setLore(lore);

            item.setItemMeta(meta);

            return item;

        }
    }

    public static ItemStack infoItem() {

        boolean useHead = FilesManager.syncMenu.getBoolean("menu.items.info.use-heads");
        String value = FilesManager.syncMenu.getString("menu.items.info.value");
        String material = FilesManager.syncMenu.getString("menu.items.info.material");
        int data = FilesManager.syncMenu.getInt("menu.items.info.data");
        String display = FilesManager.syncMenu.getString("menu.items.info.display").replace("&", "§");
        List<String> lore = FilesManager.syncMenu.getStringList("menu.items.info.lore");
        lore.replaceAll(line -> line.replace("&", "§"));

        if (useHead) {
            ItemStack item = SkullBuilder.fromBase64(SkullBuilder.Type.BLOCK, value);

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(display);
            meta.setLore(lore);

            item.setItemMeta(meta);

            return item;
        } else {
            ItemStack item = new ItemStack(Material.getMaterial(material), 1, (short) data);

            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(display));
            meta.setLore(lore);

            item.setItemMeta(meta);

            return item;

        }
    }

    public static ItemStack syncItem() {

        boolean useHead = FilesManager.syncMenu.getBoolean("menu.items.sync.use-heads");
        String value = FilesManager.syncMenu.getString("menu.items.sync.value");
        String material = FilesManager.syncMenu.getString("menu.items.sync.material");
        int data = FilesManager.syncMenu.getInt("menu.items.sync.data");
        String display = FilesManager.syncMenu.getString("menu.items.sync.display").replace("&", "§");
        List<String> lore = FilesManager.syncMenu.getStringList("menu.items.sync.lore");
        lore.replaceAll(line -> line.replace("&", "§"));

        if (useHead) {
            ItemStack item = SkullBuilder.fromBase64(SkullBuilder.Type.BLOCK, value);

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(display);
            meta.setLore(lore);

            item.setItemMeta(meta);

            return item;
        } else {
            ItemStack item = new ItemStack(Material.getMaterial(material), 1, (short) data);

            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(display));
            meta.setLore(lore);

            item.setItemMeta(meta);

            return item;

        }
    }

    public static ItemStack unSyncItem() {

        boolean useHead = FilesManager.syncMenu.getBoolean("menu.items.unsync.use-heads");
        String value = FilesManager.syncMenu.getString("menu.items.unsync.value");
        String material = FilesManager.syncMenu.getString("menu.items.unsync.material");
        int data = FilesManager.syncMenu.getInt("menu.items.unsync.data");
        String display = FilesManager.syncMenu.getString("menu.items.unsync.display").replace("&", "§");
        List<String> lore = FilesManager.syncMenu.getStringList("menu.items.unsync.lore");
        lore.replaceAll(line -> line.replace("&", "§"));

        if (useHead) {
            ItemStack item = SkullBuilder.fromBase64(SkullBuilder.Type.BLOCK, value);

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(display);
            meta.setLore(lore);

            item.setItemMeta(meta);

            return item;
        } else {
            ItemStack item = new ItemStack(Material.getMaterial(material), 1, (short) data);

            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(display));
            meta.setLore(lore);

            item.setItemMeta(meta);

            return item;

        }
    }

}
