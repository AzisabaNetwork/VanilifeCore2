package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MidnightPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "midnight";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.RED_BED;
    }

    @Override
    public int getCost()
    {
        return 90;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(
                Material.CAMPFIRE,
                Material.SOUL_CAMPFIRE,
                Material.TORCH,
                Material.SOUL_TORCH,
                Material.WHITE_BED,
                Material.LIGHT_GRAY_BED,
                Material.GRAY_BED,
                Material.BLACK_BED,
                Material.BROWN_BED,
                Material.RED_BED,
                Material.ORANGE_BED,
                Material.YELLOW_BED,
                Material.LIME_BED,
                Material.GREEN_BED,
                Material.CYAN_BED,
                Material.LIGHT_BLUE_BED,
                Material.BLUE_BED,
                Material.PURPLE_BED,
                Material.MAGENTA_BED,
                Material.PINK_BED);
    }
}
