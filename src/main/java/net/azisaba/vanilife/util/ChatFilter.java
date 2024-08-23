package net.azisaba.vanilife.util;

import com.google.gson.JsonArray;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.azisaba.vanilife.Vanilife;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class ChatFilter
{
    private final ArrayList<String> filters = new ArrayList<>();

    public ChatFilter()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT string FROM filter");

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                this.filters.add(rs.getString("string"));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get filter record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }

    public ArrayList<String> getFilters()
    {
        return this.filters;
    }

    public boolean filter(String string)
    {
        return this.filters.stream().anyMatch(filter -> string.toLowerCase().contains(filter));
    }

    public void register(String string)
    {
        if (this.filters.contains(string))
        {
            return;
        }

        this.filters.add(string);
        this.upload();
    }

    public void unregister(String string)
    {
        this.filters.remove(string);
        this.upload();
    }

    public JsonArray getAsJsonArray()
    {
        JsonArray jsonArray = new JsonArray();
        this.filters.forEach(jsonArray::add);
        return jsonArray;
    }

    public void onAsyncChat(AsyncChatEvent event)
    {
        Player player = event.getPlayer();
        String msg = ((TextComponent) event.message()).content();

        if (Vanilife.filter.filter(msg))
        {
            EmbedBuilder builder = new EmbedBuilder()
                    .setAuthor(player.getName(), null, String.format("https://api.mineatar.io/face/%s", player.getUniqueId().toString().replace("-", "")))
                    .setTitle("チャットフィルタリング")
                    .setDescription(String.format("%s このチャットはチャットフィルタリングによって不適切と判断されました、ご確認をお願いします", Vanilife.ROLE_SUPPORT))
                    .setFooter(player.getUniqueId().toString())
                    .addField("メッセージ", msg, true)
                    .setColor(new Color(255, 85, 85));

            Vanilife.channel.sendMessageEmbeds(builder.build())
                    .addActionRow(Button.danger("vanilife:mute", String.format("%s をミュートする", player.getName())),
                            Button.secondary("vanilife:unmute", "または…アンミュート")).queue();
            return;
        }

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(player.getName(), null, String.format("https://api.mineatar.io/face/%s", player.getUniqueId().toString().replace("-", "")))
                .setDescription(msg)
                .setFooter(player.getUniqueId().toString())
                .setColor(new Color(85, 255, 85));

        Vanilife.channel.sendMessageEmbeds(builder.build()).queue();
    }

    private void upload()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("DELETE FROM filter");
            stmt.executeUpdate();

            PreparedStatement stmt2 = con.prepareStatement("INSERT INTO filter VALUES(?)");

            for (String filter : this.filters)
            {
                stmt2.setString(1, filter);
                stmt2.executeUpdate();
            }

            stmt2.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to insert filter record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }
}
