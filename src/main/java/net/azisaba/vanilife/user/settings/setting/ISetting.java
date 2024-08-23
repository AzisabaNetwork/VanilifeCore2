package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.user.User;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface ISetting
{
    void init(User user);

    void save();

    String getName();

    ItemStack getFavicon();

    void onClick(InventoryClickEvent event);

    default void onLeftClick(InventoryClickEvent event) {}

    default void onRightClick(InventoryClickEvent event) {}
}
