package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DarkOakPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "dark_oak";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.DARK_OAK_LOG;
    }

    @Override
    public int getCost()
    {
        return 80;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.DARK_OAK_LOG,
                Material.DARK_OAK_WOOD,
                Material.STRIPPED_DARK_OAK_LOG,
                Material.STRIPPED_DARK_OAK_WOOD,
                Material.DARK_OAK_PLANKS,
                Material.DARK_OAK_STAIRS,
                Material.DARK_OAK_SLAB,
                Material.DARK_OAK_FENCE,
                Material.DARK_OAK_FENCE_GATE,
                Material.DARK_OAK_DOOR,
                Material.DARK_OAK_TRAPDOOR,
                Material.DARK_OAK_PRESSURE_PLATE,
                Material.DARK_OAK_BUTTON,
                Material.DARK_OAK_SIGN,
                Material.DARK_OAK_WALL_SIGN,
                Material.DARK_OAK_HANGING_SIGN,
                Material.DARK_OAK_WALL_HANGING_SIGN,
                Material.DARK_OAK_SAPLING,
                Material.DARK_OAK_LEAVES);
    }
}
