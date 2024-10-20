package net.azisaba.vanilife.aww;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.DomainSubscription;
import net.azisaba.vanilife.util.ResourceUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

public class Domain
{
    private static final List<Domain> instances = new ArrayList<>();

    public static Domain getInstance(@NotNull String name)
    {
        return Domain.instances.stream().filter(i -> i.getAbsoluteName().equals(name.toLowerCase())).findFirst().orElse(null);
    }

    public static boolean isValid(@NotNull String domain)
    {
        String[] levels = domain.split("\\.");

        for (String level : levels)
        {
            if (! Vanilife.pattern2.matcher(level).matches())
            {
                return false;
            }
        }

        return true;
    }

    public static @NotNull String parent(@NotNull String s)
    {
        String[] levels = s.split("\\.");
        return String.join(".", Arrays.copyOf(levels, levels.length - 1));
    }

    public static @NotNull String topLevel(@NotNull String s)
    {
        String[] levels = s.split("\\.");
        return levels[levels.length - 1];
    }

    public static @NotNull String reverse(@NotNull String s)
    {
        String[] levels = s.toLowerCase().split("\\.");
        Collections.reverse(Arrays.asList(levels));
        return String.join(".", levels);
    }

    public static @NotNull String registry(@NotNull String s)
    {
        String[] levels = s.split("\\.");
        return String.join(".", Arrays.copyOf(levels, levels.length - 1));
    }

    public static void mount()
    {
        List<String> domains = new ArrayList<>();

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT name FROM domain");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                domains.add(rs.getString("name"));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to get domain records: " + e.getMessage()).color(NamedTextColor.RED));
        }

        domains.sort(new Comparator<>()
        {
            @Override
            public int compare(String s1, String s2)
            {
                return this.count(s1) - this.count(s2);
            }

            private int count(String s)
            {
                return (int) s.chars().filter(c -> c == '.').count();
            }
        });

        for (String domain : domains)
        {
            new Domain(domain);
        }
    }

    public static void register(@NotNull String name, @NotNull User registrant)
    {
        name = name.toLowerCase();

        if (Domain.getInstance(name) != null)
        {
            return;
        }

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO domain VALUES(?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, registrant.getId().toString());
            stmt.setString(3, Vanilife.sdf2.format(new Date()));

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to insert domain record: " + e.getMessage()).color(NamedTextColor.RED));
        }

        Domain domain = new Domain(name);

        if (! domain.getDirectory().exists() && ! domain.getDirectory().mkdirs())
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to make domain directory: " + domain.getDirectory().getAbsolutePath()).color(NamedTextColor.RED));
        }
    }

    private final String name;
    private final Domain registry;

    private User registrant;
    private Date date;

    private final File directory;

    private final List<Domain> children = new ArrayList<>();
    private final List<WebPage> pages = new ArrayList<>();

    private Domain(@NotNull String name)
    {
        name = name.toLowerCase();
        String[] levels = name.split("\\.");

        this.name = 1 < levels.length ? levels[levels.length - 1] : name;
        this.registry = 1 < levels.length ? Domain.getInstance(String.join(".", Arrays.copyOf(levels, levels.length - 1))) : null;

        if (this.registry != null)
        {
            this.registry.children.add(this);
        }

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM domain WHERE name = ?");
            stmt.setString(1, this.getAbsoluteName());
            ResultSet rs = stmt.executeQuery();

            rs.next();

            this.registrant = User.getInstance(UUID.fromString(rs.getString("registrant")));
            this.date = Vanilife.sdf2.parse(rs.getString("date"));

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException | ParseException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to get domain record: " + e.getMessage()).color(NamedTextColor.RED));
        }

        this.directory = this.registry == null ? ResourceUtility.getResource("/aww/" + this.name) : new File(this.registry.getDirectory(), this.name);

        this.registrant.addDomain(this);

        if (! this.isTopLevel())
        {
            this.registrant.getSubscriptions().add(new DomainSubscription(this));
        }

        File[] pages = this.directory.listFiles();

        if (pages != null)
        {
            for (File page : pages)
            {
                if (! page.isFile())
                {
                    continue;
                }

                this.pages.add(new WebPage(this, page.getName()));
            }
        }

        Domain.instances.add(this);
    }

    public @NotNull String getName()
    {
        return this.name;
    }

    public @NotNull String getAbsoluteName()
    {
        return this.registry == null ? this.name : this.registry.getAbsoluteName() + "." + this.name;
    }

    public @NotNull String getUrl()
    {
        return Domain.reverse(this.getAbsoluteName());
    }

    public Domain getRegistry()
    {
        return this.registry;
    }

    public @NotNull User getRegistrant()
    {
        return this.registrant;
    }

    public void setRegistrant(@NotNull User registrant)
    {
        if (this.registrant == registrant)
        {
            return;
        }

        this.registrant.removeDomain(this);
        this.registrant = registrant;
        this.registrant.addDomain(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE domain SET registrant = ? WHERE name = ?");
            stmt.setString(1, this.getAbsoluteName());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update domain record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull Date getDate()
    {
        return this.date;
    }

    public @NotNull File getDirectory()
    {
        return this.directory;
    }

    public WebPage getPage(@NotNull String name)
    {
        return this.pages.stream().filter(page -> page.getName().equals(name)).findFirst().orElse(null);
    }

    public @NotNull List<Domain> getChildren()
    {
        return this.children;
    }

    public @NotNull List<WebPage> getPages()
    {
        return this.pages;
    }

    public void addPage(@NotNull WebPage page)
    {
        this.pages.add(page);
    }

    public void removePage(@NotNull WebPage page)
    {
        this.pages.remove(page);
        page.delete();
    }

    public boolean isTopLevel()
    {
        return this.registry == null;
    }

    public void delete()
    {
        Domain.instances.remove(this);
        this.registrant.removeDomain(this);

        this.children.forEach(Domain::delete);

        if (! this.directory.delete())
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to delete domain directory: " + this.directory.getAbsolutePath()).color(NamedTextColor.RED));
        }

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM domain WHERE name = ?");
            stmt.setString(1, this.getAbsoluteName());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to delete domain record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }
}
