package net.azisaba.vanilife.command;

import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.PlotSettingsUI;
import net.azisaba.vanilife.ui.PlotUI;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlotCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (1 < args.length)
        {
            sender.sendMessage(Component.text("Correct syntax: /plot [plot]").color(NamedTextColor.RED));
            return true;
        }

        Plot plot = args.length == 1 ? Plot.getInstance(args[0]) : Plot.getInstance(player.getChunk());

        if (plot == null)
        {
            sender.sendMessage(Language.translate("cmd.plot.not-found", player).color(NamedTextColor.RED));
            return true;
        }

        if (plot.getOwner() == User.getInstance(player))
        {
            new PlotSettingsUI(player, plot);
        }
        else
        {
            new PlotUI(player, plot);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length != 1)
        {
            return List.of();
        }

        if (! (sender instanceof Player player))
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();

        for (Plot plot : Plot.getInstances().stream().filter(p -> p.isMember(player)).toList())
        {
            suggest.add(plot.getName());
        }

        return suggest;
    }
}
