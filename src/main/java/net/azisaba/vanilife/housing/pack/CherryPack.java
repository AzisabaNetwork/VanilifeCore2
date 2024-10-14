package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CherryPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "cherry";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.CHERRY_LOG;
    }

    @Override
    public int getCost()
    {
        return 80;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.CHERRY_LOG,
                Material.CHERRY_WOOD,
                Material.STRIPPED_CHERRY_LOG,
                Material.STRIPPED_CHERRY_WOOD,
                Material.CHERRY_PLANKS,
                Material.CHERRY_STAIRS,
                Material.CHERRY_SLAB,
                Material.CHERRY_FENCE,
                Material.CHERRY_FENCE_GATE,
                Material.CHERRY_DOOR,
                Material.CHERRY_TRAPDOOR,
                Material.CHERRY_PRESSURE_PLATE,
                Material.CHERRY_BUTTON,
                Material.CHERRY_SIGN,
                Material.CHERRY_WALL_SIGN,
                Material.CHERRY_HANGING_SIGN,
                Material.CHERRY_WALL_HANGING_SIGN,
                Material.CHERRY_SAPLING,
                Material.CHERRY_LEAVES);
    }
}
