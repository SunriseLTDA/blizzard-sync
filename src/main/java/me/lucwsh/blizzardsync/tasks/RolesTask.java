package me.lucwsh.blizzardsync.tasks;

import me.lucwsh.blizzardsync.apis.SyncAPI;
import me.lucwsh.blizzardsync.discord.DiscordClient;
import me.lucwsh.blizzardsync.discord.roles.RolesManager;
import me.lucwsh.blizzardsync.discord.roles.RolesUtils;
import me.lucwsh.blizzardsync.managers.FilesManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.dv8tion.jda.api.entities.Guild;

public class RolesTask extends BukkitRunnable {
    SyncAPI syncAPI = new SyncAPI();

    @Override
    public void run() {
        String guildId = FilesManager.discord.getString("discord.server-id");
        Guild guild = DiscordClient.jda.getGuildById(guildId);

        if (guild == null) {
            System.out.println("§a[Sync] §fGuild not found!");
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {

            Boolean synced = syncAPI.isUserSynced(player);

            if (!synced) return;

            String userId = getUserIdFromPlayer(player);

            if (userId == null || userId.equalsIgnoreCase("Nenhuma")) {
                return;
            }

            guild.retrieveMemberById(userId).queue(member -> {
                for (String roleKey : RolesManager.getRoles()) {
                    String permission = RolesManager.getRolePermission(roleKey);
                    String roleId = RolesManager.getRoleId(roleKey);
                    boolean hasPermission = player.hasPermission(permission);
                    boolean hasRole = member.getRoles().contains(guild.getRoleById(roleId));

                    if (hasPermission && !hasRole) {
                        RolesUtils.addRole(guild, member, roleId);
                    } else if (!hasPermission && hasRole) {
                        RolesUtils.removeRole(guild, member, roleId);
                    }
                }
            });
        }
    }

    private String getUserIdFromPlayer(Player player) {
        return syncAPI.getAccount(player);
    }
}
