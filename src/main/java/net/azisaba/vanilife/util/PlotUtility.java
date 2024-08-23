package net.azisaba.vanilife.util;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

@Utility
public class PlotUtility
{
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
                    new Plot(UUID.fromString(rs.getString("id")));
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
