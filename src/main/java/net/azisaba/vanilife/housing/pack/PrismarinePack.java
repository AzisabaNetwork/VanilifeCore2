package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrismarinePack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "prismarine";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.PRISMARINE_SHARD;
    }

    @Override
    public int getCost()
    {
        return 175;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.PRISMARINE,
                Material.PRISMARINE_STAIRS,
                Material.PRISMARINE_SLAB,
                Material.PRISMARINE_WALL,
                Material.PRISMARINE_BRICKS,
                Material.PRISMARINE_BRICK_STAIRS,
                Material.PRISMARINE_BRICK_SLAB,
                Material.DARK_PRISMARINE,
                Material.DARK_PRISMARINE_STAIRS,
                Material.DARK_PRISMARINE_SLAB,
                Material.SEA_LANTERN);
    }
}
