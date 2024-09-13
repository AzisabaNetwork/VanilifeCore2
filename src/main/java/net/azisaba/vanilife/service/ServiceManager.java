package net.azisaba.vanilife.service;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.service.schedule.IServiceSchedule;
import net.azisaba.vanilife.service.schedule.MatchSchedule;
import net.azisaba.vanilife.util.ResourceUtility;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ServiceManager
{
    public static final HashMap<String, IServiceSchedule> schedules = new HashMap<>();

    static
    {
        ServiceManager.registerSchedule(new MatchSchedule());
    }

    public static void registerSchedule(IServiceSchedule schedule)
    {
        ServiceManager.schedules.put(schedule.getType(), schedule);
    }

    public static String compileScript(String script)
    {
        LocalDateTime date = LocalDateTime.now();
        script = script.replace("${year}", String.valueOf(date.getYear()));
        script = script.replace("${month}", String.valueOf(date.getMonth()));
        script = script.replace("${day}", String.valueOf(date.getDayOfMonth()));
        script = script.replace("${hour}", String.valueOf(date.getHour()));
        script = script.replace("${minutes}", String.valueOf(date.getMinute()));
        script = script.replace("${second}", String.valueOf(date.getSecond()));

        return script;
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

            new Service(service.getName());
        }
    }
}
