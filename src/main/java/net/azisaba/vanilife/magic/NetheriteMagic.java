package net.azisaba.vanilife.magic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NetheriteMagic implements GiveMagic
{
    @Override
    public @NotNull ItemStack getItem()
    {
        return new ItemStack(Material.NETHERITE_SCRAP, 2);
    }

    @Override
    public @NotNull List<String> getItemKeywords()
    {
        return List.of("ねざらいと", "ネザライト");
    }
}
