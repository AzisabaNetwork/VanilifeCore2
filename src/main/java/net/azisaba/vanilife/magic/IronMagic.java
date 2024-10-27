package net.azisaba.vanilife.magic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IronMagic implements GiveMagic
{
    @Override
    public @NotNull ItemStack getItem()
    {
        return new ItemStack(Material.IRON_INGOT, 32);
    }

    @Override
    public @NotNull List<String> getItemKeywords()
    {
        return List.of("てつ", "鉄", "きんぞく", "金属");
    }
}
