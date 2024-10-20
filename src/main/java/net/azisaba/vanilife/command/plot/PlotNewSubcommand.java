package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.PlotSubscription;
import net.azisaba.vanilife.util.PlotUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlotNewSubcommand implements Subcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "new";
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
            sender.sendMessage(Component.text("Correct syntax: //plot new <name>").color(NamedTextColor.RED));
            return;
        }

        if (VanilifeWorld.getInstance(player.getWorld()) == null)
        {
            sender.sendMessage(Language.translate("cmd.plot.invalid-world", player).color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user.getMola() < Vanilife.MOLA_PLOT_NEW)
        {
            sender.sendMessage(Language.translate("msg.shortage", player, "need=" + (Vanilife.MOLA_PLOT_NEW - user.getMola())).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        if (Plot.getInstance(args[0]) != null)
        {
            sender.sendMessage(Language.translate("cmd.plot.new.already", player).color(NamedTextColor.RED));
            return;
        }

        if (PlotUtility.isAdjacent(player.getChunk()))
        {
            sender.sendMessage(Language.translate("cmd.plot.cant-adjacent", player).color(NamedTextColor.RED));
            return;
        }

        if (args[0].length() < 3)
        {
            sender.sendMessage(Language.translate("cmd.plot.new.limit-under", player).color(NamedTextColor.RED));
            return;
        }

        if (16 < args[0].length())
        {
            sender.sendMessage(Language.translate("cmd.plot.new.limit-over", player).color(NamedTextColor.RED));
            return;
        }

        Chunk chunk = player.getChunk();
        int chunkX = chunk.getX() * 16 + 8;
        int chunkZ = chunk.getZ() * 16 + 8;

        if (Math.sqrt(chunkX * chunkX + chunkZ * chunkZ) <= 20)
        {
            sender.sendMessage(Language.translate("cmd.plot.spawn-protection", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        Plot plot = Plot.getInstance(chunk);

        if (plot != null)
        {
            sender.sendMessage(Language.translate("cmd.plot.already", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        plot = new Plot(args[0], player.getChunk());
        plot.setOwner(user);
        user.subscribe(new PlotSubscription(plot));
        user.setMola(user.getMola() - Vanilife.MOLA_PLOT_NEW);
        sender.sendMessage(Language.translate("cmd.plot.new.complete", player, "cost=" + Vanilife.MOLA_PLOT_NEW).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
