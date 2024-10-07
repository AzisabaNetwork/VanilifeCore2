package net.azisaba.vanilife.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IVanilifeEntity
{
    @NotNull String getName();

    default Component getDisplayName()
    {
        return null;
    }

    @NotNull EntityType getType();

    default ItemStack getHelmet()
    {
        return null;
    }

    default ItemStack getChestplate()
    {
        return null;
    }

    default ItemStack getLeggings()
    {
        return null;
    }

    default ItemStack getBoots()
    {
        return null;
    }

    default ItemStack getMainHand()
    {
        return null;
    }

    default ItemStack getOffHand()
    {
        return null;
    }

    default double getScale()
    {
        return 1.0;
    }

    void setScale(double scale);

    void tick();
}
