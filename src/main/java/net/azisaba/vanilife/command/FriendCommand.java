package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.FriendRequest;
import net.azisaba.vanilife.util.UserUtility;
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

public class FriendCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /friend <player>").color(NamedTextColor.RED));
            return true;
        }

        if (Bukkit.getPlayerExact(args[0]) == null)
        {
            sender.sendMessage(Language.translate("msg.offline", player, "name=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        User toUser = User.getInstance(args[0]);
        Player toPlayer = Bukkit.getPlayer(args[0]);

        if (player == toPlayer)
        {
            sender.sendMessage(Language.translate("cmd.friend.cant-yourself", player).color(NamedTextColor.RED));
            return true;
        }

        if (toUser.isBlock(user))
        {
            sender.sendMessage(Language.translate("cmd.friend.cant", player).color(NamedTextColor.RED));
            return true;
        }

        if (toUser.isFriend(user))
        {
            sender.sendMessage(Language.translate("cmd.friend.already", player).color(NamedTextColor.RED));
            return true;
        }

        if (user.getRequests().stream().anyMatch(r -> r.match(FriendRequest.class, toPlayer)))
        {
            user.getRequests().stream().filter(r -> r.match(FriendRequest.class, toPlayer)).toList().getFirst().onAccept();
            return true;
        }

        if (! UserUtility.isAdmin(sender) && toUser.getRequests().stream().anyMatch(r -> r.match(FriendRequest.class, player)))
        {
            player.sendMessage(Language.translate("cmd.friend.already-sent", player, "name=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        new FriendRequest(player, toPlayer);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getName().startsWith(args[0]))
                    .forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
