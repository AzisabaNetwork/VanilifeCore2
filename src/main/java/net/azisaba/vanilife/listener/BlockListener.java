package net.azisaba.vanilife.listener;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Materials;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener
{
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        Plot plot = Plot.getInstance(block.getLocation().getChunk());

        if (plot != null)
        {
            plot.onBlockBreak(event);
        }

        Player player = event.getPlayer();

        if (! event.isCancelled() && player.getGameMode() == GameMode.SURVIVAL)
        {
            User user = User.getInstance(player);

            if (Materials.MINING.contains(block.getType()) && Vanilife.random.nextDouble() < (Materials.ORES.contains(block.getType()) ? 0.24 : 0.1))
            {
                user.setMola(user.getMola() + (Materials.ORES.contains(block.getType()) ? 2 : 1), "Mining");
            }

            if (Materials.LOGGING.contains(block.getType()) && Vanilife.random.nextDouble() < 0.2)
            {
                user.setMola(user.getMola() + 3, "Logging");
            }

            if (Materials.SEICHI.contains(block.getType()) && Vanilife.random.nextDouble() < 0.05)
            {
                user.setMola(user.getMola() + 4, "Seichi");
            }

            if (Materials.FARMING.contains(block.getType()) && Vanilife.random.nextDouble() < 0.1)
            {
                user.setMola(user.getMola() + 2, "Farming");
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Plot plot = Plot.getInstance(event.getBlock().getLocation().getChunk());

        if (plot != null)
        {
            plot.onBlockPlace(event);
        }
    }
}
