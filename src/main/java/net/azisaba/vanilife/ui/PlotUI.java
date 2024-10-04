package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.HeadUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlotUI extends InventoryUI
{
    private final Plot plot;
    private final User user;

    private final BukkitRunnable runnable;

    public PlotUI(@NotNull Player player, @NotNull Plot plot)
    {
        super(player, Bukkit.createInventory(null, 45, Component.text(plot.getName()).decorate(TextDecoration.BOLD)));

        this.plot = plot;
        this.user = User.getInstance(this.player);

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            ItemStack ownerStack = HeadUtility.getPlayerHead(this.plot.getOwner().getPlaneName());
            ItemMeta ownerMeta = ownerStack.getItemMeta();
            ownerMeta.displayName((Sara.$2000YEN.level < this.plot.getOwner().getSara().level && this.plot.getName().contains("&") ? ComponentUtility.getAsComponent(this.plot.getName()) : Component.text(this.plot.getName()).color(NamedTextColor.GREEN))
                    .decoration(TextDecoration.ITALIC, false));
            ownerMeta.lore(List.of(Language.translate("ui.plot.owner", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(this.plot.getOwner().getName().decoration(TextDecoration.ITALIC, false))));
            ownerStack.setItemMeta(ownerMeta);
            this.inventory.setItem(4, ownerStack);
        });

        ItemStack memberStack = new ItemStack(Material.PLAYER_HEAD);
        this.inventory.setItem(20, memberStack);

        ItemStack spawnStack = new ItemStack(Material.ENDER_EYE);
        ItemMeta spawnMeta = spawnStack.getItemMeta();
        spawnMeta.displayName(Language.translate("ui.plot.spawn", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        spawnMeta.lore(List.of(Language.translate("ui.plot.spawn.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        spawnStack.setItemMeta(spawnMeta);
        this.inventory.setItem(21, spawnStack);

        ItemStack scopeStack = new ItemStack(Material.OAK_SIGN);
        ItemMeta scopeMeta = scopeStack.getItemMeta();
        scopeMeta.displayName(Language.translate("ui.plot.scope", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        scopeMeta.lore(List.of(Language.translate("ui.plot.scope.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.plot.scope." + this.plot.getScope().name().toLowerCase(), this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.plot.scope." + (this.plot.isMember(this.player) ? "inside" : "outside"), this.player).color(this.plot.isMember(this.player) ? NamedTextColor.GREEN : NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)));
        scopeStack.setItemMeta(scopeMeta);
        this.inventory.setItem(22, scopeStack);

        ItemStack joinStack = new ItemStack(Material.CHEST_MINECART);
        ItemMeta joinMeta = joinStack.getItemMeta();
        joinMeta.displayName(Language.translate("ui.plot.join", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        joinMeta.lore(List.of(Language.translate("ui.plot.join.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        joinStack.setItemMeta(joinMeta);

        ItemStack quitStack = new ItemStack(Material.TNT_MINECART);
        ItemMeta quitMeta = quitStack.getItemMeta();
        quitMeta.displayName(Language.translate("ui.plot.quit", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        quitMeta.lore(List.of(Language.translate("ui.plot.quit.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        quitStack.setItemMeta(quitMeta);

        this.inventory.setItem(23, (this.plot.getMembers().contains(this.user) ? quitStack : joinStack));

        ItemStack readmeStack = new ItemStack(Material.BOOK);
        ItemMeta readmeMeta = readmeStack.getItemMeta();
        readmeMeta.displayName(Language.translate("ui.plot.readme", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

        List<Component> readmeLore = new ArrayList<>();
        StringBuilder readmeBuilder = new StringBuilder();

        if (this.plot.getReadme() != null)
        {
            for (char c : this.plot.getReadme().toCharArray())
            {
                readmeBuilder.append(c);

                if (16 <= readmeBuilder.length())
                {
                    readmeLore.add(Component.text(readmeBuilder.toString()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                    readmeBuilder = new StringBuilder();
                }
            }

            if (readmeBuilder.length() < 16)
            {
                readmeLore.add(Component.text(readmeBuilder.toString()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            }
        }
        else
        {
            readmeLore.add(Language.translate("ui.unset", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        }

        readmeMeta.lore(readmeLore);
        readmeStack.setItemMeta(readmeMeta);
        this.inventory.setItem(24, readmeStack);

        ItemStack editStack = new ItemStack(Material.DIRT);
        ItemMeta editMeta = editStack.getItemMeta();
        editMeta.displayName(Language.translate("ui.plot.edit", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        editMeta.lore(List.of(Language.translate("ui.plot.edit.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.plot." + ((this.plot.getMembers().contains(this.user) && this.plot.canEdit()) ? "can": "cant"), this.player).color(((this.plot.getMembers().contains(this.user) && this.plot.canEdit()) ? NamedTextColor.GREEN : NamedTextColor.RED)).decoration(TextDecoration.ITALIC, false)));
        editStack.setItemMeta(editMeta);
        this.inventory.setItem(30, editStack);

        ItemStack chestStack = new ItemStack(Material.CHEST);
        ItemMeta chestMeta = chestStack.getItemMeta();
        chestMeta.displayName(Language.translate("ui.plot.chest", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        chestMeta.lore(List.of(Language.translate("ui.plot.chest.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.plot." + ((this.plot.getMembers().contains(this.user) && this.plot.canChest()) ? "can": "cant"), this.player).color(((this.plot.getMembers().contains(this.user) && this.plot.canChest()) ? NamedTextColor.GREEN : NamedTextColor.RED)).decoration(TextDecoration.ITALIC, false)));
        chestStack.setItemMeta(chestMeta);
        this.inventory.setItem(31, chestStack);

        ItemStack pvpStack = new ItemStack(Material.BOW);
        ItemMeta pvpMeta = pvpStack.getItemMeta();
        pvpMeta.displayName(Language.translate("ui.plot.pvp", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        pvpMeta.lore(List.of(Language.translate("ui.plot.pvp.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.plot." + ((this.plot.getMembers().contains(this.user) && this.plot.canPvP()) ? "can": "cant"), this.player).color(((this.plot.getMembers().contains(this.user) && this.plot.canPvP()) ? NamedTextColor.GREEN : NamedTextColor.RED)).decoration(TextDecoration.ITALIC, false)));
        pvpStack.setItemMeta(pvpMeta);
        this.inventory.setItem(32, pvpStack);

        this.runnable = new BukkitRunnable()
        {
            private int i = 0;

            @Override
            public void run()
            {
                if (plot.getMembers().size() <= this.i)
                {
                    this.i = 0;
                }

                User member = plot.getMembers().get(this.i);

                ItemStack memberStack = HeadUtility.getPlayerHead(member.getPlaneName());
                ItemMeta memberMeta = memberStack.getItemMeta();

                memberMeta.displayName(Language.translate("ui.plot.member", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

                List<Component> memberLore = new ArrayList<>();

                memberLore.add(Language.translate("ui.plot.member.details", player, "members=" + plot.getMembers().size()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                memberLore.add(Component.text().build());

                for (User m : plot.getMembers())
                {
                    memberLore.add(m.getName().decoration(TextDecoration.ITALIC, false));
                }

                memberMeta.lore(memberLore);

                memberStack.setItemMeta(memberMeta);

                Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> inventory.setItem(20, memberStack));

                this.i ++;

                if (plot.getMembers().size() == 1)
                {
                    this.cancel();
                }
            }
        };

        this.runnable.runTaskTimer(Vanilife.getPlugin(), 0L, 10L);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getSlot() == 21)
        {
            if (! this.plot.isMember(this.player))
            {
                this.player.sendMessage(Language.translate("ui.plot.spawn.cant", this.player).color(NamedTextColor.RED));
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            Bukkit.dispatchCommand(this.player, "ptp " + this.plot.getName());
        }

        if (event.getSlot() == 23)
        {
            this.player.closeInventory();
            Bukkit.dispatchCommand(this.player, "/plot " + (this.plot.getMembers().contains(this.user) ? "quit" : "join") + " " + this.plot.getName());
        }
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event)
    {
        super.onClose(event);
        this.runnable.cancel();
    }
}
