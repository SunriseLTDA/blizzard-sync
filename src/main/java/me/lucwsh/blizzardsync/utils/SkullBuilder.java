package me.lucwsh.blizzardsync.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;

public class SkullBuilder {
    public static ItemStack fromName(Type type, String name) {
        ItemStack item = new ItemStack(type.mat, 1, (short)3);
        return withName(item, name);
    }
    public static ItemStack withName(ItemStack item, String name) {
        notNull(item, "item");
        notNull(name, "name");
        return Bukkit.getUnsafe().modifyItemStack(item, "{SkullOwner:\"" + name + "\"}");
    }
    public static ItemStack fromUrl(Type type, String url) {
        ItemStack item = new ItemStack(type.mat, 1, (short)3);
        return withUrl(item, url);
    }
    public static ItemStack withUrl(ItemStack item, String url) {
        URI actualUrl;
        notNull(url, "url");

        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String base64 = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl.toString() + "\"}}}";
        base64 = Base64.getEncoder().encodeToString(base64.getBytes());
        return withBase64(item, base64);
    }
    public static ItemStack fromBase64(Type type, String base64) {
        ItemStack item = new ItemStack(type.mat, 1, (short)3);
        return withBase64(item, base64);
    }
    public static ItemStack withBase64(ItemStack item, String base64) {
        notNull(item, "item");
        notNull(base64, "base64");
        UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(item, "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}");
    }
    public enum Type
    {
        BLOCK(Material.PLAYER_HEAD),
        ITEM(Material.PLAYER_HEAD);

        private Material mat;


        Type(Material mat) {
            this.mat = mat;
        }
    }

    private static void notNull(Object o, String name) {
        if (o == null)
            throw new NullPointerException(name + " should not be null!");
    }
}