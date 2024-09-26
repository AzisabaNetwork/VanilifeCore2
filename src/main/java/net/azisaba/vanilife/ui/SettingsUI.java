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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsUI extends InventoryUI
{
    private final HashMap<Integer, ISetting<?>> settingMap = new HashMap<>();

    public SettingsUI(@NotNull Player player, @NotNull Settings settings)
    {
        super(player, Bukkit.createInventory(null, 36, Language.translate("ui.settings.title", player)));

        ItemStack faviconStack = new ItemStack(Material.COMPARATOR);
        ItemMeta faviconMeta = faviconStack.getItemMeta();
        faviconMeta.displayName(Language.translate("ui.settings.title", player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        final int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 17, 18};

        for (int i = 0; i < settings.getSettings().size(); i ++)
        {
            ISetting<?> setting = settings.getSettings().get(i);

            final int index = slots[i];

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    ItemStack stack = setting.getIcon();
                    ItemMeta meta = stack.getItemMeta();

                    final String translation = "settings." + setting.getName();

                    meta.displayName(Language.translate(translation + ".name", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                    List<Component> lore = new ArrayList<>();

                    if (Language.has(translation + ".description.1", player))
                    {
                        int i = 1;

                        while (Language.has(translation + ".description." + i, player))
                        {
                            lore.add(Language.translate(translation + ".description." + i, player).decoration(TextDecoration.ITALIC, false));
                            i ++;
                        }

                        lore.add(Component.text().build());
                    }

                    lore.addAll(setting.getDetails());
                    meta.lore(lore);
                    stack.setItemMeta(meta);
                    inventory.setItem(index, stack);

                    ItemStack stateStack = setting.getStateIcon();
                    ItemMeta stateMeta = stateStack.getItemMeta();
                    stateMeta.setHideTooltip(true);
                    stateStack.setItemMeta(stateMeta);
                    inventory.setItem(index + 9, stateStack);
                }
            }.runTaskAsynchronously(Vanilife.getPlugin());

            this.settingMap.put(index, setting);
        }

        ItemStack closeStack = new ItemStack(Material.ARROW);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(31, closeStack);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getSlot() == 31)
        {
            this.player.closeInventory();
        }

        if (this.settingMap.containsKey(event.getRawSlot()))
        {
            ISetting<?> setting = this.settingMap.get(event.getSlot());

            setting.onClick(event);

            if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT)
            {
                setting.onLeftClick(event);
            }

            if (event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT)
            {
                setting.onRightClick(event);
            }
        }
    }
}
