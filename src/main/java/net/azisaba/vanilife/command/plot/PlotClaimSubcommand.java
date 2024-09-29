package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlotClaimSubcommand implements ISubcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "claim";
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

        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: //plot claim").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user.getMola() < Vanilife.MOLA_PLOT_CLAIM)
        {
            sender.sendMessage(Language.translate("msg.shortage", player, "need=" + (Vanilife.MOLA_PLOT_CLAIM - user.getMola())).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            return;
        }

        Chunk chunk = player.getChunk();
        int chunkX = chunk.getX() * 16 + 8;
        int chunkZ = chunk.getZ() * 16 + 8;

        if (Math.sqrt(chunkX * chunkX + chunkZ * chunkZ) <= 100)
        {
            sender.sendMessage(Language.translate("cmd.plot.spawn-protection", player).color(NamedTextColor.RED));
            return;
        }

        if (Plot.getInstance(chunk) != null)
        {
            sender.sendMessage(Language.translate("cmd.plot.already", player).color(NamedTextColor.RED));
            return;
        }

        Plot plot = null;
        int i = 0;

        for (int x = -1; x <= 1; x ++)
        {
            for (int z = -1; z <= 1; z ++)
            {
                Plot plot2 = Plot.getInstance(chunk.getWorld().getChunkAt(chunk.getX() + x, chunk.getZ() + z));

                if (plot2 != null)
                {
                   plot = (plot == null) ? plot2 : plot;

                   if (plot != plot2)
                   {
                       i ++;
                   }
                }
            }
        }

        if (plot == null)
        {
            sender.sendMessage(Language.translate("cmd.plot.claim.not-found", player).color(NamedTextColor.RED));
            return;
        }

        if (User.getInstance(player) != plot.getOwner())
        {
            sender.sendMessage(Language.translate("cmd.plot.permission-error", player).color(NamedTextColor.RED));
            return;
        }

        if (1 < i)
        {
            sender.sendMessage(Language.translate("cmd.plot.claim.cant-adjacent", player).color(NamedTextColor.RED));
            return;
        }

        plot.claim(chunk);
        user.setMola(user.getMola() - Vanilife.MOLA_PLOT_CLAIM);
        sender.sendMessage(Language.translate("cmd.plot.claim.complete", player, "cost=" + Vanilife.MOLA_PLOT_CLAIM).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
