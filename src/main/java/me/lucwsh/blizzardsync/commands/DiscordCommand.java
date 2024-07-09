package me.lucwsh.blizzardsync.commands;

import me.lucwsh.blizzardsync.managers.FilesManager;
import me.lucwsh.blizzardsync.utils.PermissionChecker;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DiscordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(FilesManager.messages.getString("messages.basic.console-cannot").replace("&", "§"));
            return true;
        }

        Player player = (Player) sender;

        if (!PermissionChecker.hasPermission(player, FilesManager.permissions.getString("permissions.discord.use"))) {
            return true;
        }

        String placeholderText = FilesManager.messages.getString("messages.discord.placeholder");
        String discordLink = FilesManager.messages.getString("messages.discord.link");
        List<String> message = FilesManager.messages.getStringList("messages.discord.join-message");

        for (String line : message) {
            if (line.contains("{placeholder}")) {
                TextComponent lineComponent = new TextComponent(line.replace("&", "§").replace("{placeholder}", ""));
                TextComponent placeholder = new TextComponent(placeholderText.replace("&", "§"));
                placeholder.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, discordLink));
                lineComponent.addExtra(placeholder);
                player.spigot().sendMessage(lineComponent);
            } else {
                player.sendMessage(line.replace("&", "§"));
            }
        }

        return true;
    }
}
