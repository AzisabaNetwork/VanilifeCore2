package net.azisaba.vanilife.service;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Schedule
{
    private Service service;

    private final int year;
    private final int month;
    private final int date;
    private final int hour;
    private final int minute;
    private final int second;

    private final Map<Integer, Integer> match = new LinkedHashMap<>();

    private BukkitRunnable runnable;

    public Schedule(@NotNull Service service, @NotNull ConfigurationSection source)
    {
        this.service = service;

        this.year = Objects.equals(source.getString("year"), "*") ? -1 : source.getInt("year");
        this.month = Objects.equals(source.getString("month"), "*") ? -1 : source.getInt("month") - 1;
        this.date = Objects.equals(source.getString("date"), "*") ? -1 : source.getInt("date");
        this.hour = Objects.equals(source.getString("hour"), "*") ? -1 : source.getInt("hour");
        this.minute = Objects.equals(source.getString("minute"), "*") ? -1 : source.getInt("minute");
        this.second = Objects.equals(source.getString("second"), "*") ? -1 : source.getInt("second");

        this.match.put(Calendar.SECOND, this.second);
        this.match.put(Calendar.MINUTE, this.minute);
        this.match.put(Calendar.HOUR_OF_DAY, this.hour);
        this.match.put(Calendar.DATE, this.date);
        this.match.put(Calendar.MONTH, this.month);
        this.match.put(Calendar.YEAR, this.year);
    }

    public void start()
    {
        long delay = this.next().getTimeInMillis() - System.currentTimeMillis();

        if (delay < 0)
        {
            return;
        }

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), this::run, delay / 50);
    }

    public void stop()
    {
        this.service = null;
    }

    private void run()
    {
        if (this.service == null)
        {
            return;
        }

        this.service.run();
        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), this::start, 20L * 2);
    }

    public Calendar next()
    {
        Calendar now = Calendar.getInstance();
        Calendar next = Calendar.getInstance();

        next.set(Calendar.YEAR, 0 <= this.year ? this.year : now.get(Calendar.YEAR));
        next.set(Calendar.MONTH, 0 <= this.month ? this.month : now.get(Calendar.MONTH));
        next.set(Calendar.DATE, 0 <= this.date ? this.date : now.get(Calendar.DATE));
        next.set(Calendar.HOUR_OF_DAY, 0 <= this.hour ? this.hour : now.get(Calendar.HOUR_OF_DAY));
        next.set(Calendar.MINUTE, 0 <= this.minute ? this.minute : now.get(Calendar.MINUTE));
        next.set(Calendar.SECOND, 0 <= this.second ? this.second : now.get(Calendar.SECOND));

        if (next.equals(now))
        {
            return next;
        }

        if (next.after(now))
        {
            return optimize();
        }

        if (0 <= this.year)
        {
            return null;
        }

        return search();
    }

    private int next(int field)
    {
        Calendar calendar = Calendar.getInstance();
        int now = calendar.get(field);
        int max = calendar.getActualMaximum(field);

        return (now + 1) % (max + 1);
    }

    private @NotNull Calendar optimize()
    {
        Calendar calendar = Calendar.getInstance();
        boolean flag = false;

        for (Map.Entry<Integer, Integer> entry : this.match.entrySet())
        {
            int field = entry.getKey();
            int value = entry.getValue();

            if (0 <= value)
            {
                flag = true;
                calendar.set(field, value);
                continue;
            }

            calendar.set(field, flag ? calendar.get(field) : calendar.getMinimum(field));
        }

        return calendar;
    }

    private @NotNull Calendar search()
    {
        Calendar calendar = Calendar.getInstance();
        boolean b1 = false;
        boolean b2 = false;

        for (Map.Entry<Integer, Integer> entry : this.match.entrySet())
        {
            int field = entry.getKey();
            int value = entry.getValue();

            if (0 <= value)
            {
                b1 = true;
                calendar.set(field, value);
                continue;
            }

            if (b1 && ! b2)
            {
                calendar.set(field, this.next(field));
                b2 = true;
                continue;
            }

            if (! b1)
            {
                calendar.set(field, calendar.getMinimum(field));
            }
        }

        return calendar;
    }
}
