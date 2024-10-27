package net.azisaba.vanilife.magic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CakeMagic implements GiveMagic
{
    @Override
    public @NotNull ItemStack getItem()
    {
        return new ItemStack(Material.CAKE);
    }

    @Override
    public @NotNull List<String> getItemKeywords()
    {
        return List.of("おかし", "お菓子", "おやつ", "すいーつ", "スイーツ", "けーき", "ケーキ", "あまい", "甘い");
    }
}
