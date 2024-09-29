package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.JnknRequest;
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

public class JnknCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /" + label + " <player>").color(NamedTextColor.RED));
            return true;
        }

        if (Bukkit.getPlayerExact(args[0]) == null)
        {
            sender.sendMessage(Language.translate("msg.offline", from, "name=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        User fromUser = User.getInstance(from);
        User toUser = User.getInstance(args[0]);
        Player to = Bukkit.getPlayer(args[0]);

        if (from == to)
        {
            sender.sendMessage(Language.translate("cmd.jnkn.cant-yourself", from).color(NamedTextColor.RED));
            return true;
        }

        if (toUser.isBlock(fromUser))
        {
            sender.sendMessage(Language.translate("cmd.jnkn.cant", from).color(NamedTextColor.RED));
            return true;
        }

        if (fromUser.getRequests().stream().anyMatch(r -> r.match(JnknRequest.class, to)))
        {
            fromUser.getRequests().stream().filter(r -> r.match(JnknRequest.class, to)).toList().getFirst().onAccept();
            return true;
        }

        if (! UserUtility.isAdmin(sender) && toUser.getRequests().stream().anyMatch(r -> r.match(JnknRequest.class, from)))
        {
            from.sendMessage(Language.translate("cmd.jnkn.already", from, "name=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        new JnknRequest(from, to);
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
