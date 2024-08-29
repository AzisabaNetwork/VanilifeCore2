package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.PlotInvitation;
import net.azisaba.vanilife.user.request.PlotRequest;
import net.azisaba.vanilife.user.request.TradeRequest;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlotJoinSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "join";
    }

    @Override
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
            sender.sendMessage(Component.text("Correct syntax: /plot join <plot>").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(args[0]);

        if (plot == null)
        {
            sender.sendMessage(Component.text("Plot が見つかりませんでした").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (plot.isMember(user))
        {
            sender.sendMessage(Component.text("あなたは既にこの Plot のメンバーです").color(NamedTextColor.RED));
            return;
        }

        if (! plot.getOwner().isOnline())
        {
            sender.sendMessage(Component.text("Plot オーナーがオフラインのため、現在 Plot 申請を送信することはできません").color(NamedTextColor.RED));
        }

        if (user.getRequests().stream().anyMatch(r -> r.auth(PlotInvitation.class, plot.getOwner().getAsOfflinePlayer().getPlayer())))
        {
            user.getRequests().stream().filter(r -> r.auth(PlotInvitation.class, plot.getOwner().getAsOfflinePlayer().getPlayer())).toList().getFirst().onAllow();
            return;
        }

        if (! UserUtility.isAdmin(sender) && plot.getOwner().getRequests().stream().anyMatch(r -> r.auth(TradeRequest.class, player)))
        {
            sender.sendMessage(Component.text(String.format("あなたは既に %s に Plot 申請を送信しています", args[0])).color(NamedTextColor.RED));
            return;
        }

        new PlotRequest(player, plot);
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return null;
    }
}
