package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NetherPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "nether";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.NETHERRACK;
    }

    @Override
    public int getCost()
    {
        return 120;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.NETHERRACK,
                Material.CRIMSON_NYLIUM,
                Material.WARPED_NYLIUM,
                Material.NETHER_BRICK,
                Material.CRACKED_NETHER_BRICKS,
                Material.NETHER_BRICK_STAIRS,
                Material.NETHER_BRICK_SLAB,
                Material.NETHER_BRICK_WALL,
                Material.NETHER_BRICK_FENCE,
                Material.CHISELED_NETHER_BRICKS,
                Material.RED_NETHER_BRICKS,
                Material.RED_NETHER_BRICK_STAIRS,
                Material.RED_NETHER_BRICK_SLAB,
                Material.RED_NETHER_BRICK_WALL,
                Material.BASALT,
                Material.SMOOTH_BASALT,
                Material.POLISHED_BASALT,
                Material.BLACKSTONE,
                Material.GILDED_BLACKSTONE,
                Material.BLACKSTONE_STAIRS,
                Material.BLACKSTONE_SLAB,
                Material.BLACKSTONE_WALL,
                Material.CHISELED_POLISHED_BLACKSTONE,
                Material.POLISHED_BLACKSTONE,
                Material.POLISHED_BLACKSTONE_STAIRS,
                Material.POLISHED_BLACKSTONE_SLAB,
                Material.POLISHED_BLACKSTONE_WALL,
                Material.POLISHED_BLACKSTONE_PRESSURE_PLATE,
                Material.POLISHED_BLACKSTONE_BUTTON,
                Material.POLISHED_BLACKSTONE_BRICKS,
                Material.CRACKED_POLISHED_BLACKSTONE_BRICKS,
                Material.POLISHED_BLACKSTONE_BRICK_STAIRS,
                Material.POLISHED_BLACKSTONE_BRICK_SLAB,
                Material.POLISHED_BLACKSTONE_WALL,
                Material.MAGMA_BLOCK,
                Material.CRIMSON_FUNGUS,
                Material.WARPED_FUNGUS,
                Material.CRIMSON_STEM,
                Material.CRIMSON_HYPHAE,
                Material.STRIPPED_CRIMSON_STEM,
                Material.STRIPPED_CRIMSON_HYPHAE,
                Material.CRIMSON_PLANKS,
                Material.CRIMSON_STAIRS,
                Material.CRIMSON_SLAB,
                Material.CRIMSON_FENCE,
                Material.CRIMSON_FENCE_GATE,
                Material.CRIMSON_DOOR,
                Material.CRIMSON_TRAPDOOR,
                Material.CRIMSON_PRESSURE_PLATE,
                Material.CRIMSON_BUTTON,
                Material.WARPED_PLANKS,
                Material.WARPED_STAIRS,
                Material.WARPED_SLAB,
                Material.WARPED_FENCE,
                Material.WARPED_FENCE_GATE,
                Material.WARPED_DOOR,
                Material.WARPED_TRAPDOOR,
                Material.WARPED_PRESSURE_PLATE,
                Material.WARPED_BUTTON);
    }
}
