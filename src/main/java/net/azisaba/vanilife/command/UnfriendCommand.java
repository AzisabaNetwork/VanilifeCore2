package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
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
import java.util.UUID;

public class UnfriendCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /unfriend <friend>").color(NamedTextColor.RED));
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            UUID uuid = Bukkit.getPlayerUniqueId(args[0]);

            if (uuid == null)
            {
                sender.sendMessage(Language.translate("msg.not-found.player", player, "name=" + args[0]).color(NamedTextColor.RED));
                return;
            }

            if (! UserUtility.exists(uuid))
            {
                sender.sendMessage(Language.translate("msg.not-found.user", player, "name=" + args[0]).color(NamedTextColor.RED));
                return;
            }

            User user = User.getInstance(player);
            User friend = User.getInstance(args[0]);

            if (! user.isFriend(friend))
            {
                sender.sendMessage(Language.translate("cmd.unfriend.already", player).color(NamedTextColor.RED));
                return;
            }

            if (user.getOsatou() == friend)
            {
                user.setOsatou(null);
            }

            user.unfriend(friend);
            sender.sendMessage(Language.translate("cmd.unfriend.removed", player, "name=" + args[0]).color(NamedTextColor.RED));
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            User user = User.getInstance(player);
            user.getFriends().forEach(b -> suggest.add(b.getPlaneName()));
        }

        return suggest;
    }
}
