package net.azisaba.vanilife.user.settings;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.settings.setting.*;
import org.jetbrains.annotations.NotNull;

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

    public final BioSetting BIO;
    public final BirthdaySetting BIRTHDAY;
    public final YouTubeSetting YOUTUBE;
    public final TwitterSetting TWITTER;
    public final DiscordSetting DISCORD;
    public final MetubouSetting METUBOU;
    public final LanguageSetting LANGUAGE;

    private final ArrayList<ISetting<?>> settings = new ArrayList<>();

    private Settings(@NotNull User user)
    {
        this.user = user;

        this.BIO = (BioSetting) this.register(new BioSetting(this.user));
        this.BIRTHDAY = (BirthdaySetting) this.register(new BirthdaySetting(this.user));
        this.YOUTUBE = (YouTubeSetting) this.register(new YouTubeSetting(this.user));
        this.TWITTER = (TwitterSetting) this.register(new TwitterSetting(this.user));
        this.DISCORD = (DiscordSetting) this.register(new DiscordSetting(this.user));
        this.METUBOU = (MetubouSetting) this.register(new MetubouSetting(this.user));
        this.LANGUAGE = (LanguageSetting) this.register(new LanguageSetting(this.user));

        Settings.instances.add(this);
    }

    @NotNull
    public User getUser()
    {
        return this.user;
    }

    @NotNull
    public ArrayList<ISetting<?>> getSettings()
    {
        return this.settings;
    }

    @NotNull
    public ISetting<?> register(@NotNull ISetting<?> setting)
    {
        this.settings.add(setting);
        return setting;
    }
}
