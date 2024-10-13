package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.util.Watch;
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
import java.util.ArrayList;
import java.util.List;

public class WarnCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! UserUtility.isModerator(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permissions to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 2)
        {
            sender.sendMessage(Component.text("Correct syntax: /warn <player> <message>").color(NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null)
        {
            sender.sendMessage(Component.text(args[0] + " は現在オフラインです").color(NamedTextColor.RED));
            return true;
        }

        StringBuilder message = new StringBuilder();

        for (int i = 1; i < args.length; i ++)
        {
            if (i < args.length - 1)
            {
                message.append(' ');
            }

            message.append(args[i]);
        }

        target.playSound(target, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.1f);

        target.sendMessage(Component.text().build());
        target.sendMessage(Component.text().append(Component.text("[").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD).append(Language.translate("msg.warn", target)).append(Component.text("]"))));
        target.sendMessage(Component.text(message.toString()));
        target.sendMessage(Component.text().build());

        sender.sendMessage(Component.text(args[0] + " に警告を送信しました: ").color(NamedTextColor.RED).append(Component.text(message.toString())));

        Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                .setTitle("警告")
                .setColor(Color.RED)
                .addField("対象者", String.format("%s (%s)", target.getName(), target.getUniqueId()), false)
                .addField("送信者", (sender instanceof Player p) ? String.format("%s (%s)", p.getName(), p.getUniqueId()) : sender.getName(), false)
                .addField("メッセージ", message.toString(), false)
                .build()).queue();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> ! Watch.isWatcher(p))
                    .filter(p -> p.getName().startsWith(args[0]))
                    .forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
