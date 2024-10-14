package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GlassPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "glass";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.GLASS_BOTTLE;
    }

    @Override
    public int getCost()
    {
        return 90;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.GLASS,
                Material.GLASS_PANE,
                Material.TINTED_GLASS,
                Material.WHITE_STAINED_GLASS,
                Material.WHITE_STAINED_GLASS_PANE,
                Material.LIGHT_GRAY_STAINED_GLASS,
                Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                Material.GRAY_STAINED_GLASS,
                Material.GRAY_STAINED_GLASS_PANE,
                Material.BLACK_STAINED_GLASS,
                Material.BLACK_STAINED_GLASS_PANE,
                Material.BROWN_STAINED_GLASS,
                Material.BROWN_STAINED_GLASS_PANE,
                Material.RED_STAINED_GLASS,
                Material.RED_STAINED_GLASS_PANE,
                Material.ORANGE_STAINED_GLASS,
                Material.ORANGE_STAINED_GLASS_PANE,
                Material.YELLOW_STAINED_GLASS,
                Material.YELLOW_STAINED_GLASS_PANE,
                Material.LIME_STAINED_GLASS,
                Material.LIME_STAINED_GLASS_PANE,
                Material.GREEN_STAINED_GLASS,
                Material.GREEN_STAINED_GLASS_PANE,
                Material.CYAN_STAINED_GLASS,
                Material.CYAN_STAINED_GLASS_PANE,
                Material.LIGHT_BLUE_STAINED_GLASS,
                Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                Material.BLUE_STAINED_GLASS,
                Material.BLUE_STAINED_GLASS_PANE,
                Material.PURPLE_STAINED_GLASS,
                Material.PURPLE_STAINED_GLASS_PANE,
                Material.MAGENTA_STAINED_GLASS,
                Material.MAGENTA_STAINED_GLASS_PANE,
                Material.PINK_STAINED_GLASS,
                Material.PINK_STAINED_GLASS_PANE);
    }
}
