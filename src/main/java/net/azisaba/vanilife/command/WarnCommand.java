package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.penalty.Warn;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.util.Watch;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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
import java.util.UUID;

public class WarnCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! UserUtility.isAdmin(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permissions to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 2)
        {
            sender.sendMessage(Component.text("Correct syntax: /warn <player> <message>").color(NamedTextColor.RED));
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            UUID uuid = Bukkit.getPlayerUniqueId(args[0]);

            if (uuid == null)
            {
                sender.sendMessage(Component.text(args[0] + " は不明なプレイヤーです").color(NamedTextColor.RED));
                return;
            }

            if (! UserUtility.exists(uuid))
            {
                sender.sendMessage(Component.text(args[0] + " は不明なユーザーです").color(NamedTextColor.RED));
                return;
            }

            User target = User.getInstance(args[0]);

            if (sender instanceof Player player && User.getInstance(player) == target)
            {
                sender.sendMessage(Component.text("タイプミスですか？自分自身を Warn することはできません").color(NamedTextColor.RED));
                return;
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

            if (255 < message.length())
            {
                sender.sendMessage(Component.text("メッセージは255文字以内で入力してください").color(NamedTextColor.RED));
                return;
            }

            new Warn(target, message.toString());

            sender.sendMessage(Component.text(args[0] + " に警告を送信しました: ").color(NamedTextColor.RED).append(Component.text(message.toString())));

            Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                    .setTitle("警告")
                    .setColor(Color.RED)
                    .addField("対象者", String.format("%s (%s)", target.getPlaneName(), target.getId()), false)
                    .addField("送信者", (sender instanceof Player p) ? String.format("%s (%s)", p.getName(), p.getUniqueId()) : sender.getName(), false)
                    .addField("メッセージ", message.toString(), false)
                    .build()).queue();
        });

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
