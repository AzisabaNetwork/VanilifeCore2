package net.azisaba.vanilife.util;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

import java.time.LocalDate;

import static net.azisaba.vanilife.util.SeasonUtility.Season.*;

@Utility
public class SeasonUtility
{
    public static Season getSeason()
    {
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();

        if (month >= 3 && month <= 5)
        {
            return Season.SPRING;
        }
        else if (month >= 6 && month <= 8)
        {
            return Season.SUMMER;
        }
        else if (month >= 9 && month <= 11)
        {
            return Season.FALL;
        }
        else
        {
            return WINTER;
        }
    }

    public static Season getPreviousSeason()
    {
        return SeasonUtility.getPreviousSeason(SeasonUtility.getSeason());
    }

    public static Season getPreviousSeason(Season season)
    {
        return switch (season)
        {
            case SPRING -> WINTER;
            case SUMMER -> SPRING;
            case FALL -> SUMMER;
            case WINTER -> FALL;
        };
    }

    public static Season getNextSeason()
    {
        return SeasonUtility.getNextSeason(SeasonUtility.getSeason());
    }

    public static Season getNextSeason(Season season)
    {
        return switch (season)
        {
            case SPRING -> SUMMER;
            case SUMMER -> FALL;
            case FALL -> WINTER;
            case WINTER -> SPRING;
        };
    }

    public static Material getSeasonMaterial()
    {
        return SeasonUtility.getSeasonMaterial(SeasonUtility.getSeason());
    }

    public static Material getSeasonMaterial(Season season)
    {
        return switch (season)
        {
            case SPRING -> Material.CHERRY_LEAVES;
            case SUMMER -> Material.SAND;
            case FALL -> Material.PUMPKIN;
            case WINTER -> Material.SNOW_BLOCK;
        };
    }

    public static TextColor getSeasonColor()
    {
        return SeasonUtility.getSeasonColor(SeasonUtility.getSeason());
    }

    public static TextColor getSeasonColor(Season season)
    {
        return switch (season)
        {
            case SPRING -> TextColor.color(247, 200, 209);
            case SUMMER -> TextColor.color(0, 168, 227);
            case FALL -> TextColor.color(235, 89, 2);
            case WINTER -> TextColor.color(192, 196, 227);
        };
    }

    public enum Season
    {
        SPRING,
        SUMMER,
        FALL,
        WINTER
    }
}
