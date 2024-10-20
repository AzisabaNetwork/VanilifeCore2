package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.request.Request;
import net.azisaba.vanilife.util.UuidUtility;
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
import java.util.UUID;

public class AcceptCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /accept <request>"));
            return true;
        }

        if (! UuidUtility.isUuid(args[0]))
        {
            sender.sendMessage(Language.translate("cmd.accept.invalid", player).color(NamedTextColor.RED));
            return true;
        }

        Request request = Request.getInstance(UUID.fromString(args[0]));

        if (request == null || ! request.getTo().equals(player))
        {
            sender.sendMessage(Language.translate("cmd.accept.not-found", player).color(NamedTextColor.RED));
            return true;
        }

        request.onAccept();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
