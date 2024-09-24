package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SprucePack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "spruce";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.SPRUCE_LOG;
    }

    @Override
    public int getCost()
    {
        return 100;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.SPRUCE_LOG,
                Material.SPRUCE_WOOD,
                Material.STRIPPED_SPRUCE_LOG,
                Material.STRIPPED_SPRUCE_WOOD,
                Material.SPRUCE_PLANKS,
                Material.SPRUCE_STAIRS,
                Material.SPRUCE_SLAB,
                Material.SPRUCE_FENCE,
                Material.SPRUCE_FENCE_GATE,
                Material.SPRUCE_DOOR,
                Material.SPRUCE_TRAPDOOR,
                Material.SPRUCE_PRESSURE_PLATE,
                Material.SPRUCE_BUTTON,
                Material.SPRUCE_SIGN,
                Material.SPRUCE_WALL_SIGN,
                Material.SPRUCE_HANGING_SIGN,
                Material.SPRUCE_WALL_HANGING_SIGN,
                Material.SPRUCE_SAPLING,
                Material.SPRUCE_LEAVES);
    }
}
