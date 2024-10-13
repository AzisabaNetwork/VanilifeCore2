package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.HeadUtility;
import net.azisaba.vanilife.util.PlayerUtility;
import net.azisaba.vanilife.util.Watch;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WatchUI extends ChestUI
{
    private final int page;

    private final List<Player> players;
    private final Map<Integer, Player> slots = new HashMap<>();

    public WatchUI(@NotNull Player player)
    {
        this(player, 0);
    }

    public WatchUI(@NotNull Player player, int page)
    {
        super(player, Bukkit.createInventory(null, 54, Component.text("Watch").decorate(TextDecoration.BOLD)));

        this.page = page;
        this.players = Bukkit.getOnlinePlayers().stream().filter(p -> p != this.player).collect(Collectors.toList());

        if (! Watch.isWatcher(player))
        {
            this.player.closeInventory();
            return;
        }

        ItemStack backStack = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backStack.getItemMeta();
        backMeta.displayName(Language.translate("ui.back", player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        backMeta.lore(List.of(Language.translate("ui.back.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        backStack.setItemMeta(backMeta);
        this.inventory.setItem(45, backStack);

        ItemStack nextStack = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextStack.getItemMeta();
        nextMeta.displayName(Language.translate("ui.next", player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        nextMeta.lore(List.of(Language.translate("ui.next.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        nextStack.setItemMeta(nextMeta);
        this.inventory.setItem(53, nextStack);

        ItemStack closeStack = new ItemStack(Material.ANVIL);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(49, closeStack);

        int i = 0;
        int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        for (Player p : this.players.subList(this.page * 21, Math.min((this.page + 1) * 21, this.players.size())))
        {
            final int i2 = i;

            Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
                ItemStack stack = HeadUtility.getPlayerHead(p.getName());
                ItemMeta meta = stack.getItemMeta();

                User target = User.getInstance(p);

                meta.displayName(target.getName().decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

                meta.lore(List.of(Component.text("Trust Rank: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(target.getTrustRank().getName()).color(target.getTrustRank().getColor()))
                                .appendSpace().append(Component.text("(" + target.getTrust() + ")").color(NamedTextColor.DARK_GRAY)),
                        Component.text("Game Mode: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(p.getGameMode().name().toLowerCase()).color(NamedTextColor.GREEN)),
                        Component.text("Client: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(Optional.ofNullable(p.getClientBrandName()).orElse("不明")).color(NamedTextColor.GREEN)),
                        Component.text("Address: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(Optional.ofNullable(PlayerUtility.getIpAddress(player)).orElse("不明")).color(NamedTextColor.GREEN)),
                        Component.text("Country: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(PlayerUtility.getCountry(p)).color(NamedTextColor.GREEN)),
                        Component.text().build(),
                        Component.text("クリックしてテレポートします").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)));
                stack.setItemMeta(meta);
                this.inventory.setItem(slots[i2], stack);

                this.slots.put(slots[i2], p);
            });

            i ++;
        }
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);
        event.setCancelled(true);

        if (event.getSlot() == 45)
        {
            new WatchUI(this.player, Math.max(this.page - 1, 0));
        }

        if (event.getSlot() == 49)
        {
            this.player.closeInventory();
        }

        if (event.getSlot() == 53)
        {
            new WatchUI(this.player, Math.min(this.page + 1, this.players.size() / 21));
        }

        Player target = this.slots.get(event.getSlot());

        if (target == null)
        {
            return;
        }

        this.player.teleport(target);
        this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.0f);
        this.player.sendMessage(Component.text(target.getName() + " にテレポートしました").color(NamedTextColor.RED));
    }
}
