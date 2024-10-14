package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CrafterPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "crafter";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.CRAFTING_TABLE;
    }

    @Override
    public int getCost()
    {
        return 120;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.CHEST,
                Material.SHULKER_BOX,
                Material.ANVIL,
                Material.CRAFTING_TABLE,
                Material.FURNACE,
                Material.SMOKER,
                Material.BLAST_FURNACE,
                Material.LOOM,
                Material.ENCHANTING_TABLE,
                Material.BLACKSTONE,
                Material.GRINDSTONE,
                Material.BREWING_STAND,
                Material.ARMOR_STAND,
                Material.ITEM_FRAME,
                Material.GLOW_ITEM_FRAME,
                Material.PAINTING);
    }
}
