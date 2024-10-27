package net.azisaba.vanilife.magic;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface WeatherMagic extends Magic
{
    @NotNull WeatherType getWeather();

    @Override
    default @NotNull List<String> getKeywords()
    {
        List<String> keywords = new ArrayList<>(this.getItemKeywords());
        keywords.addAll(List.of("てんき", "天気", "てんこう", "天候", "そら", "空"));
        return keywords;
    }

    @NotNull List<String> getItemKeywords();

    @Override
    default void perform(@NotNull Player player)
    {
        new BukkitRunnable()
        {
            private int i = 0;

            @Override
            public void run()
            {
                if (60 < this.i ++)
                {
                    Bukkit.getOnlinePlayers().forEach(Player::resetPlayerWeather);
                    this.cancel();
                    return;
                }

                List<Entity> entities = player.getNearbyEntities(150, 150, 150);
                entities.add(player);

                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (entities.stream().anyMatch(entity -> entity.getEntityId() == player.getEntityId()))
                    {
                        player.setPlayerWeather(WeatherMagic.this.getWeather());
                    }
                    else if (player.getPlayerWeather() != null)
                    {
                        player.resetPlayerWeather();
                    }
                });
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0L, 10L);
    }
}
