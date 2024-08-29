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

public class PlotTeleportSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "teleport";
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
            sender.sendMessage(Component.text("Correct syntax: /plot teleport <plot>").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(args[0]);

        if (plot == null)
        {
            sender.sendMessage(Component.text("Plot が見つかりませんでした").color(NamedTextColor.RED));
            return;
        }

        if (! plot.isMember(player))
        {
            sender.sendMessage(Component.text("あなたはこの Plot のメンバーではありません").color(NamedTextColor.RED));
            return;
        }

        player.teleport(plot.getSpawn());
        sender.sendMessage(Component.text(String.format("%s にテレポートしました", plot.getName())).color(NamedTextColor.GREEN));
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            return new ArrayList<>();
        }

        ArrayList<String> suggest = new ArrayList<>();
        User user = User.getInstance(player);

        if (args.length == 1)
        {
            Plot.getInstances().stream().filter(i -> i.isMember(user)).forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
