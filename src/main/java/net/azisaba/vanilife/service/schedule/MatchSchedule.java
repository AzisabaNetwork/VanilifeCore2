package net.azisaba.vanilife.service.schedule;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.service.Service;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class MatchSchedule implements IServiceSchedule
{
    public static long getLaterTicks(int year, int month, int day, int hour, int minutes, int second)
    {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime match = ZonedDateTime.now(ZoneId.systemDefault());

        match = match.withYear((year < 0) ? now.getYear() : year);
        match = match.withMonth((month < 0) ? now.getMonthValue() : month);
        match = match.withDayOfMonth((day < 0) ? now.getDayOfMonth() : day);
        match = match.withHour((hour < 0) ? now.getHour() : hour);
        match = match.withMinute((minutes < 0) ? now.getMinute() : minutes);
        match = match.withSecond((second < 0) ? now.getSecond() : second);
        match = match.withNano(0);

        if (match.isBefore(now) && year < 0 && match.getMonthValue() < now.getMonthValue())
        {
            match = match.plusYears(1);
        }
        else if (match.isBefore(now) && month < 0 && match.getDayOfMonth() < now.getDayOfMonth())
        {
            match = match.plusMonths(1);
        }
        else if (match.isBefore(now) && day < 0 && match.getHour() < now.getHour())
        {
            match = match.plusDays(1);
        }
        else if (match.isBefore(now) && hour < 0 && match.getMinute() < now.getMinute())
        {
            match = match.plusHours(1);
        }
        else if (match.isBefore(now) && minutes < 0 && match.getSecond() < now.getSecond())
        {
            match = match.plusMinutes(1);
        }

        return ChronoUnit.SECONDS.between(now, match) * 20;
    }

    @Override
    public String getType()
    {
        return "vanilife:match";
    }

    @Override
    public void start(Service service, ConfigurationSection config)
    {
        if (service.isStopped())
        {
            return;
        }

        int year = config.getInt("year");
        int month = config.getInt("month");
        int day = config.getInt("day");
        int hour = config.getInt("hour");
        int minutes = config.getInt("minutes");
        int second = config.getInt("second");
        long laterTicks = MatchSchedule.getLaterTicks(year, month, day, hour, minutes, second);

        if (laterTicks < 0)
        {
            return;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (service.isStopped())
                {
                    return;
                }

                service.run();

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        start(service, config);
                    }
                }.runTaskLater(Vanilife.getPlugin(), 20L * 4);
            }
        }.runTaskLater(Vanilife.getPlugin(), MatchSchedule.getLaterTicks(year, month, day, hour, minutes, second));
    }
}
