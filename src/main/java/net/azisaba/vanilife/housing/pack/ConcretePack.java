package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConcretePack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "concrete";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.WHITE_CONCRETE;
    }

    @Override
    public int getCost()
    {
        return 200;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.WHITE_CONCRETE,
                Material.LIGHT_GRAY_CONCRETE,
                Material.GRAY_CONCRETE,
                Material.BLACK_CONCRETE,
                Material.BROWN_CONCRETE,
                Material.RED_CONCRETE,
                Material.ORANGE_CONCRETE,
                Material.YELLOW_CONCRETE,
                Material.LIME_CONCRETE,
                Material.GREEN_CONCRETE,
                Material.CYAN_CONCRETE,
                Material.LIGHT_BLUE_CONCRETE,
                Material.PURPLE_CONCRETE,
                Material.MAGENTA_CONCRETE,
                Material.PINK_CONCRETE);
    }

    @Override
    public @NotNull List<Tag<Material>> getTags()
    {
        return List.of(Tag.CONCRETE_POWDER);
    }
}
