package net.azisaba.vanilife.command;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.TeleportRequest;
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

public class TprCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /tpr <player>").color(NamedTextColor.RED));
            return true;
        }

        if (Bukkit.getPlayerExact(args[0]) == null)
        {
            sender.sendMessage(Component.text(String.format("%s は現在オフラインです", args[0])).color(NamedTextColor.RED));
            return true;
        }

        Player toPlayer = Bukkit.getPlayer(args[0]);
        User toUser = User.getInstance(toPlayer);

        if (player == toPlayer)
        {
            sender.sendMessage(Component.text("自分自身に Teleport リクエストを送信することはできません").color(NamedTextColor.RED));
            return true;
        }

        if (toUser.isBlock(User.getInstance(player)))
        {
            sender.sendMessage(Component.text("このプレイヤーに Teleport リクエストを送ることはできません").color(NamedTextColor.RED));
            return true;
        }

        if (! UserUtility.isAdmin(sender) && toUser.getRequests().stream().anyMatch(r -> r.auth(TeleportRequest.class, player)))
        {
            sender.sendMessage(Component.text(String.format("あなたは既に %s に Teleport リクエストを送信しています", args[0])).color(NamedTextColor.RED));
            return true;
        }

        new TeleportRequest(player, toPlayer);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
