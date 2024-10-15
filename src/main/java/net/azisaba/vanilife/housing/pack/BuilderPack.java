package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BuilderPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "builder";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.BRICKS;
    }

    @Override
    public int getCost()
    {
        return 100;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.LADDER,
                Material.LANTERN,
                Material.SEA_LANTERN,
                Material.BOOKSHELF,
                Material.CHISELED_BOOKSHELF,
                Material.LECTERN,
                Material.SOUL_LANTERN,
                Material.CHAIN,
                Material.SCAFFOLDING,
                Material.BEEHIVE,
                Material.DECORATED_POT);
    }
}
