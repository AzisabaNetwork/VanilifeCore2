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

import java.util.ArrayList;
import java.util.List;

public class PollUI extends InventoryUI
{
    private final List<String> options;

    private boolean multiple = false;
    private boolean anonymity = false;
    private int limit = 30;

    public PollUI(Player player, String... options)
    {
        super(player, Bukkit.createInventory(null, 27, Component.text("投票の作成")));
        this.options = List.of(options);

        ItemStack multipleStack = new ItemStack(Material.PAPER);
        ItemMeta multipleMeta = multipleStack.getItemMeta();
        multipleMeta.displayName(Component.text("複数回答").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        multipleMeta.lore(List.of(Component.text("状態: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text("無効").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false))));
        multipleStack.setItemMeta(multipleMeta);
        this.inventory.setItem(9, multipleStack);

        ItemStack anonymityStack = new ItemStack(Material.NAME_TAG);
        ItemMeta anonymityMeta = anonymityStack.getItemMeta();
        anonymityMeta.displayName(Component.text("匿名").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        anonymityMeta.lore(List.of(Component.text("状態: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text("無効").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false))));
        anonymityStack.setItemMeta(anonymityMeta);
        this.inventory.setItem(10, anonymityStack);

        ItemStack limitStack = new ItemStack(Material.CLOCK);
        ItemMeta limitMeta = limitStack.getItemMeta();
        limitMeta.displayName(Component.text("制限時間").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        limitMeta.lore(List.of(Component.text("30秒").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Component.text("1分").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("2分").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        limitStack.setItemMeta(limitMeta);
        this.inventory.setItem(11, limitStack);

        ItemStack startStack = new ItemStack(Material.OAK_SIGN);
        ItemMeta startMeta = startStack.getItemMeta();
        startMeta.displayName(Component.text("投票を開始").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        startMeta.lore(List.of(Component.text("この設定で投票を作成します").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        startStack.setItemMeta(startMeta);
        this.inventory.setItem(17, startStack);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event)
    {
        super.onInventoryClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getSlot() != 17)
        {
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0F, 1.2F);
        }

        if (event.getSlot() == 9)
        {
            this.multiple = ! this.multiple;

            ItemStack multipleStack = this.inventory.getItem(9);
            ItemMeta multipleMeta = multipleStack.getItemMeta();
            multipleMeta.lore(List.of(Component.text("状態: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(this.multiple ? "有効" : "無効").color(this.multiple ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false))));
            multipleStack.setItemMeta(multipleMeta);
        }

        if (event.getSlot() == 10)
        {
            this.anonymity = ! this.anonymity;

            ItemStack anonymityStack = this.inventory.getItem(10);
            ItemMeta anonymityMeta = anonymityStack.getItemMeta();
            anonymityMeta.lore(List.of(Component.text("状態: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(this.anonymity ? "有効" : "無効").color(this.anonymity ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false))));
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
            lore.add(Component.text("30秒").color((this.limit == 30) ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("1分").color((this.limit == 60) ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("2分").color((this.limit == 120) ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));

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
