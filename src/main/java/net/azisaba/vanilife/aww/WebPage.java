package net.azisaba.vanilife.aww;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.subscription.AzireSubscription;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WebPage
{
    private final Domain domain;
    private String name;

    private final AzireSubscription subscription;

    private final Map<Integer, WebElement> elements = new HashMap<>();

    public WebPage(@NotNull Domain domain, @NotNull String name)
    {
        this.domain = domain;
        this.name = name;

        this.subscription = new AzireSubscription(this);
        this.domain.getRegistrant().getSubscriptions().add(this.subscription);

        File sourcefile = new File(this.domain.getDirectory(), this.name);

        try
        {
            if (! sourcefile.exists() && ! sourcefile.createNewFile())
            {
                throw new RuntimeException(sourcefile.getAbsolutePath());
            }
        } catch (IOException | RuntimeException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to create source file: " + e.getMessage()).color(NamedTextColor.RED));
        }

        YamlConfiguration source = YamlConfiguration.loadConfiguration(new File(this.domain.getDirectory(), this.name));

        for (String key : source.getKeys(false))
        {
            this.elements.put(Integer.parseInt(key), new WebElement(this, Objects.requireNonNull(source.getConfigurationSection(key))));
        }
    }

    public @NotNull Domain getDomain()
    {
        return this.domain;
    }

    public @NotNull String getName()
    {
        return this.name;
    }

    public void setName(@NotNull String name)
    {
        File sourcefile = new File(this.domain.getDirectory(), this.name);

        if (! sourcefile.renameTo(new File(this.domain.getDirectory(), name)))
        {
            Vanilife.getPluginLogger().error(Component.text("Failed to rename web page: " + sourcefile.getAbsolutePath()).color(NamedTextColor.RED));
            return;
        }

        this.name = name;
    }

    public @NotNull String getUrl()
    {
        return this.domain.getUrl() + "/" + this.name;
    }

    public WebElement getElement(int slot)
    {
        return this.elements.get(slot);
    }

    public void addElement(int slot, @NotNull WebElement element)
    {
        this.elements.put(slot, element);
        this.export();
    }

    public void removeElement(int slot)
    {
        this.elements.remove(slot);
        this.export();
    }

    public void export()
    {
        YamlConfiguration source = new YamlConfiguration();
        this.elements.forEach((key, value) -> source.set(String.valueOf(key), value.source()));

        try
        {
            source.save(new File(this.domain.getDirectory(), this.name));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected void delete()
    {
        File source = new File(this.domain.getDirectory(), this.name);
        this.domain.getRegistrant().getSubscriptions().remove(this.subscription);

        if (! source.delete())
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to delete web page: " + source.getAbsolutePath()));
        }
    }
}
