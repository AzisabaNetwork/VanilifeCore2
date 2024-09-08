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
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlotInviteSkill implements ICommandSkill
{
    @Override
    @NotNull
    public String getName()
    {
        return "invite";
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
            sender.sendMessage(Component.text("Correct syntax: /plot add <player>").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(player.getChunk());

        if (plot == null)
        {
            sender.sendMessage(Component.text("Plot が見つかりませんでした").color(NamedTextColor.RED));
            return;
        }

        if (player.getName().equals(args[0]))
        {
            sender.sendMessage(Component.text("自分自身に招待を送信することはできません").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user != plot.getOwner())
        {
            sender.sendMessage(Component.text("あなたはこの Plot のオーナーではありません").color(NamedTextColor.RED));
            return;
        }

        if (Bukkit.getPlayerExact(args[0]) == null)
        {
            sender.sendMessage(Component.text(String.format("%s は現在オフラインです", args[0])).color(NamedTextColor.RED));
            return;
        }

        User member = User.getInstance(args[0]);

        if (member.getRequests().stream().anyMatch(r -> r.auth(PlotRequest.class, Bukkit.getPlayer(args[0]))))
        {
            member.getRequests().stream().filter(r -> r.auth(PlotRequest.class, Bukkit.getPlayer(args[0]))).toList().getFirst().onAllow();
            return;
        }

        if (plot.isMember(member))
        {
            sender.sendMessage(Component.text(String.format("%s は既にこの Plot のメンバーです", args[0])).color(NamedTextColor.RED));
            return;
        }

        if (! UserUtility.isAdmin(sender) && plot.getOwner().getRequests().stream().anyMatch(r -> r.auth(TradeRequest.class, player)))
        {
            sender.sendMessage(Component.text(String.format("あなたは既に %s にこの Plot への招待を送信しています", args[0])).color(NamedTextColor.RED));
            return;
        }

        new PlotInvitation(plot, Bukkit.getPlayer(args[0]));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
