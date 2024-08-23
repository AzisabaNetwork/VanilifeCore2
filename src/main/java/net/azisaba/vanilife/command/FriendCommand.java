package net.azisaba.vanilife.command;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.FriendRequest;
import net.azisaba.vanilife.user.request.TradeRequest;
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
        if (! (sender instanceof Player from))
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
            sender.sendMessage(Component.text(String.format("%s は現在オフラインです", args[0])).color(NamedTextColor.RED));
            return true;
        }

        User fromUser = User.getInstance(from);
        User toUser = User.getInstance(args[0]);
        Player to = Bukkit.getPlayer(args[0]);

        if (from == to)
        {
            sender.sendMessage(Component.text("自分自身に Friend 申請を送信することはできません").color(NamedTextColor.RED));
            return true;
        }

        if (toUser.isBlock(fromUser))
        {
            sender.sendMessage(Component.text("このプレイヤーに Friend 申請を送ることはできません").color(NamedTextColor.RED));
            return true;
        }

        if (toUser.isFriend(fromUser))
        {
            fromUser.unfriend(toUser);
            sender.sendMessage(Component.text(String.format("%s をフレンドから削除しました", args[0])).color(NamedTextColor.GREEN));
            return true;
        }

        if (fromUser.getRequests().stream().anyMatch(r -> r.auth(FriendRequest.class, to)))
        {
            fromUser.getRequests().stream().filter(r -> r.auth(FriendRequest.class, to)).toList().getFirst().onAllow();
            return true;
        }

        if (! from.isOp() && toUser.getRequests().stream().anyMatch(r -> r.auth(TradeRequest.class, from)))
        {
            from.sendMessage(Component.text(String.format("あなたは既に %s に Friend 申請を送信しています", args[0])).color(NamedTextColor.RED));
            return true;
        }

        new FriendRequest(from, to);
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
