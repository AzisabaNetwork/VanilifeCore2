package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlotKickSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "kick";
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
            sender.sendMessage(Component.text("Correct syntax: /plot add <player>").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(player.getChunk());

        if (plot == null)
        {
            sender.sendMessage(Component.text("Plot が見つかりませんでした").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user != plot.getOwner())
        {
            sender.sendMessage(Component.text("あなたはこの Plot のオーナーではありません").color(NamedTextColor.RED));
            return;
        }

        User member = User.getInstance(args[0]);

        if (! plot.isMember(member))
        {
            sender.sendMessage(Component.text(String.format("%s はこの Plot のメンバーではありません", args[0])).color(NamedTextColor.RED));
            return;
        }

        if (plot.getOwner() == member)
        {
            sender.sendMessage(Component.text("Plot のオーナーを追放することはできません").color(NamedTextColor.RED));
            return;
        }

        plot.removeMember(member);
        sender.sendMessage(Component.text(String.format("%s を Plot から追放しました", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            return new ArrayList<>();
        }

        Plot plot = Plot.getInstance(player.getChunk());

        if (plot == null)
        {
            return new ArrayList<>();
        }

        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            plot.getMembers().forEach(m -> suggest.add(m.getPlaneName()));
        }

        return suggest;
    }
}
