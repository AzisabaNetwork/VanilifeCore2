package net.azisaba.vanilife.command;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.TeleportRequest;
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

import java.util.List;

public class TpaCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /tpa <player>").color(NamedTextColor.RED));
            return true;
        }

        if (Bukkit.getPlayerExact(args[0]) == null)
        {
            sender.sendMessage(Component.text(String.format("%s は現在オフラインです", args[0])).color(NamedTextColor.RED));
            return true;
        }

        Player toPlayer = Bukkit.getPlayer(args[0]);
        User fromUser = User.getInstance(player);

        if (player == toPlayer)
        {
            sender.sendMessage(Component.text("自分自身に Teleport リクエストを送信することはできません").color(NamedTextColor.RED));
            return true;
        }

        if (fromUser.getRequests().stream().noneMatch(r -> r.auth(TeleportRequest.class, toPlayer)))
        {
            sender.sendMessage(Component.text(String.format("%s からの Teleport リクエストが見つかりませんでした", args[0])).color(NamedTextColor.RED));
            return true;
        }

        fromUser.getRequests().stream().filter(r -> r.auth(TeleportRequest.class, toPlayer)).toList().getFirst().onAllow();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
