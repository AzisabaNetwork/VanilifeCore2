package net.azisaba.vanilife.user.settings.setting;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public interface ISetting<T extends Serializable>
{
    @NotNull String getName();

    @NotNull ItemStack getIcon();

    @NotNull ItemStack getStateIcon();

    @NotNull List<Component> getDetails();

    T getDefault();

    default void onClick(@NotNull InventoryClickEvent event) {}

    default void onLeftClick(@NotNull InventoryClickEvent event) {}

    default void onRightClick(@NotNull InventoryClickEvent event) {}
}
