package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JunglePack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "jungle";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.JUNGLE_LOG;
    }

    @Override
    public int getCost()
    {
        return 70;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.JUNGLE_LOG,
                Material.JUNGLE_WOOD,
                Material.STRIPPED_JUNGLE_LOG,
                Material.STRIPPED_JUNGLE_WOOD,
                Material.JUNGLE_PLANKS,
                Material.JUNGLE_STAIRS,
                Material.JUNGLE_SLAB,
                Material.JUNGLE_FENCE,
                Material.JUNGLE_FENCE_GATE,
                Material.JUNGLE_DOOR,
                Material.JUNGLE_TRAPDOOR,
                Material.JUNGLE_PRESSURE_PLATE,
                Material.JUNGLE_BUTTON,
                Material.JUNGLE_SIGN,
                Material.JUNGLE_WALL_SIGN,
                Material.JUNGLE_HANGING_SIGN,
                Material.JUNGLE_WALL_HANGING_SIGN,
                Material.JUNGLE_SAPLING,
                Material.JUNGLE_LEAVES);
    }
}
