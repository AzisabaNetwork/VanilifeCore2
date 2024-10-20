package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SkullPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "skull";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.CREEPER_HEAD;
    }

    @Override
    public int getCost()
    {
        return 220;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.ZOMBIE_HEAD,
                Material.SKELETON_SKULL,
                Material.WITHER_ROSE,
                Material.CREEPER_HEAD,
                Material.PIGLIN_HEAD,
                Material.DRAGON_HEAD);
    }
}
