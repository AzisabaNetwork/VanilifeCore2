package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.gimmick.SoundGimmick;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoundGimmickUI extends ChestUI
{
    private static final List<SoundGimmick.Sound> sounds = List.of(SoundGimmick.Sound.values());

    private final SoundGimmick gimmick;
    private final Map<Integer, SoundGimmick.Sound> slots = new HashMap<>();
    private final int page;

    public SoundGimmickUI(@NotNull Player player, @NotNull SoundGimmick gimmick)
    {
        this(player, gimmick, 0);
    }

    public SoundGimmickUI(@NotNull Player player, @NotNull SoundGimmick gimmick, int page)
    {
        super(player, Bukkit.createInventory(null, 45, Language.translate("ui.gimmick.sound.title", player)));

        this.gimmick = gimmick;
        this.page = page;

        ItemStack backStack = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backStack.getItemMeta();
        backMeta.displayName(Language.translate("ui.back", player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        backMeta.lore(List.of(Language.translate("ui.back.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        backStack.setItemMeta(backMeta);
        this.inventory.setItem(18, backStack);

        ItemStack nextStack = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextStack.getItemMeta();
        nextMeta.displayName(Language.translate("ui.next", player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        nextMeta.lore(List.of(Language.translate("ui.next.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        nextStack.setItemMeta(nextMeta);
        this.inventory.setItem(26, nextStack);

        ItemStack closeStack = new ItemStack(Material.ARROW);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(40, closeStack);

        int i = 0;
        int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        for (SoundGimmick.Sound sound : SoundGimmickUI.sounds.subList(this.page * 21, Math.min((this.page + 1) * 21, SoundGimmickUI.sounds.size())))
        {
            ItemStack stack = new ItemStack(Material.NOTE_BLOCK);

            ItemMeta meta = stack.getItemMeta();
            meta.displayName(Language.translate("ui.gimmick.sound.sound." + sound.name().toLowerCase(), player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

            if (sound == this.gimmick.getSound())
            {
                meta.lore(List.of(Language.translate("ui.gimmick.sound.selected", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
                meta.addEnchant(Enchantment.INFINITY, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            stack.setItemMeta(meta);
            this.inventory.setItem(slots[i], stack);

            this.slots.put(slots[i], sound);

            i ++;
        }
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        super.onUiClick(event);
        event.setCancelled(true);

        if (event.getSlot() == 18)
        {
            new SoundGimmickUI(this.player, this.gimmick, Math.max(this.page - 1, 0));
        }

        if (event.getSlot() == 26)
        {
            new SoundGimmickUI(this.player, this.gimmick, Math.min(this.page + 1, SoundGimmickUI.sounds.size() / 21));
        }

        if (event.getSlot() == 40)
        {
            this.player.closeInventory();
        }

        SoundGimmick.Sound sound = this.slots.get(event.getSlot());

        if (sound == null)
        {
            return;
        }

        this.gimmick.setSound(sound);
        this.player.playSound(this.player, Sound.UI_BUTTON_CLICK, 1.0f, 1.2f);

        new SoundGimmickUI(this.player, this.gimmick, this.page);
    }
}
