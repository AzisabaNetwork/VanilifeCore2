package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VanilifeStoreUI extends InventoryUI
{
    public VanilifeStoreUI(@NotNull Player player)
    {
        super(player, Bukkit.createInventory(null, 36, Language.translate("ui.store.title", player)));

        User user = User.getInstance(player);

        ItemStack saraStack = new ItemStack(Material.GOLD_INGOT);
        ItemMeta saraMeta = saraStack.getItemMeta();
        saraMeta.displayName(Language.translate("ui.store.sara", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        saraMeta.lore(List.of(Language.translate("ui.store.sara.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.store.sara.current", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(user.getSara() == Sara.DEFAULT ? Language.translate("ui.store.sara.default", player) : user.getSara().role),
                Component.text().build(),
                Language.translate("ui.click-to-view", player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)));
        saraMeta.addEnchant(Enchantment.INFINITY, 1, false);
        saraMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        saraStack.setItemMeta(saraMeta);
        this.inventory.setItem(10, saraStack);

        ItemStack vanilife365Stack = new ItemStack(Material.NETHER_STAR);
        ItemMeta vanilife365Meta = vanilife365Stack.getItemMeta();
        vanilife365Meta.displayName(Language.translate("ui.store.365", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        vanilife365Meta.lore(List.of(Language.translate("ui.store.365.details.1", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.store.365.details.2", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.click-to-view", player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)));
        vanilife365Stack.setItemMeta(vanilife365Meta);
        this.inventory.setItem(12, vanilife365Stack);

        ItemStack blockStack = new ItemStack(Material.BRICKS);
        ItemMeta blockMeta = blockStack.getItemMeta();
        blockMeta.displayName(Language.translate("ui.store.block", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        blockMeta.lore(List.of(Language.translate("ui.store.block.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.click-to-view", player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)));
        blockStack.setItemMeta(blockMeta);
        this.inventory.setItem(14, blockStack);

        ItemStack walletStack = new ItemStack(Material.BOOK);
        ItemMeta walletMeta = walletStack.getItemMeta();
        walletMeta.displayName(Language.translate("ui.store.wallet", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        walletMeta.lore(List.of(Language.translate("ui.store.wallet.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.store.wallet.help.1", player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.store.wallet.help.2", player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.click-to-view", player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)));
        walletMeta.addEnchant(Enchantment.INFINITY, 1, false);
        walletMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        walletStack.setItemMeta(walletMeta);
        this.registerListener(16, walletStack, "vanilife:wallet", ExecutionType.CLIENT);

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

        if (event.getSlot() == 10)
        {
            new SaraStoreUI(this.player);
        }

        if (event.getSlot() == 12)
        {
            new Vanilife365UI(this.player);
        }

        if (event.getSlot() == 14)
        {
            new HousingStoreUI(this.player);
        }

        if (event.getSlot() == 31)
        {
            this.player.closeInventory();
        }
    }
}
