package net.azisaba.vanilife.util;

import net.azisaba.vanilife.plot.Plot;
import org.bukkit.Chunk;
import org.bukkit.World;

public class PlotUtility
{
    public static boolean isAdjacent(Chunk chunk)
    {
        if (chunk == null)
        {
            return false;
        }

        Plot plot = Plot.getInstance(chunk);
        World world = chunk.getWorld();

        int x = chunk.getX();
        int z = chunk.getZ();

        Plot west = Plot.getInstance(world.getChunkAt(x - 1, z));
        Plot east = Plot.getInstance(world.getChunkAt(x + 1, z));
        Plot south = Plot.getInstance(world.getChunkAt(x, z - 1));
        Plot north = Plot.getInstance(world.getChunkAt(x, z + 1));

        return (west != null && west != plot) || (east != null && east != plot) || (south != null && south != plot) || (north != null && north != plot);
    }
}
