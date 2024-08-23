package net.azisaba.vanilife.command;

import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UnblockCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /unblock <player>").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);
        User target = User.getInstance(args[0]);

        if (! user.isBlock(target))
        {
            sender.sendMessage(Component.text(String.format("あなたは既に %s をブロックしていません", args[0])).color(NamedTextColor.RED));
            return true;
        }

        user.unblock(target);
        sender.sendMessage(Component.text(String.format("%s のブロックを解除しました", args[0])).color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            return List.of();
        }

        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            User user = User.getInstance(player);
            user.getBlocks().forEach(b -> suggest.add(b.getPlaneName()));
        }

        return suggest;
    }
}
