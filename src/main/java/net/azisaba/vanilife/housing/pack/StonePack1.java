package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StonePack1 implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "stone1";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.STONE;
    }

    @Override
    public int getCost()
    {
        return 600;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.STONE,
                Material.STONE_STAIRS,
                Material.STONE_SLAB,
                Material.STONE_PRESSURE_PLATE,
                Material.STONE_BUTTON,
                Material.COBBLESTONE,
                Material.COBBLESTONE_STAIRS,
                Material.COBBLESTONE_SLAB,
                Material.COBBLESTONE_WALL,
                Material.MOSSY_COBBLESTONE,
                Material.MOSSY_COBBLESTONE_STAIRS,
                Material.MOSSY_COBBLESTONE_SLAB,
                Material.MOSSY_COBBLESTONE_WALL,
                Material.SMOOTH_STONE,
                Material.SMOOTH_STONE_SLAB,
                Material.STONE_BRICKS,
                Material.CRACKED_STONE_BRICKS,
                Material.STONE_BRICK_STAIRS,
                Material.STONE_BRICK_SLAB,
                Material.STONE_BRICK_WALL,
                Material.CHISELED_STONE_BRICKS,
                Material.MOSSY_STONE_BRICKS,
                Material.MOSSY_STONE_BRICK_STAIRS,
                Material.MOSSY_STONE_BRICK_SLAB,
                Material.MOSSY_STONE_BRICK_WALL,
                Material.GRANITE,
                Material.GRANITE_STAIRS,
                Material.GRANITE_SLAB,
                Material.GRANITE_WALL,
                Material.GRANITE_WALL,
                Material.POLISHED_GRANITE,
                Material.POLISHED_GRANITE_STAIRS,
                Material.POLISHED_GRANITE_SLAB,
                Material.DIORITE,
                Material.DIORITE_STAIRS,
                Material.DIORITE_SLAB,
                Material.DIORITE_WALL,
                Material.POLISHED_DIORITE,
                Material.POLISHED_DIORITE_STAIRS,
                Material.POLISHED_GRANITE_SLAB);
    }
}
