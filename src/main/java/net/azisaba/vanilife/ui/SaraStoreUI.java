package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.user.Sara;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaraStoreUI extends ChestUI
{
    public SaraStoreUI(@NotNull Player player)
    {
        super(player, Bukkit.createInventory(null, 54, Language.translate("ui.sara.title", player)));

        ItemStack saraStack = new ItemStack(Material.BOOK);
        ItemMeta saraMeta = saraStack.getItemMeta();
        saraMeta.displayName(Language.translate("ui.sara.buy", player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        saraMeta.lore(List.of(Component.text().build(), Component.text("https://azisaba.buycraft.net/category/rank").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        saraStack.setItemMeta(saraMeta);

        ItemStack gamingSaraStack = new ItemStack(Material.BOOK);
        ItemMeta gamingSaraMeta = gamingSaraStack.getItemMeta();
        gamingSaraMeta.displayName(Language.translate("ui.sara.buy", player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        gamingSaraMeta.lore(List.of(Component.text().build(), Component.text("https://azisaba.buycraft.net/category/gamingrank").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        gamingSaraStack.setItemMeta(gamingSaraMeta);

        ItemStack $100Stack = new ItemStack(Material.IRON_INGOT);
        ItemMeta $100Meta = $100Stack.getItemMeta();
        $100Meta.displayName(Language.translate("ui.sara.100", player).color(Sara.$100YEN.getColor()));

        List<Component> $100Lore = new ArrayList<>();

        if (Language.has("ui.sara.100.1", player))
        {
            int i = 1;

            while (Language.has("ui.sara.100." + i, player))
            {
                $100Lore.add(Language.translate("ui.sara.100." + i, player).decoration(TextDecoration.ITALIC, false));
                i ++;
            }

            $100Lore.add(Component.text().build());
        }

        $100Meta.lore($100Lore);

        $100Stack.setItemMeta($100Meta);
        this.inventory.setItem(1, $100Stack);
        this.inventory.setItem(1 + 9, saraStack);

        ItemStack $500Stack = new ItemStack(Material.IRON_INGOT);
        ItemMeta $500Meta = $500Stack.getItemMeta();
        $500Meta.displayName(Language.translate("ui.sara.500", player).color(Sara.$500YEN.getColor()));
        $500Meta.addEnchant(Enchantment.INFINITY, 1, false);
        $500Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<Component> $500Lore = new ArrayList<>();

        if (Language.has("ui.sara.500.1", player))
        {
            int i = 1;

            while (Language.has("ui.sara.500." + i, player))
            {
                $500Lore.add(Language.translate("ui.sara.500." + i, player).decoration(TextDecoration.ITALIC, false));
                i ++;
            }

            $500Lore.add(Component.text().build());
        }

        $500Meta.lore($500Lore);

        $500Stack.setItemMeta($500Meta);
        this.inventory.setItem(3, $500Stack);
        this.inventory.setItem(3 + 9, saraStack);

        ItemStack $1000Stack = new ItemStack(Material.GOLD_INGOT);
        ItemMeta $1000Meta = $1000Stack.getItemMeta();
        $1000Meta.displayName(Language.translate("ui.sara.1000", player).color(Sara.$1000YEN.getColor()));

        List<Component> $1000Lore = new ArrayList<>();

        if (Language.has("ui.sara.1000.1", player))
        {
            int i = 1;

            while (Language.has("ui.sara.1000." + i, player))
            {
                $1000Lore.add(Language.translate("ui.sara.1000." + i, player).decoration(TextDecoration.ITALIC, false));
                i ++;
            }

            $1000Lore.add(Component.text().build());
        }

        $1000Meta.lore($1000Lore);

        $1000Stack.setItemMeta($1000Meta);
        this.inventory.setItem(5, $1000Stack);
        this.inventory.setItem(5 + 9, saraStack);

        ItemStack $2000Stack = new ItemStack(Material.GOLD_INGOT);
        ItemMeta $2000Meta = $2000Stack.getItemMeta();
        $2000Meta.displayName(Language.translate("ui.sara.2000", player).color(Sara.$2000YEN.getColor()));
        $2000Meta.addEnchant(Enchantment.INFINITY, 1, false);
        $2000Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<Component> $2000Lore = new ArrayList<>();

        if (Language.has("ui.sara.2000.1", player))
        {
            int i = 1;

            while (Language.has("ui.sara.2000." + i, player))
            {
                $2000Lore.add(Language.translate("ui.sara.2000." + i, player).decoration(TextDecoration.ITALIC, false));
                i ++;
            }

            $2000Lore.add(Component.text().build());
        }

        $2000Meta.lore($2000Lore);

        $2000Stack.setItemMeta($2000Meta);
        this.inventory.setItem(7, $2000Stack);
        this.inventory.setItem(7 + 9, saraStack);

        ItemStack $5000Stack = new ItemStack(Material.EMERALD);
        ItemMeta $5000Meta = $5000Stack.getItemMeta();
        $5000Meta.displayName(Language.translate("ui.sara.5000", player).color(Sara.$5000YEN.getColor()));

        List<Component> $5000Lore = new ArrayList<>();

        if (Language.has("ui.sara.5000.1", player))
        {
            int i = 1;

            while (Language.has("ui.sara.5000." + i, player))
            {
                $5000Lore.add(Language.translate("ui.sara.5000." + i, player).decoration(TextDecoration.ITALIC, false));
                i ++;
            }

            $5000Lore.add(Component.text().build());
        }

        $5000Meta.lore($5000Lore);

        $5000Stack.setItemMeta($5000Meta);
        this.inventory.setItem(28, $5000Stack);
        this.inventory.setItem(28 + 9, saraStack);

        ItemStack $10000Stack = new ItemStack(Material.EMERALD);
        ItemMeta $10000Meta = $10000Stack.getItemMeta();
        $10000Meta.displayName(Language.translate("ui.sara.10000", player).color(Sara.$10000YEN.getColor()));
        $10000Meta.addEnchant(Enchantment.INFINITY, 1, false);
        $10000Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<Component> $10000Lore = new ArrayList<>();

        if (Language.has("ui.sara.10000.1", player))
        {
            int i = 1;

            while (Language.has("ui.sara.10000." + i, player))
            {
                $10000Lore.add(Language.translate("ui.sara.10000." + i, player).decoration(TextDecoration.ITALIC, false));
                i ++;
            }

            $10000Lore.add(Component.text().build());
        }

        $10000Meta.lore($10000Lore);

        $10000Stack.setItemMeta($10000Meta);
        this.inventory.setItem(30, $10000Stack);
        this.inventory.setItem(30 + 9, saraStack);

        ItemStack $50000Stack = new ItemStack(Material.DIAMOND);
        ItemMeta $50000Meta = $50000Stack.getItemMeta();
        $50000Meta.displayName(Language.translate("ui.sara.50000", player).color(Sara.$50000YEN.getColor()));

        List<Component> $50000Lore = new ArrayList<>();

        if (Language.has("ui.sara.50000.1", player))
        {
            int i = 1;

            while (Language.has("ui.sara.50000." + i, player))
            {
                $50000Lore.add(Language.translate("ui.sara.50000." + i, player).decoration(TextDecoration.ITALIC, false));
                i ++;
            }

            $50000Lore.add(Component.text().build());
        }

        $50000Meta.lore($50000Lore);

        $50000Stack.setItemMeta($50000Meta);
        this.inventory.setItem(32, $50000Stack);
        this.inventory.setItem(32 + 9, saraStack);

        ItemStack gamingStack = new ItemStack(Material.DIAMOND);
        ItemMeta gamingMeta = gamingStack.getItemMeta();
        gamingMeta.displayName(Language.translate("ui.sara.gaming", player).color(Sara.GAMING.getColor()));
        gamingMeta.addEnchant(Enchantment.INFINITY, 1, false);
        gamingMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<Component> gamingLore = new ArrayList<>();

        if (Language.has("ui.sara.gaming.1", player))
        {
            int i = 1;

            while (Language.has("ui.sara.gaming." + i, player))
            {
                gamingLore.add(Language.translate("ui.sara.gaming." + i, player).decoration(TextDecoration.ITALIC, false));
                i ++;
            }

            gamingLore.add(Component.text().build());
        }

        gamingMeta.lore(gamingLore);

        gamingStack.setItemMeta(gamingMeta);
        this.inventory.setItem(34, gamingStack);
        this.inventory.setItem(34 + 9, gamingSaraStack);

        ItemStack returnStack = new ItemStack(Material.ARROW);
        ItemMeta returnMeta = returnStack.getItemMeta();
        returnMeta.displayName(Language.translate("ui.return", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        returnMeta.lore(List.of(Language.translate("ui.return.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        returnStack.setItemMeta(returnMeta);
        this.registerListener(48, returnStack, "vanilife:store", ExecutionType.CLIENT);

        ItemStack closeStack = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(49, closeStack);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        final int[] sara = {1 + 9, 3 + 9, 5 + 9, 7 + 9, 28 + 9, 30 + 9};
        final int[] gaming = {32 + 9, 34 + 9};

        if (Arrays.stream(sara).anyMatch(s -> s == event.getSlot()))
        {
            this.player.closeInventory();

            this.player.sendMessage(Component.text().build());
            this.player.sendMessage(Language.translate("msg.click-to-open", this.player).color(NamedTextColor.YELLOW).append(Component.text(":")));
            this.player.sendMessage(Component.text("https://azisaba.buycraft.net/category/rank").color(NamedTextColor.BLUE).hoverEvent(HoverEvent.showText(Component.text("Click here!"))).clickEvent(ClickEvent.openUrl("https://azisaba.buycraft.net/category/rank")));
            this.player.sendMessage(Component.text().build());
        }

        if (Arrays.stream(gaming).anyMatch(s -> s == event.getSlot()))
        {
            this.player.closeInventory();
            this.player.sendMessage(Component.text().build());
            this.player.sendMessage(Language.translate("msg.click-to-open", this.player).color(NamedTextColor.YELLOW).append(Component.text(":")));
            this.player.sendMessage(Component.text("https://azisaba.buycraft.net/category/gamingrank").color(NamedTextColor.BLUE).hoverEvent(HoverEvent.showText(Component.text("Click here!"))).clickEvent(ClickEvent.openUrl("https://azisaba.buycraft.net/category/gamingrank")));
            this.player.sendMessage(Component.text().build());
        }

        if (event.getSlot() == 49)
        {
            this.player.closeInventory();
        }
    }
}
