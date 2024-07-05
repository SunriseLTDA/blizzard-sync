package me.lucwsh.blizzardsync.discord;

import me.lucwsh.blizzardsync.discord.listeners.SlashCommandListener;
import me.lucwsh.blizzardsync.managers.FilesManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import javax.security.auth.login.LoginException;

public class DiscordClient {
    public static JDA jda;

    public static void initialize(String token) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.DIRECT_MESSAGES
        );
        builder.addEventListeners(
                new SlashCommandListener()
        );
        builder.setStatus(OnlineStatus.IDLE);
        builder.setActivity(Activity.watching("Sincronize sua conta!"));
        jda = builder.build();

        jda.updateCommands().addCommands(
                Commands.slash("ping", "Veja o ping do bot."),

                Commands.slash("sync", "Sincronize sua conta.")
                        .addOption(OptionType.STRING, "player", "Seu nick in-game.", true)
                        .addOption(OptionType.STRING, "security", "Seu código de verificação.", true),

                Commands.slash("forcesync", "Sincronize um jogador.")
                        .addOption(OptionType.STRING, "player", "Nome do jogador.", true)
                        .addOption(OptionType.STRING, "discord-id", "Nome da conta do Discord do jogador.", true),

                Commands.slash("forceunsync", "Dessincronize um jogador.")
                        .addOption(OptionType.STRING, "player", "Player's name.", true)

//                Commands.slash("unsync", "Dessincronize sua conta.")
//                        .addOption(OptionType.STRING, "player", "Seu nick in-game.", true)

        ).queue();
    }

    // public static void updateUser() {}

    public static void shutdown() {
        if (jda != null) {
            jda.shutdown();
        }
    }
}
