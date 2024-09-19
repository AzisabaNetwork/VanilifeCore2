package net.azisaba.vanilife.command;

import net.azisaba.vanilife.poll.Poll;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.PollUI;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
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

import java.util.Arrays;
import java.util.List;

public class PollCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 2)
        {
            sender.sendMessage(Component.text("Correct syntax: /poll <option1> <option2> [options…]").color(NamedTextColor.RED));
            return true;
        }

        if (! UserUtility.isModerator(player) && Poll.getInstance(player) != null)
        {
            sender.sendMessage(Language.translate("cmd.poll.already", player).color(NamedTextColor.RED));
            return true;
        }

        if (User.getInstance(player).getStatus() != UserStatus.DEFAULT)
        {
            sender.sendMessage(Language.translate("cmd.poll.cant", player).color(NamedTextColor.RED));
            return true;
        }

        if (Arrays.stream(args).anyMatch(a -> a.contains(" ") || a.contains("　")))
        {
            sender.sendMessage(Language.translate("cmd.poll.cant-include.space", player).color(NamedTextColor.RED));
            return true;
        }

        if (Arrays.stream(args).distinct().count() != args.length)
        {
            sender.sendMessage(Language.translate("cmd.poll.cant-duplicate", player).color(NamedTextColor.RED));
            return true;
        }

        if (8 < args.length)
        {
            sender.sendMessage(Language.translate("cmd.poll.limit-over", player).color(NamedTextColor.RED));
            return true;
        }

        new PollUI(player, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
