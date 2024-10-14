package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TerrainPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "terrain";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.GRASS_BLOCK;
    }

    @Override
    public int getCost()
    {
        return 150;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.GRASS_BLOCK,
                Material.PODZOL,
                Material.MYCELIUM,
                Material.DIRT_PATH,
                Material.DIRT,
                Material.COARSE_DIRT,
                Material.ROOTED_DIRT,
                Material.FARMLAND,
                Material.MUD,
                Material.WATER,
                Material.LAVA,
                Material.CLAY,
                Material.GRAVEL,
                Material.SAND,
                Material.SANDSTONE,
                Material.SANDSTONE_STAIRS,
                Material.SANDSTONE_SLAB,
                Material.SANDSTONE_WALL,
                Material.CHISELED_SANDSTONE,
                Material.SMOOTH_STONE,
                Material.SMOOTH_SANDSTONE_STAIRS,
                Material.SMOOTH_SANDSTONE_SLAB,
                Material.CUT_SANDSTONE,
                Material.CUT_SANDSTONE_SLAB,
                Material.RED_SAND,
                Material.RED_SANDSTONE,
                Material.RED_SANDSTONE_STAIRS,
                Material.RED_SANDSTONE_SLAB,
                Material.RED_SANDSTONE_WALL,
                Material.CHISELED_RED_SANDSTONE,
                Material.SMOOTH_RED_SANDSTONE,
                Material.SMOOTH_RED_SANDSTONE_STAIRS,
                Material.SMOOTH_RED_SANDSTONE_SLAB,
                Material.CUT_RED_SANDSTONE,
                Material.CUT_RED_SANDSTONE_SLAB,
                Material.ICE,
                Material.PACKED_ICE,
                Material.BLUE_ICE,
                Material.SNOW_BLOCK,
                Material.SNOW,
                Material.MOSS_BLOCK,
                Material.MOSS_CARPET,
                Material.SHORT_GRASS,
                Material.FERN,
                Material.DEAD_BUSH);
    }
}
