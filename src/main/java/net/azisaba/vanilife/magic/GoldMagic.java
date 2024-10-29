package net.azisaba.vanilife.magic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GoldMagic implements GiveMagic
{
    @Override
    public @NotNull ItemStack getItem()
    {
        return new ItemStack(Material.GOLD_INGOT, 20);
    }

    @Override
    public @NotNull List<String> getItemKeywords()
    {
        return List.of("きん", "金", "ごーるど", "ゴールド", "ききんぞく", "貴金属");
    }
}
