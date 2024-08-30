package net.azisaba.vanilife.command;

import net.azisaba.vanilife.poll.Poll;
import net.azisaba.vanilife.util.UuidUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class VoteCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 2)
        {
            sender.sendMessage(Component.text("Correct syntax: /vote <poll> <option>").color(NamedTextColor.RED));
            return true;
        }

        if (! UuidUtility.isUuid(args[0]))
        {
            sender.sendMessage(Component.text(args[0] + " は無効な投票識別子です").color(NamedTextColor.RED));
            return true;
        }

        Poll poll = Poll.getInstance(UUID.fromString(args[0]));

        if (poll == null)
        {
            sender.sendMessage(Component.text("この投票は既に終了している可能性があります").color(NamedTextColor.RED));
            return true;
        }

        poll.vote(player, args[1]);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
