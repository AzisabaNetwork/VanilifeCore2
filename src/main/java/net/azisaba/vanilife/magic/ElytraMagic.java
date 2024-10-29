package net.azisaba.vanilife.magic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ElytraMagic implements GiveMagic
{
    @Override
    public @NotNull ItemStack getItem()
    {
        return new ItemStack(Material.PHANTOM_MEMBRANE, 3);
    }

    @Override
    public @NotNull List<String> getItemKeywords()
    {
        return List.of("えりとら", "エリトラ", "ほうき");
    }
}
