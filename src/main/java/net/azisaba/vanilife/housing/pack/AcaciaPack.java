package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AcaciaPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "acacia";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.ACACIA_LOG;
    }

    @Override
    public int getCost()
    {
        return 80;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.ACACIA_LOG,
                Material.ACACIA_WOOD,
                Material.STRIPPED_ACACIA_LOG,
                Material.STRIPPED_ACACIA_WOOD,
                Material.ACACIA_PLANKS,
                Material.ACACIA_STAIRS,
                Material.ACACIA_SLAB,
                Material.ACACIA_FENCE,
                Material.ACACIA_FENCE_GATE,
                Material.ACACIA_DOOR,
                Material.ACACIA_TRAPDOOR,
                Material.ACACIA_PRESSURE_PLATE,
                Material.ACACIA_BUTTON,
                Material.ACACIA_SIGN,
                Material.ACACIA_WALL_SIGN,
                Material.ACACIA_HANGING_SIGN,
                Material.ACACIA_WALL_HANGING_SIGN,
                Material.ACACIA_SAPLING,
                Material.ACACIA_LEAVES);
    }
}
