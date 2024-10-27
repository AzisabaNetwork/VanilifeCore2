package net.azisaba.vanilife.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IVanilifeItem
{
    @NotNull String getName();

    Component getDisplayName();

    List<Component> getLore();

    @NotNull Material getTexture();

    boolean hasEnchantment();

    default @NotNull ItemStack asItemStack()
    {
        return this.asItemStack(1);
    }

    @NotNull ItemStack asItemStack(int amount);

    void use(@NotNull Player player, @NotNull ItemStorage storage);
}
