package net.azisaba.vanilife.command;

import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PtpCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /" + label + " <plot>").color(NamedTextColor.RED));
            return true;
        }

        Plot plot = Plot.getInstance(args[0]);

        if (plot == null)
        {
            sender.sendMessage(Language.translate("cmd.plot.not-found", player).color(NamedTextColor.RED));
            return true;
        }

        if (! plot.isMember(player))
        {
            sender.sendMessage(Language.translate("cmd.ptp.permission-error", player));
            return true;
        }

        player.teleport(plot.getSpawn());
        player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.2f);
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
