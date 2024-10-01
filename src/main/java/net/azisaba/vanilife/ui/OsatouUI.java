package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.HeadUtility;
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

import java.util.List;

public class OsatouUI extends InventoryUI
{
    private final User user;

    public OsatouUI(@NotNull Player player)
    {
        super(player, Bukkit.createInventory(null, 45, Language.translate("ui.osatou.title", player)));

        this.user = User.getInstance(player);

        ItemStack closeStack = new ItemStack(Material.ARROW);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(40, closeStack);

        if (! this.user.hasOsatou())
        {
            ItemStack emptyStack = new ItemStack(Material.BEDROCK);
            ItemMeta emptyMeta = emptyStack.getItemMeta();
            emptyMeta.displayName(Language.translate("ui.osatou.empty", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            emptyMeta.lore(List.of(Language.translate("ui.osatou.empty.details.1", this.player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                    Language.translate("ui.osatou.empty.details.2", this.player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
            emptyStack.setItemMeta(emptyMeta);
            this.inventory.setItem(22, emptyStack);
            return;
        }

        User osatou = this.user.getOsatou();

        Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> {
            ItemStack headStack = HeadUtility.getPlayerHead(osatou.getPlaneName());
            ItemMeta headMeta = headStack.getItemMeta();
            headMeta.displayName(osatou.getName().decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            headMeta.lore(List.of(Language.translate("ui.osatou.profile", this.player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)));
            headStack.setItemMeta(headMeta);
            this.registerListener(20, headStack, "vanilife:profile " + osatou.getPlaneName(), ExecutionType.CLIENT);
        });

        boolean open1 = this.user.read("settings.osatou.open").getAsBoolean();
        boolean open2 = osatou.read("settings.osatou.open").getAsBoolean();

        boolean open = open1 && open2;

        ItemStack openStack = new ItemStack(Material.OAK_SIGN);
        ItemMeta openMeta = openStack.getItemMeta();
        openMeta.displayName(Language.translate("ui.osatou.open", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        openMeta.lore(List.of(Language.translate("ui.osatou.open.details.1", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.osatou.open.details.2", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate(open ? "ui.true" : "ui.false", this.player).color(open ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY)),
                Component.text().build(),
                Language.translate("ui.osatou.open.you", this.player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate(open1 ? "ui.true" : "ui.false", this.player).color(open1 ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY)),
                Language.translate("ui.osatou.open.partner", this.player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate(open2 ? "ui.true" : "ui.false", this.player).color(open2 ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY))));
        openStack.setItemMeta(openMeta);
        this.inventory.setItem(22, openStack);

        ItemStack deleteStack = new ItemStack(Material.TNT_MINECART);
        ItemMeta deleteMeta = deleteStack.getItemMeta();
        deleteMeta.displayName(Language.translate("ui.osatou.delete", this.player).color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        deleteMeta.lore(List.of(Language.translate("ui.osatou.delete.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        deleteStack.setItemMeta(deleteMeta);
        this.inventory.setItem(24, deleteStack);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getSlot() == 40)
        {
            this.player.closeInventory();
        }

        if (! this.user.hasOsatou())
        {
            return;
        }

        if (event.getSlot() == 22)
        {
            boolean open = this.user.read("settings.osatou.open").getAsBoolean();
            this.user.write("settings.osatou.open", ! open);
            this.player.playSound(this.player, Sound.UI_BUTTON_CLICK, 1.0f, 1.2f);
            new OsatouUI(this.player);
        }

        if (event.getSlot() == 24)
        {
            new ConfirmUI(this.player, () -> {
                this.player.closeInventory();
                this.player.sendMessage(Language.translate("ui.osatou.delete.deleted", player, "name=" + this.user.getOsatou().getPlaneName()).color(NamedTextColor.GREEN));
                this.user.setOsatou(null);
            }, () -> new OsatouUI(this.player));
        }
    }
}
