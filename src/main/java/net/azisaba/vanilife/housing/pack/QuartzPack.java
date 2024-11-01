package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class QuartzPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "quartz";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.QUARTZ;
    }

    @Override
    public int getCost()
    {
        return 180;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.QUARTZ_BLOCK,
                Material.QUARTZ_STAIRS,
                Material.QUARTZ_SLAB,
                Material.CHISELED_QUARTZ_BLOCK,
                Material.QUARTZ_BRICKS,
                Material.QUARTZ_PILLAR,
                Material.SMOOTH_QUARTZ,
                Material.SMOOTH_QUARTZ_STAIRS,
                Material.SMOOTH_QUARTZ_SLAB);
    }
}
