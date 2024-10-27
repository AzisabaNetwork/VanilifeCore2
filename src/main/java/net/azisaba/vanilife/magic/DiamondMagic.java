package net.azisaba.vanilife.magic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DiamondMagic implements GiveMagic
{
    @Override
    public @NotNull ItemStack getItem()
    {
        return new ItemStack(Material.DIAMOND);
    }

    @Override
    public @NotNull List<String> getItemKeywords()
    {
        return List.of("だいや", "ダイヤ", "だいあ", "ダイア", "だいやもんど", "ダイヤモンド", "だいあもんど", "ダイアモンド");
    }
}
