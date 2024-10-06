package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BambooPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "bamboo";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.BAMBOO;
    }

    @Override
    public int getCost()
    {
        return 80;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.BAMBOO_BLOCK,
                Material.STRIPPED_BAMBOO_BLOCK,
                Material.BAMBOO_PLANKS,
                Material.BAMBOO_MOSAIC,
                Material.BAMBOO_STAIRS,
                Material.BAMBOO_MOSAIC_SLAB,
                Material.BAMBOO_SLAB,
                Material.BAMBOO_MOSAIC_SLAB,
                Material.BAMBOO_FENCE,
                Material.BAMBOO_FENCE_GATE,
                Material.BAMBOO_DOOR,
                Material.BAMBOO_TRAPDOOR,
                Material.BAMBOO_PRESSURE_PLATE,
                Material.BAMBOO_BUTTON,
                Material.BAMBOO_SIGN,
                Material.BAMBOO_WALL_SIGN,
                Material.BAMBOO_HANGING_SIGN,
                Material.BAMBOO_HANGING_SIGN);
    }
}
