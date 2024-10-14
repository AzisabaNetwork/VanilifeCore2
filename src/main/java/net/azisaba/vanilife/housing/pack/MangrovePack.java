package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MangrovePack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "mangrove";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.MANGROVE_LOG;
    }

    @Override
    public int getCost()
    {
        return 80;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.MANGROVE_LOG,
                Material.MANGROVE_WOOD,
                Material.STRIPPED_MANGROVE_LOG,
                Material.STRIPPED_MANGROVE_WOOD,
                Material.MANGROVE_PLANKS,
                Material.MANGROVE_STAIRS,
                Material.MANGROVE_SLAB,
                Material.MANGROVE_FENCE,
                Material.MANGROVE_FENCE_GATE,
                Material.MANGROVE_DOOR,
                Material.MANGROVE_TRAPDOOR,
                Material.MANGROVE_PRESSURE_PLATE,
                Material.MANGROVE_BUTTON,
                Material.MANGROVE_SIGN,
                Material.MANGROVE_WALL_SIGN,
                Material.MANGROVE_HANGING_SIGN,
                Material.MANGROVE_WALL_HANGING_SIGN,
                Material.MANGROVE_ROOTS,
                Material.MANGROVE_PROPAGULE,
                Material.MANGROVE_LEAVES);
    }
}
