package net.azisaba.vanilife.util;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.subscription.PlotSubscription;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.sql.*;
import java.util.UUID;

@Utility
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

    public static void mount()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM plot");

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                if (Bukkit.getWorld(rs.getString("world")) != null)
                {
                    Plot instance = new Plot(UUID.fromString(rs.getString("id")));
                    instance.getOwner().subscribe(new PlotSubscription(instance));
                }
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to mount plot: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }
}
