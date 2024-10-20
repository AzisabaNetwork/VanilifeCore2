package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.TrustRank;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.TeleportRequest;
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

        Player fromPlayer = Bukkit.getPlayer(args[0]);

        if (fromPlayer == null)
        {
            sender.sendMessage(Language.translate("msg.offline", player, "name=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        User fromUser = User.getInstance(player);

        if (player == fromPlayer)
        {
            sender.sendMessage(Language.translate("cmd.tpa.cant-yourself", player).color(NamedTextColor.RED));
            return true;
        }

        if (fromUser.getRequests().stream().noneMatch(r -> r.match(TeleportRequest.class, fromPlayer)))
        {
            sender.sendMessage(Language.translate("cmd.tpa.not-found", player, "name=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        if (TrustRank.VISITOR.getLevel() < fromUser.getTrustRank().getLevel() && fromUser.getMola() < Vanilife.MOLA_TPR)
        {
            fromPlayer.sendMessage(Language.translate("msg.shortage", fromPlayer, "need=" + (Vanilife.MOLA_TPR - fromUser.getMola())).color(NamedTextColor.RED));
            fromPlayer.playSound(fromPlayer, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            return true;
        }

        fromUser.getRequests().stream().filter(r -> r.match(TeleportRequest.class, fromPlayer)).toList().getFirst().onAccept();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
