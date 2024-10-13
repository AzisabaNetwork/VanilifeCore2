package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.OsatouUI;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.OsatouRequest;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.util.Watch;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OsatouCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0)
        {
            new OsatouUI(player);
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + label + " <player>").color(NamedTextColor.RED));
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
            sender.sendMessage(Language.translate("cmd.osatou.cant-yourself", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            return true;
        }

        if (user.getOsatou() == toUser)
        {
            sender.sendMessage(Language.translate("cmd.osatou.already", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            return true;
        }

        if (! user.isFriend(toUser) || toUser.isBlock(user))
        {
            sender.sendMessage(Language.translate("cmd.osatou.cant", player).color(NamedTextColor.RED));
            return true;
        }

        if (user.getRequests().stream().anyMatch(r -> r.match(OsatouRequest.class, toPlayer)))
        {
            user.getRequests().stream().filter(r -> r.match(OsatouRequest.class, toPlayer)).toList().getFirst().onAccept();
            return true;
        }

        if (! UserUtility.isAdmin(sender) && toUser.getRequests().stream().anyMatch(r -> r.match(OsatouRequest.class, player)))
        {
            player.sendMessage(Language.translate("cmd.osatou.already-sent", player, "name=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        new OsatouRequest(player, toPlayer);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            return List.of();
        }

        User user = User.getInstance(player);

        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            user.getFriends().stream()
                    .filter(p -> ! Watch.isWatcher(p))
                    .filter(p -> p.getPlaneName().startsWith(args[0]))
                    .forEach(p -> suggest.add(p.getPlaneName()));
        }

        return suggest;
    }
}
