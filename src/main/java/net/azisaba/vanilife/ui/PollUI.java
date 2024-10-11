package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.poll.Poll;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PollUI extends ChestUI
{
    private final List<String> options;

    private boolean multiple = false;
    private boolean anonymity = false;
    private int limit = 30;

    public PollUI(@NotNull Player player, String... options)
    {
        super(player, Bukkit.createInventory(null, 27, Language.translate("ui.poll.title", player)));
        this.options = List.of(options);

        ItemStack multipleStack = new ItemStack(Material.PAPER);
        ItemMeta multipleMeta = multipleStack.getItemMeta();
        multipleMeta.displayName(Language.translate("ui.poll.multiple", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        multipleMeta.lore(List.of(Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate("ui.false", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))));
        multipleStack.setItemMeta(multipleMeta);
        this.inventory.setItem(9, multipleStack);

        ItemStack anonymityStack = new ItemStack(Material.NAME_TAG);
        ItemMeta anonymityMeta = anonymityStack.getItemMeta();
        anonymityMeta.displayName(Language.translate("ui.poll.anonymity", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        anonymityMeta.lore(List.of(Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate("ui.false", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))));
        anonymityStack.setItemMeta(anonymityMeta);
        this.inventory.setItem(10, anonymityStack);

        ItemStack limitStack = new ItemStack(Material.CLOCK);
        ItemMeta limitMeta = limitStack.getItemMeta();
        limitMeta.displayName(Language.translate("ui.poll.limit", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        limitMeta.lore(List.of(Language.translate("ui.poll.limit.30s", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.poll.limit.1m", this.player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.poll.limit.2m", this.player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        limitStack.setItemMeta(limitMeta);
        this.inventory.setItem(11, limitStack);

        ItemStack startStack = new ItemStack(Material.OAK_SIGN);
        ItemMeta startMeta = startStack.getItemMeta();
        startMeta.displayName(Language.translate("ui.poll.start", this.player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        startMeta.lore(List.of(Language.translate("ui.poll.start.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        startStack.setItemMeta(startMeta);
        this.inventory.setItem(17, startStack);
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event)
    {
        super.onClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getSlot() != 17 && event.getClickedInventory() == this.inventory)
        {
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0F, 1.2F);
        }

        if (event.getSlot() == 9)
        {
            this.multiple = ! this.multiple;

            ItemStack multipleStack = this.inventory.getItem(9);
            ItemMeta multipleMeta = multipleStack.getItemMeta();
            multipleMeta.lore(List.of(Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append((this.multiple ? Language.translate("ui.true", this.player) : Language.translate("ui.false", this.player)).color(this.multiple ? NamedTextColor.GREEN : NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))));
            multipleStack.setItemMeta(multipleMeta);
        }

        if (event.getSlot() == 10)
        {
            this.anonymity = ! this.anonymity;

            ItemStack anonymityStack = this.inventory.getItem(10);
            ItemMeta anonymityMeta = anonymityStack.getItemMeta();
            anonymityMeta.lore(List.of(Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append((this.multiple ? Language.translate("ui.true", this.player) : Language.translate("ui.false", this.player)).color(this.anonymity ? NamedTextColor.GREEN : NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))));
            anonymityStack.setItemMeta(anonymityMeta);
        }

        if (event.getSlot() == 11)
        {
            this.limit = switch (this.limit)
            {
                case 30 -> 60;
                case 60 -> 120;
                default -> 30;
            };

            ItemStack limitStack = this.inventory.getItem(11);
            ItemMeta limitMeta = limitStack.getItemMeta();

            List<Component> lore = new ArrayList<>();
            lore.add(Language.translate("ui.poll.limit.30s", this.player).color((this.limit == 30) ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Language.translate("ui.poll.limit.1m", this.player).color((this.limit == 60) ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Language.translate("ui.poll.limit.2m", this.player).color((this.limit == 120) ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));

            limitMeta.lore(lore);
            limitStack.setItemMeta(limitMeta);
        }

        if (event.getSlot() == 17)
        {
            this.player.closeInventory();
            new Poll(this.player, this.multiple, this.anonymity, this.limit, this.options.toArray(new String[0]));
        }
    }
}
