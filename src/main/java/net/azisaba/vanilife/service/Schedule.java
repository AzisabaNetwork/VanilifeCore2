package net.azisaba.vanilife.service;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Schedule
{
    private final Service service;
    private final Map<Integer, Integer> match = new LinkedHashMap<>();

    private BukkitRunnable runnable;

    public Schedule(@NotNull Service service, @NotNull ConfigurationSection source)
    {
        this.service = service;

        this.match.put(Calendar.YEAR, Objects.equals(source.getString("year"), "*") ? -1 : source.getInt("year"));
        this.match.put(Calendar.MONTH, Objects.equals(source.getString("month"), "*") ? -1 : source.getInt("month") - 1);
        this.match.put(Calendar.DATE, Objects.equals(source.getString("date"), "*") ? -1 : source.getInt("date"));
        this.match.put(Calendar.HOUR_OF_DAY, Objects.equals(source.getString("hour"), "*") ? -1 : source.getInt("hour"));
        this.match.put(Calendar.MINUTE, Objects.equals(source.getString("minute"), "*") ? -1 : source.getInt("minute"));
        this.match.put(Calendar.SECOND, Objects.equals(source.getString("second"), "*") ? -1 : source.getInt("second"));
        this.match.put(Calendar.MILLISECOND, 0);
    }

    public void start()
    {
        long delay = this.next().getTimeInMillis() - System.currentTimeMillis();

        if (delay < 0)
        {
            return;
        }

        this.runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                service.run();
                start();
            }
        };

        this.runnable.runTaskLater(Vanilife.getPlugin(), delay / 50);
    }

    public void stop()
    {
        if (this.runnable == null)
        {
            return;
        }

        this.runnable.cancel();
    }

    public Calendar next()
    {
        Calendar now = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        next.set(now.getMinimum(Calendar.YEAR), now.getMinimum(Calendar.MONTH), now.getMinimum(Calendar.DATE), now.getMinimum(Calendar.HOUR_OF_DAY), now.getMinimum(Calendar.MINUTE), now.getMinimum(Calendar.SECOND));

        Integer[] keys = this.match.keySet().toArray(new Integer[0]);
        Integer[] values = this.match.values().toArray(new Integer[0]);

        for (int i = 0; i < keys.length - 1; i ++)
        {
            if (0 <= values[i])
            {
                next.set(keys[i], values[i]);
            }

            for (int j = i - 1; 0 <= j; j --)
            {
                int k = j + 1;

                if (! (values[j] < 0 && 0 <= values[k]))
                {
                    continue;
                }

                next.set(keys[j], next.get(keys[k]) < now.get(keys[k]) ? this.next(keys[j]) : keys[j] == Calendar.YEAR ? now.get(Calendar.YEAR) : now.getMinimum(keys[j]));
            }
        }

        return next;
    }

    private int next(int field)
    {
        Calendar calendar = Calendar.getInstance();
        int now = calendar.get(field);
        int max = calendar.getActualMaximum(field);

        return (now + 1) % (max + 1);
    }
}
