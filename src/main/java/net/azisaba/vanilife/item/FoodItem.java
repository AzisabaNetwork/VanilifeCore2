package net.azisaba.vanilife.item;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface FoodItem extends IVanilifeItem
{
    default void consume(@NotNull Player player) {}
}
