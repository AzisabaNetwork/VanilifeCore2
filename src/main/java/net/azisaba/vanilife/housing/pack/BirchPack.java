package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BirchPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "birch";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.BIRCH_LOG;
    }

    @Override
    public int getCost()
    {
        return 80;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.BAMBOO,
                Material.BIRCH_LOG,
                Material.BIRCH_WOOD,
                Material.STRIPPED_BIRCH_LOG,
                Material.STRIPPED_BIRCH_WOOD,
                Material.BIRCH_PLANKS,
                Material.BIRCH_STAIRS,
                Material.BIRCH_SLAB,
                Material.BIRCH_FENCE,
                Material.BIRCH_FENCE_GATE,
                Material.BIRCH_DOOR,
                Material.BIRCH_TRAPDOOR,
                Material.BIRCH_PRESSURE_PLATE,
                Material.BIRCH_BUTTON,
                Material.BIRCH_SIGN,
                Material.BIRCH_WALL_SIGN,
                Material.BIRCH_HANGING_SIGN,
                Material.BIRCH_WALL_HANGING_SIGN,
                Material.BIRCH_SAPLING,
                Material.BIRCH_LEAVES);
    }
}
