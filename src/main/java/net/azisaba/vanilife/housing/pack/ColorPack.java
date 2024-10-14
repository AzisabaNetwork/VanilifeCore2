package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ColorPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "color";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.PINK_DYE;
    }

    @Override
    public int getCost()
    {
        return 100;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.WHITE_WOOL,
                Material.LIGHT_GRAY_WOOL,
                Material.GRAY_WOOL,
                Material.BLACK_WOOL,
                Material.BROWN_WOOL,
                Material.RED_WOOL,
                Material.ORANGE_WOOL,
                Material.YELLOW_WOOL,
                Material.LIME_WOOL,
                Material.GREEN_WOOL,
                Material.CYAN_WOOL,
                Material.LIGHT_BLUE_WOOL,
                Material.BLUE_WOOL,
                Material.PURPLE_WOOL,
                Material.MAGENTA_WOOL,
                Material.PINK_WOOL,
                Material.WHITE_CARPET,
                Material.LIGHT_GRAY_CARPET,
                Material.GRAY_CARPET,
                Material.BLACK_CARPET,
                Material.BROWN_CARPET,
                Material.RED_CARPET,
                Material.ORANGE_CARPET,
                Material.YELLOW_CARPET,
                Material.LIME_CARPET,
                Material.GREEN_CARPET,
                Material.CYAN_CARPET,
                Material.LIGHT_BLUE_CARPET,
                Material.BLUE_CARPET,
                Material.PURPLE_CANDLE,
                Material.MAGENTA_CARPET,
                Material.PINK_CARPET);
    }
}
