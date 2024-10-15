package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GardeningPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "gardening";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.PEONY;
    }

    @Override
    public int getCost()
    {
        return 90;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.DANDELION,
                Material.POPPY,
                Material.BLUE_ORCHID,
                Material.ALLIUM,
                Material.AZURE_BLUET,
                Material.RED_TULIP,
                Material.ORANGE_TULIP,
                Material.WHITE_TULIP,
                Material.PINK_TULIP,
                Material.OXEYE_DAISY,
                Material.CORNFLOWER,
                Material.LILY_OF_THE_VALLEY,
                Material.TORCHFLOWER,
                Material.WITHER_ROSE,
                Material.PINK_PETALS,
                Material.SPORE_BLOSSOM,
                Material.SUGAR_CANE,
                Material.CACTUS,
                Material.TALL_GRASS,
                Material.LARGE_FERN);
    }
}
