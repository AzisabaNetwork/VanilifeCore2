package net.azisaba.vanilife.service;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.util.ResourceUtility;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Service
{
    private static final List<Service> instances = new ArrayList<>();

    public static Service getInstance(@NotNull String name)
    {
        List<Service> filteredInstances = Service.instances.stream().filter(i -> i.name().equals(name)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static List<Service> getInstances()
    {
        return Service.instances;
    }

    public static void mount()
    {
        File directory = new File(Vanilife.getPlugin().getDataFolder(), "/service");

        if (! directory.exists())
        {
            directory.mkdirs();
        }

        File[] services = directory.listFiles();

        if (services == null)
        {
            return;
        }

        for (File service : services)
        {
            if (! service.isFile())
            {
                continue;
            }

            new Service(service.getName()).start();
        }
    }

    private final String name;

    private final List<String> script;

    private final List<Schedule> schedules = new ArrayList<>();

    public Service(@NotNull String name)
    {
        this.name = name;

        YamlConfiguration source = ResourceUtility.getYamlResource("/service/" + name);

        this.script = source.getStringList("script");

        for (String schedule : source.getConfigurationSection("schedule").getKeys(false))
        {
            ConfigurationSection s = source.getConfigurationSection("schedule." + schedule);
            this.schedules.add(new Schedule(this, s));
        }

        Service.instances.add(this);
    }

    public String name()
    {
        return this.name;
    }

    public void start()
    {
        this.schedules.forEach(Schedule::start);
    }

    public void stop()
    {
        this.schedules.forEach(Schedule::stop);
    }

    public void run()
    {
        this.script.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }
}
