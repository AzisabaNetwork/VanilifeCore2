package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.settings.setting.ISetting;
import net.azisaba.vanilife.user.settings.Settings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class SettingsUI extends InventoryUI
{
    private final HashMap<Integer, ISetting> settingMap = new HashMap<>();

    public SettingsUI(@NotNull Player player, @NotNull Settings settings)
    {
        super(player, Bukkit.createInventory(null, 36, Component.text("設定")));

        for (int i = 0; i < settings.getSettings().size(); i ++)
        {
            int index = (i < 5) ? 11 + i : 20 + (i - 5);

            ISetting setting = settings.getSettings().get(i);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    inventory.setItem(index, setting.getFavicon());
                }
            }.runTaskAsynchronously(Vanilife.getPlugin());

            this.settingMap.put(index, setting);
        }

        ItemStack closeStack = new ItemStack(Material.OAK_DOOR);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Component.text("閉じる").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Component.text("この画面を閉じます").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(24, closeStack);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getSlot() == 24)
        {
            this.player.closeInventory();
            return;
        }

        if (this.settingMap.containsKey(event.getSlot()))
        {
            ISetting setting = this.settingMap.get(event.getSlot());

            setting.onClick(event);

            if (event.getClick() == ClickType.LEFT)
            {
                setting.onLeftClick(event);
            }

            if (event.getClick() == ClickType.RIGHT)
            {
                setting.onRightClick(event);
            }
        }
    }
}
