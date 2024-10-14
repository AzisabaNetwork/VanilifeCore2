package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FarmingPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "farming";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.WHEAT;
    }

    @Override
    public int getCost()
    {
        return 100;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.WHEAT,
                Material.CARROTS,
                Material.POTATOES,
                Material.SWEET_BERRY_BUSH,
                Material.BEETROOTS,
                Material.MELON_STEM,
                Material.PUMPKIN_STEM,
                Material.CAVE_VINES);
    }
}
