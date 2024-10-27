package net.azisaba.vanilife.magic;

import org.bukkit.WeatherType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DownfallMagic implements WeatherMagic
{
    @Override
    public @NotNull WeatherType getWeather()
    {
        return WeatherType.DOWNFALL;
    }

    @Override
    public @NotNull List<String> getItemKeywords()
    {
        return List.of("あめ", "雨", "らいう", "雷雨", "ゆき", "雪", "たいふう", "台風");
    }
}
