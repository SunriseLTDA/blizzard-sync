package me.lucwsh.blizzardsync.discord.listeners;

import me.lucwsh.blizzardsync.apis.SyncAPI;
import me.lucwsh.blizzardsync.managers.FilesManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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

        String permission = FilesManager.discord.getString("discord.commands.without-permission");
        String servers = FilesManager.discord.getString("discord.commands.only-server");

        if (!event.isFromGuild() || event.getGuild() != guild) {
            event.reply(servers)
                    .setEphemeral(true).queue();
            return;
        }

        switch (command) {

            case "ping":
                Boolean ephemeralP = FilesManager.discord.getBoolean("discord.commands.ping.ephemeral");
                String messageP = FilesManager.discord.getString("discord.commands.ping.message")
                        .replace("{ping}", String.valueOf(event.getJDA().getGatewayPing()));

                event.reply(messageP)
                        .setEphemeral(ephemeralP).queue();
                break;

            case "sync":
                String syncPlayerName = event.getOption("player").getAsString();
                String syncPlayerCode = event.getOption("security").getAsString();

                Player player = Bukkit.getPlayer(syncPlayerName);

                if (player == null) {
                    event.reply("Jogador não encontrado")
                            .setEphemeral(true).queue();
                    return;
                }

                if (syncAPI.isUserSynced(player)) {
                    event.reply("Esse jogador já está vinculado, use /unsync antes!")
                            .setEphemeral(true).queue();
                    return;
                }

                String syncMemberID = event.getMember().getUser().getId();

                if (!syncAPI.getSecurity(player).equals(syncPlayerCode)
                        || syncAPI.getSecurity(player).equalsIgnoreCase("Nenhum")
                        || syncAPI.getSecurity(player).equalsIgnoreCase(null)) {
                    event.reply("Código de verificação incorreto")
                            .setEphemeral(true).queue();
                    return;
                }

                syncAPI.syncUser(player, syncMemberID);
                syncAPI.resetSecurity(player);
                event.reply("Conta vinculada com sucesso!")
                        .setEphemeral(true).queue();

                break;

            case "forcesync":

                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.reply(permission)
                            .setEphemeral(true).queue();
                    return;
                }

                String forceSyncPlayerName = event.getOption("player").getAsString();
                String forceSyncDiscordId = event.getOption("discord-id").getAsString();

                Boolean ephemeralFS = FilesManager.discord.getBoolean("discord.commands.forcesync.ephemeral");

                String messageFSPN = FilesManager.discord.getString("discord.commands.forcesync.player-not-found")
                        .replace("{player}", forceSyncPlayerName);

                String messageFSDN = FilesManager.discord.getString("discord.commands.forcesync.discord-not-found")
                        .replace("{discord}", forceSyncDiscordId);

                Player forceSyncPlayer = Bukkit.getPlayer(forceSyncPlayerName);

                if (forceSyncPlayer == null) {
                    event.reply(messageFSPN)
                            .setEphemeral(ephemeralFS).queue();
                    return;

                }

                Member forceSyncMember = guild.retrieveMemberById(forceSyncDiscordId).complete();
                String forceSyncID = forceSyncMember.getUser().getId();

                if (forceSyncID == null) {
                    event.reply(messageFSDN)
                            .setEphemeral(ephemeralFS).queue();
                    return;
                }

                String messageFSD = FilesManager.discord.getString("discord.commands.forcesync.message")
                        .replace("{player}", forceSyncPlayer.getName())
                        .replace("{discord}", forceSyncID);

                syncAPI.syncUser(forceSyncPlayer, forceSyncID);

                event.reply(messageFSD)
                        .setEphemeral(ephemeralFS).queue();

                break;

            case "forceunsync":

                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.reply(permission)
                            .setEphemeral(true).queue();
                    return;
                }

                String forceUnSyncPlayerName = event.getOption("player").getAsString();

                Boolean ephemeralFUS = FilesManager.discord.getBoolean("discord.commands.forceunsync.ephemeral");
                String messageFUSPN = FilesManager.discord.getString("discord.commands.forceunsync.player-not-found")
                        .replace("{player}", forceUnSyncPlayerName);

                Player forceUnSyncPlayer = Bukkit.getPlayer(forceUnSyncPlayerName);

                if (forceUnSyncPlayer == null) {
                    event.reply(messageFUSPN)
                            .setEphemeral(ephemeralFUS).queue();
                    return;
                }

                String forceUnSyncDiscord = syncAPI.getAccount(forceUnSyncPlayer);

                String messageFUSD = FilesManager.discord.getString("discord.commands.forceunsync.message")
                        .replace("{player}", forceUnSyncPlayer.getName())
                        .replace("{discord}", forceUnSyncDiscord);

                syncAPI.unSyncUser(forceUnSyncPlayer);
                event.reply(messageFUSD)
                        .setEphemeral(ephemeralFUS).queue();

                break;

            default:
                break;
        }
    }
}
