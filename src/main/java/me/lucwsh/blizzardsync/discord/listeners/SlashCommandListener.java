package me.lucwsh.blizzardsync.discord.listeners;

import me.lucwsh.blizzardsync.apis.SyncAPI;
import me.lucwsh.blizzardsync.discord.roles.RolesUtils;
import me.lucwsh.blizzardsync.managers.FilesManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SlashCommandListener extends ListenerAdapter {

    private final SyncAPI syncAPI = new SyncAPI();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Guild guild = event.getJDA().getGuildById(FilesManager.discord.getString("discord.server-id"));
        String command = event.getName();

        if (!event.isFromGuild() || event.getGuild() != guild) {
            event.reply(FilesManager.discord.getString("discord.commands.only-server")).setEphemeral(true).queue();
            return;
        }

        switch (command) {
            case "ping":
                Boolean ephemeralP = FilesManager.discord.getBoolean("discord.commands.ping.ephemeral");
                event.deferReply(ephemeralP).queue();

                event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.ping.message").replace("{ping}", String.valueOf(event.getJDA().getGatewayPing()))).queue();
                break;

            case "sync":
                Boolean ephemeralS = FilesManager.discord.getBoolean("discord.commands.sync.ephemeral");
                event.deferReply(ephemeralS).queue();
                String syncPlayerName = event.getOption("player").getAsString();
                String syncPlayerCode = event.getOption("security").getAsString();
                Player player = Bukkit.getPlayer(syncPlayerName);

                if (player == null) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.sync.player-not-found").replace("{player}", syncPlayerName)).queue();
                    return;
                }

                if (syncAPI.isUserSynced(player)) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.sync.already-synced").replace("{player}", syncPlayerName)).setEphemeral(true).queue();
                    return;
                }

                String syncMemberID = event.getMember().getUser().getId();

                if (!syncAPI.getSecurity(player).equals(syncPlayerCode)
                        || syncAPI.getSecurity(player).equalsIgnoreCase("Nenhum")
                        || syncAPI.getSecurity(player).equalsIgnoreCase(null)) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.sync.security-not-found").replace("{code}", syncPlayerCode)).setEphemeral(true).queue();
                    return;
                }

                syncAPI.syncUser(player, syncMemberID);
                syncAPI.resetSecurity(player);
                RolesUtils.addRoles(player, syncMemberID);
                RolesUtils.setSyncOptions(player, syncMemberID);

                event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.sync.message").replace("{player}", player.getName())).setEphemeral(true).queue();

                RolesUtils.executeRewards(player);
                break;

            case "unsync":
                Boolean ephemeralUS = FilesManager.discord.getBoolean("discord.commands.unsync.ephemeral");
                event.deferReply(ephemeralUS).queue();
                String unSyncPlayerName = event.getOption("player").getAsString();

                Boolean option = FilesManager.discord.getBoolean("discord.commands.unsync.use");

                if (!option) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.unsync.cannot-use")).queue();
                    return;
                }

                Player unSyncPlayer = Bukkit.getPlayer(unSyncPlayerName);

                if (unSyncPlayer == null) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.unsync.player-not-found").replace("{player}", unSyncPlayerName)).queue();
                    return;
                }

                String unSyncPlayerID = syncAPI.getAccount(unSyncPlayer);
                User unSyncUser = event.getMember().getUser();
                String unSyncUserID = unSyncUser.getId();

                if (unSyncUserID.equals(unSyncPlayerID)) {
                    syncAPI.unSyncUser(unSyncPlayer);
                    RolesUtils.removeRoles(unSyncPlayer, unSyncPlayerID);
                    RolesUtils.unSetSyncOptions(unSyncPlayerID);
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.unsync.message").replace("{player}", unSyncPlayer.getName())).setEphemeral(true).queue();
                } else {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.unsync.not-synced").replace("{player}", unSyncPlayerName)).setEphemeral(true).queue();
                }

                break;

            case "forcesync":
                Boolean ephemeralFS = FilesManager.discord.getBoolean("discord.commands.forcesync.ephemeral");
                event.deferReply(ephemeralFS).queue();

                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.without-permission")).setEphemeral(true).queue();
                    return;
                }

                String forceSyncPlayerName = event.getOption("player").getAsString();
                String forceSyncDiscordId = event.getOption("discord-id").getAsString();

                Player forceSyncPlayer = Bukkit.getPlayer(forceSyncPlayerName);
                if (forceSyncPlayer == null) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.forcesync.player-not-found").replace("{player}", forceSyncPlayerName)).queue();
                    return;
                }

                Member forceSyncMember;
                try {
                    forceSyncMember = guild.retrieveMemberById(forceSyncDiscordId).complete();
                    if (forceSyncMember == null) {
                        event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.forcesync.discord-not-found").replace("{discord}", forceSyncDiscordId)).setEphemeral(ephemeralFS).queue();
                        return;
                    }
                } catch (Exception ex) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.forcesync.discord-not-found").replace("{discord}", forceSyncDiscordId)).setEphemeral(ephemeralFS).queue();
                    return;
                }

                syncAPI.syncUser(forceSyncPlayer, forceSyncMember.getUser().getId());
                RolesUtils.addRoles(forceSyncPlayer, forceSyncMember.getUser().getId());
                RolesUtils.setSyncOptions(forceSyncPlayer, forceSyncMember.getUser().getId());
                event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.forcesync.message").replace("{player}", forceSyncPlayer.getName()).replace("{discord}", forceSyncMember.getUser().getName())).setEphemeral(ephemeralFS).queue();
                break;

            case "forceunsync":

                Boolean ephemeralFUS = FilesManager.discord.getBoolean("discord.commands.forceunsync.ephemeral");
                event.deferReply(ephemeralFUS).queue();

                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.without-permission")).queue();
                    return;
                }

                String forceUnSyncPlayerName = event.getOption("player").getAsString();

                Player forceUnSyncPlayer = Bukkit.getPlayer(forceUnSyncPlayerName);
                if (forceUnSyncPlayer == null) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.forceunsync.player-not-found").replace("{player}", forceUnSyncPlayerName)).queue();
                    return;
                }

                String forceUnSyncDiscord = syncAPI.getAccount(forceUnSyncPlayer);
                Member forceUnSyncMember = guild.retrieveMemberById(forceUnSyncDiscord).complete();
                if (forceUnSyncMember == null) {
                    event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.forceunsync.discord-not-found").replace("{discord}", forceUnSyncDiscord)).queue();
                    return;
                }
                syncAPI.unSyncUser(forceUnSyncPlayer);
                RolesUtils.removeRoles(forceUnSyncPlayer, forceUnSyncDiscord);
                RolesUtils.unSetSyncOptions(forceUnSyncDiscord);
                event.getHook().sendMessage(FilesManager.discord.getString("discord.commands.forceunsync.message").replace("{player}", forceUnSyncPlayer.getName()).replace("{discord}", forceUnSyncMember.getUser().getName())).queue();
                break;

            default:
                break;
        }
    }
}
