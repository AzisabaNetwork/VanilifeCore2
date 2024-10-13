package net.azisaba.vanilife.gomenne;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

public class ConvertRequest
{
    private static final List<ConvertRequest> instances = new ArrayList<>();

    public static List<ConvertRequest> getInstances()
    {
        return ConvertRequest.instances;
    }

    public static void mount()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM imereq");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                new ConvertRequest(UUID.fromString(rs.getString("id")));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text("Failed to mount ime requests: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    private final UUID id;

    private User sender;
    private Date date;

    private String yomi;
    private String kaki;

    private ConvertRequest(@NotNull UUID id)
    {
        this.id = id;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM imereq WHERE id = ?");
            stmt.setString(1, this.id.toString());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            this.sender = User.getInstance(UUID.fromString(rs.getString("sender")));
            this.date = Vanilife.sdf2.parse(rs.getString("date"));
            this.yomi = rs.getString("yomi");
            this.kaki = rs.getString("kaki");

            rs.close();
            con.close();

            ConvertRequest.instances.add(this);
        }
        catch (SQLException | ParseException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text("Failed to get imereq record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public ConvertRequest(@NotNull User sender, @NotNull String yomi, @NotNull String kaki)
    {
        this.id = UUID.randomUUID();
        this.sender = sender;
        this.date = new Date();
        this.yomi = yomi;
        this.kaki = kaki;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO imereq VALUES(?, ?, ?, ?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, this.sender.getId().toString());
            stmt.setString(3, Vanilife.sdf2.format(this.date));
            stmt.setString(4, this.yomi);
            stmt.setString(5, this.kaki);

            stmt.executeUpdate();

            stmt.close();
            con.close();

            ConvertRequest.instances.add(this);
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text("Failed to insert imereq record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull UUID getId()
    {
        return this.id;
    }

    public @NotNull User getSender()
    {
        return this.sender;
    }

    public @NotNull Date getDate()
    {
        return this.date;
    }

    public @NotNull String getYomi()
    {
        return this.yomi;
    }

    public @NotNull String getKaki()
    {
        return this.kaki;
    }

    public void accept()
    {
        Gomenne.register(this.yomi, this.kaki);
        Vanilife.CHANNEL_ANNOUNCE.sendMessage(String.format(":printer: 変換リクエストから `%s (%s)` が IME 辞書に追加されました！", this.kaki, this.yomi)).queue();
        this.delete("追加 (3 Mola を付与しました)");
        this.sender.setMola(this.sender.getMola() + 3);
    }

    public void reject()
    {
        this.delete("追加見送り");
    }

    private void delete(@NotNull String result)
    {
        ConvertRequest.instances.remove(this);

        this.sender.sendNotice("[通知] 変換リクエストの審査結果のお知らせ", "平素より格別のお引き立てを賜り厚くお礼申し上げます。" +
                "\nさて、先日頂きました変換リクエストについて、審査の結果が出ましたので下記の通りにお知らせいたします。" +
                "\n\n審査結果: " + result +
                "\nよみ: " + this.yomi +
                "\nかき: " + this.kaki +
                "\n\n今度ともどうぞアジ鯖にご支援を賜りますようお願い申し上げます。");

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM imereq WHERE id = ?");
            stmt.setString(1, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text("Failed to delete an ime request.").color(NamedTextColor.RED));
        }
    }
}
