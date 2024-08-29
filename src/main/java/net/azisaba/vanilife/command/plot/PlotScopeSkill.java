package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.plot.PlotScope;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class PlotScopeSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "scope";
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
            sender.sendMessage(Component.text("Correct syntax: /plot scope <scope>").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(player.getChunk());
        User user = User.getInstance(player);

        if (user != plot.getOwner())
        {
            sender.sendMessage(Component.text("あなたはこの Plot のオーナーではありません").color(NamedTextColor.RED));
            return;
        }

        if (Arrays.stream(PlotScope.values()).noneMatch(s -> s.toString().equals(args[0].toUpperCase())))
        {
            sender.sendMessage(Component.text(String.format("Scope %s は定義されていません", args[0])).color(NamedTextColor.RED));
            return;
        }

        plot.setScope(PlotScope.valueOf(args[0].toUpperCase()));
        sender.sendMessage(Component.text(String.format("Scope を %s に変更しました", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Arrays.stream(PlotScope.values()).forEach(s -> suggest.add(s.toString().toLowerCase()));
        }

        return suggest;
    }
}
