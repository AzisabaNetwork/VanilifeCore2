package net.azisaba.vanilife.command;

import net.azisaba.vanilife.user.User;
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

import java.util.ArrayList;
import java.util.List;

public class BlockCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /block <player>").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);
        User target = User.getInstance(args[0]);

        if (user == target)
        {
            sender.sendMessage(Component.text("自分自身をブロックすることはできません").color(NamedTextColor.RED));
            return true;
        }

        if (user.isBlock(target))
        {
            sender.sendMessage(Component.text(String.format("あなたは既に %s をブロックしています", args[0])).color(NamedTextColor.RED));
            return true;
        }

        user.block(target);
        sender.sendMessage(Component.text(String.format("%s をブロックしました", args[0])).color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
