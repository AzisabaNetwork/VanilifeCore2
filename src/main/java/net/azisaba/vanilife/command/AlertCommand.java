package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.util.UserUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class AlertCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! UserUtility.isAdmin(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permissions to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /alert <message>").color(NamedTextColor.RED));
            return true;
        }

        StringBuilder message = new StringBuilder();

        for (int i = 0; i < args.length; i ++)
        {
            if (i < args.length - 1)
            {
                message.append(' ');
            }

            message.append(args[i]);
        }

        Bukkit.getOnlinePlayers().forEach(listener -> {
            listener.playSound(listener, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.1f);

            listener.sendMessage(Component.text().build());
            listener.sendMessage(Component.text(message.toString()).color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
            listener.sendMessage(Component.text().build());
        });

        Vanilife.CHANNEL_HISTORY.sendMessageEmbeds(new EmbedBuilder()
                .setTitle("アラート")
                .setColor(Color.YELLOW)
                .addField("送信者", (sender instanceof Player p) ? String.format("%s (%s)", p.getName(), p.getUniqueId()) : sender.getName(), false)
                .addField("メッセージ", message.toString(), false)
                .build()).queue();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
