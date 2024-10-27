package net.azisaba.vanilife.magic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmeraldMagic implements GiveMagic
{
    @Override
    public @NotNull ItemStack getItem()
    {
        return new ItemStack(Material.EMERALD, 3);
    }

    @Override
    public @NotNull List<String> getItemKeywords()
    {
        return List.of("えめらるど", "エメラルド", "おかね", "かね", "お金");
    }
}
