package net.azisaba.vanilife.gimmick;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IGimmick
{
    @NotNull String getType();

    @NotNull ItemStack getDrop();

    void use(@NotNull PlayerInteractEvent event);

    void run(@NotNull Player player);
}
