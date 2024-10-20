package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StoragePack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "storage";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.CHEST;
    }

    @Override
    public int getCost()
    {
        return 200;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.CHEST,
                Material.BARREL,
                Material.ENDER_CHEST,
                Material.SHULKER_BOX,
                Material.WHITE_SHULKER_BOX,
                Material.LIGHT_GRAY_SHULKER_BOX,
                Material.GRAY_SHULKER_BOX,
                Material.BLACK_SHULKER_BOX,
                Material.BROWN_SHULKER_BOX,
                Material.RED_SHULKER_BOX,
                Material.ORANGE_SHULKER_BOX,
                Material.YELLOW_SHULKER_BOX,
                Material.LIME_SHULKER_BOX,
                Material.GREEN_SHULKER_BOX,
                Material.CYAN_SHULKER_BOX,
                Material.LIGHT_BLUE_SHULKER_BOX,
                Material.BLUE_SHULKER_BOX,
                Material.PURPLE_SHULKER_BOX,
                Material.MAGENTA_SHULKER_BOX,
                Material.PINK_SHULKER_BOX);
    }
}
