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

public class PlotListSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "list";
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

        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /plot list").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        ArrayList<Plot> plots = new ArrayList<>();
        Plot.getInstances().stream().filter(i -> i.isMember(user)).forEach(plots::add);

        int size = plots.size();
        sender.sendMessage(Component.text("Plots (" + size + "):").color((0 < size) ? NamedTextColor.WHITE : NamedTextColor.RED));

        for (Plot plot : plots)
        {
            sender.sendMessage(Component.text("- ").color(NamedTextColor.DARK_GRAY)
                    .append(Component.text(plot.getName()).color(NamedTextColor.GREEN)));
        }
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return null;
    }
}
