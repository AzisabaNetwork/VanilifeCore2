package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StonePack2 implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "stone2";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.DEEPSLATE;
    }

    @Override
    public int getCost()
    {
        return 200;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.ANDESITE,
                Material.ANDESITE_STAIRS,
                Material.ANDESITE_SLAB,
                Material.ANDESITE_WALL,
                Material.POLISHED_ANDESITE,
                Material.POLISHED_ANDESITE_STAIRS,
                Material.POLISHED_ANDESITE_SLAB,
                Material.DEEPSLATE,
                Material.COBBLED_DEEPSLATE,
                Material.COBBLED_DEEPSLATE_STAIRS,
                Material.COBBLED_DEEPSLATE_SLAB,
                Material.COBBLED_DEEPSLATE_WALL,
                Material.CHISELED_DEEPSLATE,
                Material.POLISHED_DEEPSLATE,
                Material.POLISHED_DEEPSLATE_STAIRS,
                Material.POLISHED_DEEPSLATE_SLAB,
                Material.POLISHED_DEEPSLATE_WALL,
                Material.DEEPSLATE_BRICKS,
                Material.DEEPSLATE_BRICK_STAIRS,
                Material.DEEPSLATE_BRICK_SLAB,
                Material.DEEPSLATE_BRICK_WALL,
                Material.DEEPSLATE_TILES,
                Material.CRACKED_DEEPSLATE_TILES,
                Material.DEEPSLATE_TILE_STAIRS,
                Material.DEEPSLATE_TILE_SLAB,
                Material.DEEPSLATE_TILE_WALL,
                Material.REINFORCED_DEEPSLATE,
                Material.TUFF,
                Material.TUFF_STAIRS,
                Material.TUFF_SLAB,
                Material.TUFF_WALL,
                Material.CHISELED_TUFF,
                Material.POLISHED_TUFF,
                Material.POLISHED_TUFF_STAIRS,
                Material.POLISHED_TUFF_SLAB,
                Material.POLISHED_TUFF_WALL,
                Material.TUFF_BRICKS,
                Material.TUFF_BRICK_STAIRS,
                Material.TUFF_BRICK_SLAB,
                Material.TUFF_BRICK_WALL,
                Material.CHISELED_TUFF_BRICKS,
                Material.BRICKS,
                Material.BRICK_STAIRS,
                Material.BRICK_SLAB,
                Material.BRICK_WALL);
    }
}
