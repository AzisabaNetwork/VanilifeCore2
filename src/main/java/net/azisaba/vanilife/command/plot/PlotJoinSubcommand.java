package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.PlotInvite;
import net.azisaba.vanilife.user.request.PlotRequest;
import net.azisaba.vanilife.user.request.TradeRequest;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlotJoinSubcommand implements ISubcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "join";
    }

    @Override
    @NotNull
    public Sara getRequirement()
    {
        return Sara.DEFAULT;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: //plot join <plot>").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(args[0]);

        if (plot == null)
        {
            sender.sendMessage(Language.translate("cmd.plot.not-found", player).color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (plot.getMembers().contains(user))
        {
            sender.sendMessage(Language.translate("cmd.plot.join.already", player).color(NamedTextColor.RED));
            return;
        }

        if (! plot.getOwner().isOnline())
        {
            sender.sendMessage(Language.translate("cmd.plot.join.offline", player).color(NamedTextColor.RED));
            return;
        }

        if (user.getRequests().stream().anyMatch(r -> r.match(PlotInvite.class, plot.getOwner().getAsPlayer())))
        {
            user.getRequests().stream().filter(r -> r.match(PlotInvite.class, plot.getOwner().getAsPlayer())).toList().getFirst().onAccept();
            return;
        }

        if (! UserUtility.isAdmin(sender) && plot.getOwner().getRequests().stream().anyMatch(r -> r.match(TradeRequest.class, player)))
        {
            sender.sendMessage(Language.translate("cmd.plot.join.already-sent", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        new PlotRequest(player, plot);
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}