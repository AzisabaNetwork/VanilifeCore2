package net.azisaba.vanilife.util;

import com.google.gson.JsonArray;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.chat.DirectChat;
import net.azisaba.vanilife.chat.GroupChat;
import net.azisaba.vanilife.chat.IChat;
import net.azisaba.vanilife.gomenne.Gomenne;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatFilter
{
    private final List<String> filters = new ArrayList<>();

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
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get filter record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }

    public List<String> getFilters()
    {
        return this.filters;
    }

    public boolean filter(@NotNull String string)
    {
        return this.filters.stream().anyMatch(filter -> string.toLowerCase().contains(filter));
    }

    public void register(@NotNull String string)
    {
        if (this.filters.contains(string))
        {
            return;
        }

        this.filters.add(string);
        this.upload();
    }

    public void unregister(@NotNull String string)
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

    public void onChat(@NotNull Player sender, @NotNull String message, IChat chat)
    {
        final String field = chat == null ? "全体" :
                chat instanceof GroupChat group ? String.format("%s (%s)", group.getName(), group.getId()) :
                        chat instanceof DirectChat direct ? String.format("DM %s (%s)", direct.getPartner(sender).getPlaneName(), direct.getPartner(sender).getId()) :
                                "不明";

        final String compiled = ! Gomenne.isValid(sender, message) ? message : String.format("%s (%s)", message, Gomenne.convert(Gomenne.hira(message)));

        if (UserUtility.isModerator(sender) || ! Vanilife.filter.filter(message))
        {
            Vanilife.CHANNEL_HISTORY.sendMessage(String.format("**[%s] %s (%s)**: %s", field, sender.getName(), sender.getUniqueId(), compiled)).queue();

            return;
        }

        Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                        .setAuthor(sender.getName(), null, String.format("https://api.mineatar.io/face/%s", sender.getUniqueId().toString().replace("-", "")))
                        .setTitle(":shield:チャットフィルタリング")
                        .setDescription(compiled)
                        .setFooter(sender.getUniqueId().toString())
                        .setColor(new Color(255, 85, 85)).build())
                .addActionRow(Button.danger("vanilife:mute", String.format("%s をミュートする", sender.getName())),
                        Button.secondary("vanilife:unmute", "または…アンミュート")).queue();

        Vanilife.CHANNEL_CONSOLE.sendMessage(":envelope_with_arrow: " + Vanilife.ROLE_SUPPORT.getAsMention() + " このチャットはチャットフィルタリングによって不適切と判断されました、ご確認をお願いします").queue();
    }

    public void onChat(@NotNull Player sender, @NotNull String message)
    {
        this.onChat(sender, message, null);
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
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to insert filter record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }
}
