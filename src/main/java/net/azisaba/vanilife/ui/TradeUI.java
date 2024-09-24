package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.trade.Trade;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class TradeUI extends InventoryUI
{
    private final Trade trade;
    private final Player player;
    private final Player partner;

    public TradeUI(@NotNull Player player, @NotNull Trade trade)
    {
        super(player, Bukkit.createInventory(null, 54, Language.translate("ui.trade.title", player)));

        this.trade = trade;
        this.player = player;
        this.partner = (this.trade.getPlayer1() == this.player) ? this.trade.getPlayer2() : this.trade.getPlayer1();

        ItemStack profile1Stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta profile1Meta = (SkullMeta) profile1Stack.getItemMeta();
        profile1Meta.displayName(this.player.displayName().decoration(TextDecoration.ITALIC, false));
        profile1Meta.setOwningPlayer(this.player);
        profile1Stack.setItemMeta(profile1Meta);
        this.inventory.setItem(0, profile1Stack);

        ItemStack agree1Stack = new ItemStack(this.trade.getAgree(this.player).favicon);
        ItemMeta agree1Meta = agree1Stack.getItemMeta();
        agree1Meta.displayName(Language.translate(this.trade.getAgree(this.player).translate, this.player).decoration(TextDecoration.ITALIC, false));
        agree1Stack.setItemMeta(agree1Meta);
        this.inventory.setItem(1, agree1Stack);

        ItemStack profile2Stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta profile2Meta = (SkullMeta) profile2Stack.getItemMeta();
        profile2Meta.displayName(this.partner.displayName().decoration(TextDecoration.ITALIC, false));
        profile2Meta.setOwningPlayer(this.partner);
        profile2Stack.setItemMeta(profile2Meta);
        this.inventory.setItem(5, profile2Stack);

        ItemStack agree2Stack = new ItemStack(this.trade.getAgree(this.partner).favicon);
        ItemMeta agree2Meta = agree2Stack.getItemMeta();
        agree2Meta.displayName(Language.translate(this.trade.getAgree(this.partner).translate, this.player).decoration(TextDecoration.ITALIC, false));
        agree2Stack.setItemMeta(agree2Meta);
        this.inventory.setItem(6, agree2Stack);

        ItemStack agreeStack = new ItemStack(Material.RED_DYE);
        ItemMeta agreeMeta = agreeStack.getItemMeta();
        agreeMeta.displayName(Component.text(""));

        ItemStack separatorStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta separatorMeta = separatorStack.getItemMeta();
        separatorMeta.displayName(Component.text(""));
        separatorStack.setItemMeta(separatorMeta);

        this.inventory.setItem(4, separatorStack);
        this.inventory.setItem(4 + 9, separatorStack);
        this.inventory.setItem(4 + 9 * 2, separatorStack);
        this.inventory.setItem(4 + 9 * 3, separatorStack);
        this.inventory.setItem(4 + 9 * 4, separatorStack);
        this.inventory.setItem(4 + 9 * 5, separatorStack);
    }

    @NotNull
    public Player getPlayer()
    {
        return this.player;
    }

    @NotNull
    public Player getPartner()
    {
        return this.partner;
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getSlot() == 1)
        {
            if (this.trade.getAgree(this.player) == Trade.Agree.NONE)
            {
                this.trade.setAgree(this.player, Trade.Agree.CHECK);
            }
            else if (this.trade.getAgree(this.player) == Trade.Agree.CHECK && this.trade.getAgree(this.partner) == Trade.Agree.NONE)
            {
                this.trade.setAgree(this.player, Trade.Agree.NONE);
            }
            else if (this.trade.getAgree(this.player) == Trade.Agree.CHECK && (this.trade.getAgree(this.partner) == Trade.Agree.CHECK || this.trade.getAgree(this.partner) == Trade.Agree.SET))
            {
                this.trade.setAgree(this.player, Trade.Agree.SET);
            }

            if (this.trade.getAgree())
            {
                this.trade.trade();
            }

            Trade.Agree agree = this.trade.getAgree(this.player);
            ItemStack stack = new ItemStack(agree.favicon);
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(Language.translate(agree.translate, this.player));
            stack.setItemMeta(meta);
            this.inventory.setItem(1, stack);
        }
    }

    @Override
    public void onInventoryClick(@NotNull InventoryClickEvent event)
    {
        event.setCancelled(event.isCancelled()
                || this.trade.getAgree((Player) event.getWhoClicked()) != Trade.Agree.NONE
                || (! Trade.CONTROL.contains(event.getRawSlot()) && event.getClickedInventory() == this.inventory));

        if (! event.isShiftClick() || event.getClickedInventory() == this.inventory)
        {
            return;
        }

        ItemStack stack = event.getCurrentItem();

        if (stack == null || stack.getType() == Material.AIR)
        {
            return;
        }

        boolean b = false;

        for (int i : Trade.CONTROL)
        {
            ItemStack j = this.inventory.getItem(i);

            if (j == null || j.getType() == Material.AIR)
            {
                this.inventory.setItem(i, stack);
                b = true;
                break;
            }
        }

        if (b)
        {
            event.getWhoClicked().getInventory().remove(stack);
        }

        event.setCancelled(true);
    }

    @Override
    public void onInventoryClose(@NotNull InventoryCloseEvent event)
    {
        super.onInventoryClose(event);

        if (! this.trade.getAgree())
        {
            this.trade.cancel();
        }
    }
}
