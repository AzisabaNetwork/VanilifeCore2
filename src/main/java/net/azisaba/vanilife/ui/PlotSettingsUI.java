package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.plot.PlotScope;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.HeadUtility;
import net.azisaba.vanilife.util.Typing;
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

public class PlotSettingsUI extends InventoryUI
{
    private final Plot plot;
    private final BukkitRunnable runnable;

    public PlotSettingsUI(@NotNull Player player, @NotNull Plot plot)
    {
        super(player, Bukkit.createInventory(null, 45, Component.text(plot.getName()).decorate(TextDecoration.BOLD)));
        this.plot = plot;

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            ItemStack ownerStack = HeadUtility.getPlayerHead(this.plot.getOwner().getPlaneName());
            ItemMeta ownerMeta = ownerStack.getItemMeta();
            ownerMeta.displayName(Component.text(this.plot.getName()).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            ownerMeta.lore(List.of(Language.translate("ui.plot.owner", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(this.plot.getOwner().getName())));
            ownerStack.setItemMeta(ownerMeta);
            this.inventory.setItem(4, ownerStack);
        });

        ItemStack renameStack = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = renameStack.getItemMeta();
        renameMeta.displayName(Language.translate("ui.plot-settings.rename", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        renameMeta.lore(List.of(Language.translate("ui.plot-settings.rename.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        renameStack.setItemMeta(renameMeta);
        this.inventory.setItem(21, renameStack);

        ItemStack spawnStack = new ItemStack(Material.ENDER_EYE);
        ItemMeta spawnMeta = spawnStack.getItemMeta();
        spawnMeta.displayName(Language.translate("ui.plot-settings.spawn", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        spawnMeta.lore(List.of(Language.translate("ui.plot-settings.spawn.details.1", player).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.plot-settings.spawn.details.2", player).decoration(TextDecoration.ITALIC, false)));
        spawnStack.setItemMeta(spawnMeta);
        this.inventory.setItem(22, spawnStack);

        ItemStack readmeStack = new ItemStack(Material.BOOK);
        ItemMeta readmeMeta = readmeStack.getItemMeta();
        readmeMeta.displayName(Language.translate("ui.plot-settings.readme", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

        List<Component> readmeLore = new ArrayList<>();
        readmeLore.add(Language.translate("ui.plot-settings.readme.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        StringBuilder readmeBuilder = new StringBuilder();

        if (this.plot.getReadme() != null)
        {
            readmeLore.add(Component.text().build());

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

        readmeMeta.lore(readmeLore);

        readmeStack.setItemMeta(readmeMeta);
        this.inventory.setItem(23, readmeStack);

        ItemStack deleteStack = new ItemStack(Material.TNT_MINECART);
        ItemMeta deleteMeta = deleteStack.getItemMeta();
        deleteMeta.displayName(Language.translate("ui.plot-settings.delete", player).color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        deleteMeta.lore(List.of(Language.translate("ui.plot-settings.delete.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        deleteStack.setItemMeta(deleteMeta);
        this.inventory.setItem(24, deleteStack);

        ItemStack scopeStack = new ItemStack(Material.COMPASS);
        ItemMeta scopeMeta = spawnMeta.clone();
        scopeMeta.displayName(Language.translate("ui.plot-settings.scope", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        scopeMeta.lore(List.of(Language.translate("ui.plot-settings.scope.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("settings.scope.public", player).color(this.plot.getScope() == PlotScope.PUBLIC ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("settings.scope.friend", player).color(this.plot.getScope() == PlotScope.FRIEND ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("settings.scope.private", player).color(this.plot.getScope() == PlotScope.PRIVATE ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        scopeStack.setItemMeta(scopeMeta);
        this.inventory.setItem(29, scopeStack);

        ItemStack editStack = new ItemStack(Material.DIRT);
        ItemMeta editMeta = editStack.getItemMeta();
        editMeta.displayName(Language.translate("ui.plot.edit", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        editMeta.lore(List.of(Language.translate("ui.plot.edit.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.state", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate(this.plot.canEdit() ? "ui.true" : "ui.false", player).color(this.plot.canEdit() ? NamedTextColor.GREEN : NamedTextColor.RED))));
        editStack.setItemMeta(editMeta);
        this.inventory.setItem(30, editStack);

        ItemStack chestStack = new ItemStack(Material.CHEST);
        ItemMeta chestMeta = chestStack.getItemMeta();
        chestMeta.displayName(Language.translate("ui.plot.chest", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        chestMeta.lore(List.of(Language.translate("ui.plot.chest.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.state", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate(this.plot.canChest() ? "ui.true" : "ui.false", player).color(this.plot.canChest() ? NamedTextColor.GREEN : NamedTextColor.RED))));
        chestStack.setItemMeta(chestMeta);
        this.inventory.setItem(31, chestStack);

        ItemStack pvpStack = new ItemStack(Material.BOW);
        ItemMeta pvpMeta = pvpStack.getItemMeta();
        pvpMeta.displayName(Language.translate("ui.plot.pvp", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        pvpMeta.lore(List.of(Language.translate("ui.plot.pvp.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.state", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate(this.plot.canPvP() ? "ui.true" : "ui.false", player).color(this.plot.canPvP() ? NamedTextColor.GREEN : NamedTextColor.RED))));
        pvpStack.setItemMeta(pvpMeta);
        this.inventory.setItem(32, pvpStack);

        this.runnable = new BukkitRunnable()
        {
            private int i = 0;

            @Override
            public void run()
            {
                if (plot.getMembers().isEmpty())
                {
                    plot.addMember(plot.getOwner());
                }

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
                memberLore.add(Language.translate("ui.plot.member.help.1", player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
                memberLore.add(Language.translate("ui.plot.member.help.2", player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
                memberLore.add(Component.text().build());

                for (User m : plot.getMembers())
                {
                    memberLore.add(m.getName().decoration(TextDecoration.ITALIC, false));
                }

                memberMeta.lore(memberLore);

                memberStack.setItemMeta(memberMeta);

                Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> inventory.setItem(20, memberStack));

                this.i ++;
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
            new Typing(this.player)
            {
                @Override
                public void init()
                {
                    super.init();
                    this.player.sendMessage(Language.translate("ui.plot-settings.rename.send-me", this.player).color(NamedTextColor.GREEN));
                    this.player.sendMessage(Language.translate("ui.plot-settings.rename.send-me.details", this.player).color(NamedTextColor.YELLOW));
                }

                @Override
                public void onTyped(String string)
                {
                    super.onTyped(string);

                    if (string.equals(":"))
                    {
                        this.player.sendMessage(Language.translate("ui.plot-settings.rename.canceled", this.player).color(NamedTextColor.GREEN));
                        return;
                    }

                    if (string.length() <= 2)
                    {
                        this.player.sendMessage(Language.translate("ui.plot-settings.rename.limit-under", this.player).color(NamedTextColor.RED));
                        this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                        return;
                    }

                    if (16 < string.length())
                    {
                        this.player.sendMessage(Language.translate("ui.plot-settings.rename.limit-over", this.player).color(NamedTextColor.RED));
                        this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                        return;
                    }

                    if (Plot.getInstance(string) != null)
                    {
                        this.player.sendMessage(Language.translate("ui.plot-settings.rename.already", this.player).color(NamedTextColor.RED));
                        this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                        return;
                    }

                    plot.setName(string);

                    this.player.sendMessage(Language.translate("ui.plot-settings.rename.changed", this.player, "name=" + string).color(NamedTextColor.GREEN));
                    this.player.playSound(this.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
                }
            };

            return;
        }

        if (event.getSlot() == 22)
        {
            this.player.closeInventory();

            if (event.isLeftClick())
            {
                Bukkit.dispatchCommand(this.player, "ptp " + this.plot.getName());
                return;
            }

            if (Plot.getInstance(this.player.getChunk()) != this.plot)
            {
                this.player.sendMessage(Language.translate("ui.plot-settings.spawn.cant", this.player).color(NamedTextColor.RED));
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.plot.setSpawn(this.player.getLocation());

            this.player.sendMessage(Language.translate("ui.plot-settings.spawn.changed", this.player).color(NamedTextColor.GREEN));
            this.player.playSound(this.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
            return;
        }

        if (event.getSlot() == 23)
        {
            new Typing(this.player)
            {
                @Override
                public void init()
                {
                    super.init();
                    this.player.sendMessage(Language.translate("ui.plot-settings.readme.send-me", this.player).color(NamedTextColor.GREEN));
                    this.player.sendMessage(Language.translate("ui.plot-settings.readme.send-me.details", this.player).color(NamedTextColor.YELLOW));
                }

                @Override
                public void onTyped(String string)
                {
                    super.onTyped(string);

                    if (string.equals(":"))
                    {
                        this.player.sendMessage(Language.translate("ui.plot-settings.readme.canceled", this.player).color(NamedTextColor.RED));
                        return;
                    }

                    if (string.equals("!"))
                    {
                        this.player.sendMessage(Language.translate("ui.plot-settings.readme.deleted", this.player).color(NamedTextColor.RED));
                        return;
                    }

                    if (36 < string.length())
                    {
                        this.player.sendMessage(Language.translate("ui.plot-settings.readme.limit-over", this.player).color(NamedTextColor.RED));
                        this.player.playSound(this.player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
                        return;
                    }

                    plot.setReadme(string);
                    this.player.sendMessage(Language.translate("ui.plot-settings.readme.changed", this.player).color(NamedTextColor.GREEN));
                    this.player.playSound(this.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
                }
            };

            return;
        }

        if (event.getSlot() == 24)
        {
            new Typing(this.player)
            {
                private String code;

                @Override
                public void init()
                {
                    super.init();

                    this.code = this.getConfirmCode(6);

                    this.player.sendMessage(Language.translate("ui.plot-settings.delete.check", this.player, "code=" + this.code).color(NamedTextColor.GREEN));
                }

                @Override
                public void onTyped(String string)
                {
                    super.onTyped(string);

                    if (! string.equals(this.code))
                    {
                        this.player.sendMessage(Language.translate("ui.plot-settings.delete.canceled", this.player).color(NamedTextColor.RED));
                        return;
                    }

                    plot.delete();

                    this.player.sendMessage(Language.translate("ui.plot-settings.delete.deleted", this.player).color(NamedTextColor.GREEN));
                }
            };

            return;
        }

        this.player.playSound(this.player, Sound.UI_BUTTON_CLICK, 0.8F, 1.2F);

        if (event.getSlot() == 29)
        {
            PlotScope scope = switch (this.plot.getScope())
            {
                case PUBLIC -> PlotScope.FRIEND;
                case FRIEND -> PlotScope.PRIVATE;
                default -> PlotScope.PUBLIC;
            };

            this.plot.setScope(scope);
            new PlotSettingsUI(this.player, this.plot);
        }

        if (event.getSlot() == 30)
        {
            this.plot.setEdit(! this.plot.canEdit());

            if (this.plot.canEdit())
            {
                this.plot.setChest(true);
            }

            new PlotSettingsUI(this.player, this.plot);
        }

        if (event.getSlot() == 31)
        {
            this.plot.setChest(! this.plot.canChest());

            if (! this.plot.canChest())
            {
                this.plot.setEdit(false);
            }

            new PlotSettingsUI(this.player, this.plot);
        }

        if (event.getSlot() == 32)
        {
            this.plot.setPvP(! this.plot.canPvP());
            new PlotSettingsUI(this.player, this.plot);
        }
    }

    @Override
    public void onInventoryClose(@NotNull InventoryCloseEvent event)
    {
        super.onInventoryClose(event);

        this.runnable.cancel();
    }
}
