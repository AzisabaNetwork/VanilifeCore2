package net.azisaba.vanilife.service;

import net.azisaba.vanilife.service.schedule.IServiceSchedule;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;

public class Service
{
    private static final ArrayList<Service> instances = new ArrayList<>();

    public static Service getInstance(String name)
    {
        ArrayList<Service> filteredInstances = new ArrayList<>(Service.instances.stream().filter(i -> i.getName().equals(name)).toList());
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static ArrayList<Service> getInstances()
    {
        return Service.instances;
    }

    private final String name;

    private final ConfigurationSection parameters;
    private final ArrayList<String> script = new ArrayList<>();
    public final ArrayList<IServiceSchedule> schedules = new ArrayList<>();
    private boolean stopped = false;

    public Service(String name)
    {
        this.name = name;
        this.parameters = ServiceManager.config.getConfigurationSection(this.name);
        this.script.addAll(new ArrayList<>(this.parameters.getStringList("script")));

        for (String scheduleName : this.parameters.getConfigurationSection("schedule").getKeys(false))
        {
            ConfigurationSection config = this.parameters.getConfigurationSection(String.format("schedule.%s", scheduleName));
            ServiceManager.schedules.get(config.getString(String.format("type", this.name))).start(this, config);
        }

        Service.instances.add(this);
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isStopped()
    {
        return this.stopped;
    }

    public void run()
    {
        if (this.stopped)
        {
            return;
        }

        this.script.forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ServiceManager.compileScript(s)));
    }

    public void stop()
    {
        Service.instances.remove(this);
    }
}
