package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EndPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "end";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.END_STONE;
    }

    @Override
    public int getCost()
    {
        return 140;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.END_STONE,
                Material.END_STONE_BRICKS,
                Material.END_STONE_BRICK_STAIRS,
                Material.END_STONE_BRICK_SLAB,
                Material.END_STONE_BRICK_WALL,
                Material.PURPUR_BLOCK,
                Material.PURPUR_PILLAR,
                Material.PURPUR_STAIRS,
                Material.PURPUR_SLAB,
                Material.END_ROD);
    }
}
