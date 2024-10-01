package net.azisaba.vanilife.util;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.report.Report;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.sql.*;
import java.util.UUID;

public class ReportUtility
{
    public static void mount()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM report");

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Report.getInstance(UUID.fromString(rs.getString("id")));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to mount report: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }
}
