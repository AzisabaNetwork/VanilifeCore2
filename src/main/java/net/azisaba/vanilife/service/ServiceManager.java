package net.azisaba.vanilife.service;

import net.azisaba.vanilife.service.schedule.IServiceSchedule;
import net.azisaba.vanilife.service.schedule.MatchSchedule;
import net.azisaba.vanilife.util.ResourceUtility;
import org.bukkit.configuration.ConfigurationSection;

import java.time.LocalDateTime;
import java.util.HashMap;

public class ServiceManager
{
    public static final ConfigurationSection config = ResourceUtility.getYamlResource("service.yml");
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
        ServiceManager.config.getKeys(false).forEach(Service::new);
    }
}
