package net.azisaba.vanilife.command;

import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaraCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! UserUtility.isAdmin(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 2)
        {
            sender.sendMessage(Component.text("Correct syntax: /sara <player> <sara>").color(NamedTextColor.RED));
            return true;
        }

        if (Arrays.stream(Sara.values()).noneMatch(s -> s.toString().equals(args[1])))
        {
            sender.sendMessage(Component.text(String.format("%s は未定義の皿です", args[1])).color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(args[0]);
        Sara sara = Sara.valueOf(args[1]);

        user.setSara(sara);

        if (user.isOnline())
        {
            Bukkit.getPlayer(user.getId()).playerListName(user.getName());
        }

        sender.sendMessage(Component.text(String.format("%s の皿を %s に変更しました", args[0], args[1])).color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (! UserUtility.isAdmin(sender))
        {
            return suggest;
        }

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().forEach(p -> suggest.add(p.getName()));
        }

        if (args.length == 2)
        {
            Arrays.stream(Sara.values()).forEach(s -> suggest.add(s.toString()));
        }

        return suggest;
    }
}
