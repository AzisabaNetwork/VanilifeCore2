package net.azisaba.vanilife.user.referral;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.runnable.ReferralRunnable;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

public class Referral
{
    private static final List<Referral> instances = new ArrayList<>();

    public static Referral getInstance(@NotNull String token)
    {
        return Referral.instances.stream().filter(i -> i.getToken().equals(token)).findFirst().orElse(null);
    }

    public static Referral getInstance(@NotNull User referrer, @NotNull UUID referee)
    {
        return Referral.instances.stream().filter(i -> i.getReferrer() == referrer && i.getReferee().equals(referee)).findFirst().orElse(null);
    }

    public static @NotNull List<Referral> getInstances(@NotNull User referrer)
    {
        return Referral.instances.stream().filter(i -> i.getReferrer() == referrer).toList();
    }

    public static @NotNull List<Referral> getInstances(@NotNull UUID referee)
    {
        return Referral.instances.stream().filter(i -> i.getReferee().equals(referee)).toList();
    }

    public static List<Referral> getInstances()
    {
        return new ArrayList<>(Referral.instances);
    }

    public static void mount()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT token FROM referral");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                new Referral(rs.getString("token"));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to mount referrals: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    private static @NotNull String token()
    {
        String characters = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnoprstuvwxyz12345678";
        StringBuilder sb;

        do
        {
            sb = new StringBuilder();

            for (int i = 0; i < 6; i ++)
            {
                sb.append(characters.charAt(Vanilife.random.nextInt(characters.length())));
            }
        }
        while (Referral.getInstance(sb.toString()) != null);

        return sb.toString();
    }

    static
    {
        new ReferralRunnable().runTaskTimerAsynchronously(Vanilife.getPlugin(), 0L, 20L * 60);
    }

    private final String token;
    private User referrer;
    private UUID referee;
    private Date expiration;

    public Referral(@NotNull String token)
    {
        this.token = token;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM referral WHERE token = ?");
            stmt.setString(1, this.token);
            ResultSet rs = stmt.executeQuery();

            rs.next();

            this.referrer = User.getInstance(UUID.fromString(rs.getString("referrer")));
            this.referee = UUID.fromString(rs.getString("referee"));
            this.expiration = Vanilife.sdf2.parse(rs.getString("expiration"));

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException | ParseException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to get referral record: " + e.getMessage()).color(NamedTextColor.RED));
        }

        Referral.instances.add(this);
    }

    public Referral(@NotNull User referrer, @NotNull UUID referee)
    {
        this.token = Referral.token();
        this.referrer = referrer;
        this.referee = referee;

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 3);
        this.expiration = c.getTime();

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO referral VALUES(?, ?, ?, ?)");
            stmt.setString(1, this.token);
            stmt.setString(2, this.referrer.getId().toString());
            stmt.setString(3, this.referee.toString());
            stmt.setString(4, Vanilife.sdf2.format(this.expiration));

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to insert referral record: " + e.getMessage()).color(NamedTextColor.RED));
        }

        Referral.instances.add(this);
    }

    public @NotNull String getToken()
    {
        return this.token;
    }

    public @NotNull User getReferrer()
    {
        return this.referrer;
    }

    public @NotNull UUID getReferee()
    {
        return this.referee;
    }

    public @NotNull Date getExpiration()
    {
        return this.expiration;
    }

    public void use()
    {
        this.referrer.setMola(this.referrer.getMola() + 400);
        User referee = User.getInstance(this.referee);
        referee.setMola(referee.getMola() + 100);

        this.referrer.sendNotification("紹介報酬を受け取りました！",
                referee.getPlaneName() + " さんに発行した紹介コードが使用されたため、400 Mola が付与されました！\nこの度はご紹介、ありがとうございました。");

        new ArrayList<>(Referral.instances).stream()
                .filter(ref -> ref.getReferee().equals(this.referee))
                .forEach(Referral::delete);
    }

    public void delete()
    {
        Referral.instances.remove(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM referral WHERE token = ?");
            stmt.setString(1, this.token);

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to delete referral record: " + e.getMessage()));
        }
    }
}
