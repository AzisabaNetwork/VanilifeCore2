package net.azisaba.vanilife.user.settings;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.settings.setting.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Settings
{
    private static final ArrayList<Settings> instances = new ArrayList<>();

    public static Settings getInstance(User user)
    {
        ArrayList<Settings> filteredInstances = new ArrayList<>(Settings.instances.stream().filter(i -> i.getUser() == user).toList());
        return filteredInstances.isEmpty() ? new Settings(user) : filteredInstances.getFirst();
    }

    private final User user;

    public final MailSetting mailSetting;
    public final BioSetting bioSetting;
    public final BirthdaySetting birthdaySetting;
    public final OhatuSetting ohatuSetting;
    public final MetubouSetting metubouSetting;
    public final YouTubeSetting youtubeSetting;
    public final TwitterSetting twitterSetting;
    public final DiscordSetting discordSetting;
    public final PreviewSetting previewSetting;

    private final ArrayList<ISetting> settings = new ArrayList<>();

    private Settings(User user)
    {
        this.user = user;

        this.mailSetting = (MailSetting) this.registerSetting(new MailSetting());
        this.bioSetting = (BioSetting) this.registerSetting(new BioSetting());
        this.birthdaySetting = (BirthdaySetting) this.registerSetting(new BirthdaySetting());
        this.ohatuSetting = (OhatuSetting) this.registerSetting(new OhatuSetting());
        this.metubouSetting = (MetubouSetting) this.registerSetting(new MetubouSetting());
        this.youtubeSetting = (YouTubeSetting) this.registerSetting(new YouTubeSetting());
        this.twitterSetting = (TwitterSetting) this.registerSetting(new TwitterSetting());
        this.discordSetting = (DiscordSetting) this.registerSetting(new DiscordSetting());
        this.previewSetting = (PreviewSetting) this.registerSetting(new PreviewSetting());

        Settings.instances.add(this);
    }

    public User getUser()
    {
        return this.user;
    }

    public ArrayList<ISetting> getSettings()
    {
        return this.settings;
    }

    public ISetting registerSetting(ISetting setting)
    {
        setting.init(this.user);
        this.settings.add(setting);
        return setting;
    }

    public static class Builder
    {
        public static Settings build(User user)
        {
            try
            {
                Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
                PreparedStatement stmt = con.prepareStatement("INSERT INTO settings VALUES(?, ?, 0, 0, 0, ?, ?, ?, ?)");
                stmt.setString(1, user.getId().toString());
                stmt.setString(2, AbstractScopeSetting.Scope.PUBLIC.toString());
                stmt.setString(3, AbstractScopeSetting.Scope.PUBLIC.toString());
                stmt.setString(4, AbstractScopeSetting.Scope.PUBLIC.toString());
                stmt.setString(5, AbstractScopeSetting.Scope.PUBLIC.toString());
                stmt.setString(6, AbstractScopeSetting.Scope.PUBLIC.toString());

                stmt.executeUpdate();

                stmt.close();
                con.close();
            }
            catch (SQLException e)
            {
                Vanilife.getPluginLogger().error(Component.text(String.format("Failed to insert settings record: %s", e.getMessage())).color(NamedTextColor.RED));
            }

            return Settings.getInstance(user);
        }
    }
}
