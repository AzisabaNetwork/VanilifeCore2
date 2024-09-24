package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OakPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "oak";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.OAK_LOG;
    }

    @Override
    public int getCost()
    {
        return 100;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.OAK_LOG,
                Material.OAK_WOOD,
                Material.STRIPPED_OAK_LOG,
                Material.STRIPPED_OAK_WOOD,
                Material.OAK_PLANKS,
                Material.OAK_STAIRS,
                Material.OAK_SLAB,
                Material.OAK_FENCE,
                Material.OAK_FENCE_GATE,
                Material.OAK_DOOR,
                Material.OAK_TRAPDOOR,
                Material.OAK_PRESSURE_PLATE,
                Material.OAK_BUTTON,
                Material.OAK_SIGN,
                Material.OAK_WALL_SIGN,
                Material.OAK_HANGING_SIGN,
                Material.OAK_WALL_HANGING_SIGN,
                Material.OAK_SAPLING,
                Material.OAK_LEAVES);
    }
}
