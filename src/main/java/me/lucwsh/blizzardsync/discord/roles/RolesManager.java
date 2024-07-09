package me.lucwsh.blizzardsync.discord.roles;

import me.lucwsh.blizzardsync.managers.FilesManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;

public class RolesManager {
    public static Set<String> getRoles() {
        ConfigurationSection rolesSection = FilesManager.roles.getConfigurationSection("roles");
        return rolesSection.getKeys(false);
    }

    public static String getRoleId(String roleKey) {
        return FilesManager.roles.getString("roles." + roleKey + ".id");
    }

    public static String getRolePermission(String roleKey) {
        return FilesManager.roles.getString("roles." + roleKey + ".permission");
    }
}
