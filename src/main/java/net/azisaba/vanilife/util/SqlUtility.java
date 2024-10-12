package net.azisaba.vanilife.util;

import net.azisaba.vanilife.Vanilife;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SqlUtility
{
    public static void setup()
    {
        Map<String, String> tablemap = new HashMap<>();

        tablemap.put("user", "CREATE TABLE user(id VARCHAR(36) PRIMARY KEY, nick VARCHAR(16), bio VARCHAR(120), birthday VARCHAR(16), youtube VARCHAR(30), twitter VARCHAR(15), discord VARCHAR(19), mola INT UNSIGNED, trust INT UNSIGNED, sara VARCHAR(16), status VARCHAR(16), skin VARCHAR(16), chat VARCHAR(36), osatou VARCHAR(36), storage TEXT);");
        tablemap.put("skin", "CREATE TABLE skin(id VARCHAR(36) PRIMARY KEY, name VARCHAR(8), owner VARCHAR(36), value TEXT, signature TEXT)");
        tablemap.put("login", "CREATE TABLE login(user VARCHAR(36) PRIMARY KEY, login VARCHAR(16), streak INT UNSIGNED)");
        tablemap.put("achievement", "CREATE TABLE achievement(user VARCHAR(36), objective VARCHAR(64))");
        tablemap.put("subscription", "CREATE TABLE subscription(user VARCHAR(36), subscription VARCHAR(16))");
        tablemap.put("housing", "CREATE TABLE housing(id VARCHAR(36) PRIMARY KEY, pos1_x INT, pos1_y INT, pos1_z INT, pos2_x INT, pos2_y INT, pos2_z INT, x INT, y INT, z INT)");

        tablemap.put("friend", "CREATE TABLE friend(user1 VARCHAR(36), user2 VARCHAR(36))");
        tablemap.put("block", "CREATE TABLE block(user1 VARCHAR(36), user2 VARCHAR(36))");

        tablemap.put("chat", "CREATE TABLE chat(id VARCHAR(36) PRIMARY KEY, name VARCHAR(16) UNIQUE, owner VARCHAR(36), scope VARCHAR(16), color INT)");
        tablemap.put("chat-member", "CREATE TABLE `chat-member`(user VARCHAR(36), chat VARCHAR(36))");

        tablemap.put("plot", "CREATE TABLE plot(id VARCHAR(36) PRIMARY KEY, name VARCHAR(16) UNIQUE, owner VARCHAR(36), readme VARCHAR(36), scope VARCHAR(16), edit BOOLEAN, chest BOOLEAN, pvp BOOLEAN, world TEXT, x INT, y INT, z INT)");
        tablemap.put("plot-member", "CREATE TABLE `plot-member`(user VARCHAR(36), plot VARCHAR(36))");
        tablemap.put("plot-chunk", "CREATE TABLE `plot-chunk`(plot VARCHAR(36), x INT, z INT)");

        tablemap.put("mail", "CREATE TABLE mail(id VARCHAR(36) PRIMARY KEY, user_from VARCHAR(36), user_to VARCHAR(36), subject TEXT, date VARCHAR(16), message TEXT, readed BOOLEAN)");
        tablemap.put("report", "CREATE TABLE report(id VARCHAR(36) PRIMARY KEY, sender VARCHAR(36), details VARCHAR(250), world VARCHAR(64), x INT, y INT, z INT, date VARCHAR(16), controller VARCHAR(19), supported BOOLEAN)");
        tablemap.put("imereq", "CREATE TABLE imereq(id VARCHAR(36) PRIMARY KEY, sender VARCHAR(36), date VARCHAR(16), yomi VARCHAR(16), kaki VARCHAR(16))");
        tablemap.put("filter", "CREATE TABLE filter(string VARCHAR(16))");

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            for (Map.Entry<String, String> table : tablemap.entrySet())
            {
                if (SqlUtility.exists(table.getKey()))
                {
                    continue;
                }

                Statement stmt = con.createStatement();
                stmt.executeUpdate(table.getValue());

                stmt.close();

                Vanilife.getPluginLogger().info(Component.text("Solved the table \"" + table.getKey() + "\".").color(NamedTextColor.GREEN));
            }

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to setup tables: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public static boolean jdbc(@NotNull String clazz)
    {
        try
        {
            Class.forName(clazz);
            return true;
        }
        catch (ClassNotFoundException e)
        {
            Vanilife.sendExceptionReport(e);
            return false;
        }
    }

    public static boolean exists(@NotNull String table)
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            DatabaseMetaData meta = con.getMetaData();
            ResultSet rs = meta.getTables(null, null, table, new String[]{"TABLE"});

            boolean exists = rs.next();

            rs.close();
            con.close();

            return exists;
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            return false;
        }
    }
}
