package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.gomenne.ConvertRequest;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImereqUI extends InventoryUI
{
    private final List<ConvertRequest> requests = new ArrayList<>(ConvertRequest.getInstances());

    private final int page;

    private final int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

    public ImereqUI(@NotNull Player player)
    {
        this(player, 0);
    }

    public ImereqUI(@NotNull Player player, int page)
    {
        super(player, Bukkit.createInventory(null, 54, Component.text("ごめんね IME: 変換リクエスト")));

        this.page = page;

        User user = User.getInstance(this.player);

        if (! UserUtility.isModerator(user))
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

        ItemStack closeStack = new ItemStack(Material.OAK_DOOR);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(49, closeStack);

        int i = 0;

        for (ConvertRequest request : this.requests.subList(this.page * 21, Math.min((this.page + 1) * 21, this.requests.size())))
        {
            ItemStack requestStack = new ItemStack(Material.PAPER);
            ItemMeta requestMeta = requestStack.getItemMeta();
            requestMeta.displayName(Component.text(request.getKaki()).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            requestMeta.lore(List.of(Component.text("送信者: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(request.getSender().getName()),
                    Component.text("よみ: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(request.getYomi()).color(NamedTextColor.GREEN)),
                    Component.text("かき: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(request.getKaki()).color(NamedTextColor.GREEN)),
                    Component.text().build(),
                    Component.text(Vanilife.sdf2.format(request.getDate())).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("シフトキーを押しながら: 左クリックで承認、右クリックで却下").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
            requestStack.setItemMeta(requestMeta);

            this.inventory.setItem(this.slots[i], requestStack);

            i ++;
        }
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);
        event.setCancelled(true);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getSlot() == 45)
        {
            new ImereqUI(this.player, Math.max(this.page - 1, 0));
        }

        if (event.getSlot() == 49)
        {
            this.player.closeInventory();
        }

        if (event.getSlot() == 53)
        {
            new ImereqUI(this.player, Math.min(this.page + 1, this.requests.size() / 21));
        }

        if (! event.isShiftClick())
        {
            return;
        }

        if (Arrays.stream(this.slots).anyMatch(slot -> slot == event.getSlot()))
        {
            int i = -1;

            for (int j = 0; j < this.slots.length; j ++)
            {
                if (this.slots[j] == event.getSlot())
                {
                    i = j;
                    break;
                }
            }

            if (i == -1)
            {
                return;
            }

            ConvertRequest request = this.requests.get(this.page * this.slots.length + i);

            if (request == null)
            {
                return;
            }

            if (event.isLeftClick())
            {
                request.accept();
                this.player.sendMessage(Component.text(request.getKaki() + " を承認しました").color(NamedTextColor.GREEN));

                Vanilife.consoleChannel.sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("変換リクエストの承認")
                        .setColor(Color.GREEN)
                        .addField("送信者", request.getSender().getPlaneName() + " (" + request.getSender().getId().toString() + ")", false)
                        .addField("担当者", this.player.getName() + " (" + this.player.getUniqueId().toString() + ")", false)
                        .addField("よみ", request.getYomi(), false)
                        .addField("かき", request.getKaki(), false)
                        .build()).queue();
            }
            else
            {
                request.reject();
                this.player.sendMessage(Component.text(request.getKaki() + " を拒否しました").color(NamedTextColor.RED));

                Vanilife.consoleChannel.sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("変換リクエストの拒否")
                        .setColor(Color.RED)
                        .addField("送信者", request.getSender().getPlaneName() + " (" + request.getSender().getId().toString() + ")", false)
                        .addField("担当者", this.player.getName() + " (" + this.player.getUniqueId().toString() + ")", false)
                        .addField("よみ", request.getYomi(), false)
                        .addField("かき", request.getKaki(), false)
                        .build()).queue();
            }

            new ImereqUI(this.player, this.page);
        }
    }
}
