package net.azisaba.vanilife.magic;

import org.bukkit.WeatherType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClearMagic implements WeatherMagic
{
    @Override
    public @NotNull WeatherType getWeather()
    {
        return WeatherType.CLEAR;
    }

    @Override
    public @NotNull List<String> getItemKeywords()
    {
        return List.of("はれ", "晴れ", "せいてん", "晴天");
    }
}
