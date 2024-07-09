package me.lucwsh.blizzardsync.discord.roles;

import me.lucwsh.blizzardsync.Main;
import me.lucwsh.blizzardsync.discord.DiscordClient;
import me.lucwsh.blizzardsync.managers.FilesManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class RolesUtils {
    public static void addRoles(Player player, String userId) {
        String guildId = FilesManager.discord.getString("discord.server-id");
        Guild guild = DiscordClient.jda.getGuildById(guildId);

        if (guild == null) {
            System.out.println("§a[Sync] §fGuild not found!");
            return;
        }

        guild.retrieveMemberById(userId).queue(member -> {
            for (String roleKey : RolesManager.getRoles()) {
                String permission = RolesManager.getRolePermission(roleKey);
                if (player.hasPermission(permission)) {
                    String roleId = RolesManager.getRoleId(roleKey);
                    addRole(guild, member, roleId);
                }
            }
        });
    }

    public static void removeRoles(Player player, String userId) {
        String guildId = FilesManager.discord.getString("discord.server-id");
        Guild guild = DiscordClient.jda.getGuildById(guildId);

        if (guild == null) {
            System.out.println("§a[Sync] §fGuild not found!");
            return;
        }

        guild.retrieveMemberById(userId).queue(member -> {
            for (String roleKey : RolesManager.getRoles()) {
                String permission = RolesManager.getRolePermission(roleKey);
                if (player.hasPermission(permission)) {
                    String roleId = RolesManager.getRoleId(roleKey);
                    removeRole(guild, member, roleId);
                }
            }
        });
    }

    public static void setSyncOptions(Player player, String userId) {
        String guildId = FilesManager.discord.getString("discord.server-id");
        Guild guild = DiscordClient.jda.getGuildById(guildId);

        if (guild == null) {
            System.out.println("§a[Sync] §fGuild not found!");
            return;
        }

        guild.retrieveMemberById(userId).queue(member -> {
            String syncRoleId = FilesManager.roles.getString("sync-options.role-id");
            boolean updateName = FilesManager.roles.getBoolean("sync-options.update-name");

            Role role = guild.getRoleById(syncRoleId);
            if (role != null) {
                guild.addRoleToMember(member, role).queue();
            } else {
                System.out.println("§a[Sync] §fSync role not found!");
            }

            if (updateName) {
                try {
                    member.modifyNickname(player.getName()).queue();
                } catch (HierarchyException e) {
                    Bukkit.getConsoleSender().sendMessage("§a[Sync] §fThe bot cannot change the nickname of a user with a position greater than or equal to his!");
                }
            }
        });
    }

    public static void unSetSyncOptions(String userId) {
        String guildId = FilesManager.discord.getString("discord.server-id");
        Guild guild = DiscordClient.jda.getGuildById(guildId);

        if (guild == null) {
            System.out.println("§a[Sync] §fGuild not found!");
            return;
        }

        guild.retrieveMemberById(userId).queue(member -> {
            String syncRoleId = FilesManager.roles.getString("sync-options.role-id");

            Role role = guild.getRoleById(syncRoleId);
            if (role != null) {
                guild.removeRoleFromMember(member, role).queue();
            } else {
                System.out.println("§a[Sync] §fSync role not found!");
            }

            try {
                member.modifyNickname(null).queue();
            } catch (HierarchyException e) {
                Bukkit.getConsoleSender().sendMessage("§a[Sync] §fThe bot cannot change the nickname of a user with a position greater than or equal to his!");
            }
        });
    }

    public static void addRole(Guild guild, Member member, String roleId) {
        Role role = guild.getRoleById(roleId);
        if (role != null) {
            guild.addRoleToMember(member, role).queue();
        } else {
            System.out.println("§a[Sync] §fRole not found!");
        }
    }

    public static void removeRole(Guild guild, Member member, String roleId) {
        Role role = guild.getRoleById(roleId);
        if (role != null) {
            guild.removeRoleFromMember(member, role).queue();
        } else {
            System.out.println("§a[Sync] §fRole not found!");
        }
    }

    public static void executeRewards(Player player) {
        Bukkit.getScheduler().runTask(Main.instance, () -> {
            List<String> rewards = FilesManager.rewards.getStringList("rewards.commands");
            for (String reward : rewards) {
                String command = reward.replace("{player}", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        });
    }


}
