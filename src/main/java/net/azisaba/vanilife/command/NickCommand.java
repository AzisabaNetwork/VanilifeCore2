package net.azisaba.vanilife.command;

import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
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

public class NickCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        Sara sara = UserUtility.getSara(player);

        if (sara != Sara.GAMING && ! UserUtility.isModerator(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permissions to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (args.length == 0)
        {
            user.setNick(null);
            sender.sendMessage(Component.text("nick をリセットしました").color(NamedTextColor.GREEN));
        }
        else if (args.length == 1)
        {
            user.setNick(args[0]);
            sender.sendMessage(Component.text(String.format("%s を nick に設定しました", args[0])).color(NamedTextColor.GREEN));
        }
        else
        {
            sender.sendMessage(Component.text("Correct syntax: /nick [name]").color(NamedTextColor.RED));
            return true;
        }

        player.displayName(user.getName());
        player.playerListName(user.getName());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
