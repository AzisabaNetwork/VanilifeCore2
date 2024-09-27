package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.HousingInvite;
import net.azisaba.vanilife.util.HeadUtility;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileUI extends InventoryUI
{
    private final User profile;

    public ProfileUI(@NotNull Player player, @NotNull User profile)
    {
        super(player, Bukkit.createInventory(null, 45, profile.getName()));
        
        this.profile = profile;
        
        User user = User.getInstance(player);

        ItemStack friendStack = new ItemStack(user.isFriend(profile) ? Material.REDSTONE : Material.DIAMOND);
        ItemMeta friendMeta = friendStack.getItemMeta();
        friendMeta.displayName((user.isFriend(this.profile) ? Language.translate("ui.profile.unfriend", player) : Language.translate("ui.profile.friend", player)).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        friendMeta.lore(Collections.singletonList((user.isFriend(profile) ? Language.translate("ui.profile.unfriend.details", player) : Language.translate("ui.profile.friend.details", player)).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        friendStack.setItemMeta(friendMeta);
        this.registerListener(12, friendStack, String.format("vanilife:friend %s", this.profile.getPlaneName()), ExecutionType.CLIENT);

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            ItemStack headStack = HeadUtility.getPlayerHead(this.profile.getPlaneName());
            ItemMeta headMeta = headStack.getItemMeta();
            headMeta.displayName(this.profile.getName().decoration(TextDecoration.ITALIC, false));

            ArrayList<Component> headLore = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            if (this.profile.getBio() != null)
            {
                for (int i = 0; i < this.profile.getBio().length(); i ++)
                {
                    sb.append(this.profile.getBio().charAt(i));

                    if (16 <= sb.length())
                    {
                        headLore.add(Component.text(sb.toString()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                        sb = new StringBuilder();
                    }
                }

                if (sb.length() < 16)
                {
                    headLore.add(Component.text(sb.toString()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                }

                headLore.add(Component.text(""));
            }

            headLore.add(Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                    .append((profile.isOnline() ? Language.translate("ui.profile.online", this.player) : Language.translate("ui.profile.offline", player)).color(profile.isOnline() ? NamedTextColor.GREEN : NamedTextColor.RED)));

            if (! this.profile.isOnline())
            {
                headLore.add(Language.translate("ui.profile.last-login", this.player).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY).append(Component.text(Vanilife.sdf3.format(profile.getLastLogin())).color(NamedTextColor.GREEN)));
            }

            if (this.profile.getSettings().BIRTHDAY.isWithinScope(user) && this.profile.getBirthday() != null)
            {
                headLore.add(Language.translate("ui.profile.birthday", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text(Vanilife.sdf4.format(this.profile.getBirthday())).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
            }

            if (this.profile.hasHousing() && this.profile.inHousing() && this.profile.read("settings.housing.activity").getAsBoolean())
            {
                headLore.add(Component.text().build());
                headLore.add(Language.translate("ui.profile.playing-housing", this.player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            }

            headMeta.lore(headLore);

            headStack.setItemMeta(headMeta);
            this.inventory.setItem(13, headStack);
        });

        ItemStack reportStack = new ItemStack(Material.PAPER);
        ItemMeta reportMeta = reportStack.getItemMeta();
        reportMeta.displayName(Language.translate("ui.profile.report", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        reportMeta.lore(Collections.singletonList(Language.translate("ui.profile.report.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        reportStack.setItemMeta(reportMeta);
        this.inventory.setItem(14, reportStack);

        ItemStack youtubeStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta youtubeMeta = (SkullMeta) youtubeStack.getItemMeta();
        youtubeMeta.displayName(Component.text("YouTube").color(TextColor.color(255, 0, 0)).decoration(TextDecoration.ITALIC, false));

        if (! this.profile.getSettings().YOUTUBE.isWithinScope(user))
        {
            youtubeMeta.lore(Collections.singletonList(Language.translate("ui.not-viewable", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }
        else if (this.profile.getYoutube() != null)
        {
            youtubeMeta.lore(List.of(Component.text("@" + this.profile.getYoutube()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        }
        else
        {
            youtubeMeta.lore(Collections.singletonList(Language.translate("ui.unset", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }

        youtubeMeta.setPlayerProfile(HeadUtility.getPlayerProfile(HeadUtility.YOUTUBE));
        youtubeStack.setItemMeta(youtubeMeta);
        this.inventory.setItem(21, youtubeStack);

        ItemStack twitterStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta twitterMeta = (SkullMeta) twitterStack.getItemMeta();
        twitterMeta.displayName(Component.text("Twitter").color(TextColor.color(29, 161, 242)).decoration(TextDecoration.ITALIC, false));

        if (! this.profile.getSettings().TWITTER.isWithinScope(user))
        {
            twitterMeta.lore(Collections.singletonList(Language.translate("ui.not-viewable", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }
        else if (this.profile.getTwitter() != null)
        {
            twitterMeta.lore(List.of(Component.text("@" + this.profile.getTwitter()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        }
        else
        {
            twitterMeta.lore(Collections.singletonList(Language.translate("ui.unset", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }

        twitterMeta.setPlayerProfile(HeadUtility.getPlayerProfile(HeadUtility.TWITTER));
        twitterStack.setItemMeta(twitterMeta);
        this.inventory.setItem(22, twitterStack);

        ItemStack discordStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta discordMeta = (SkullMeta) discordStack.getItemMeta();
        discordMeta.displayName(Component.text("Discord").color(TextColor.color(88, 101, 242)).decoration(TextDecoration.ITALIC, false));

        if (! this.profile.getSettings().DISCORD.isWithinScope(user))
        {
            discordMeta.lore(Collections.singletonList(Language.translate("ui.not-viewable", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }
        else if (this.profile.getDiscord() != null)
        {
            discordMeta.lore(List.of(Component.text(this.profile.getDiscord().getName()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        }
        else
        {
            discordMeta.lore(Collections.singletonList(Language.translate("ui.unset", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }

        discordMeta.setPlayerProfile(HeadUtility.getPlayerProfile(HeadUtility.DISCORD));
        discordStack.setItemMeta(discordMeta);
        this.inventory.setItem(23, discordStack);

        ItemStack blockStack = new ItemStack(Material.HOPPER);
        ItemMeta blockMeta = blockStack.getItemMeta();
        blockMeta.displayName((user.isBlock(profile) ? Language.translate("ui.profile.unblock", player) : Language.translate("ui.profile.block", player)).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        blockMeta.lore(List.of(Language.translate("ui.profile.block.details.1", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.profile.block.details.2", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.profile.block.details.3", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        blockStack.setItemMeta(blockMeta);
        this.registerListener(30, blockStack, (user.isBlock(profile) ? "vanilife:unblock " : "vanilife:block ") + this.profile.getPlaneName(), ExecutionType.CLIENT);

        ItemStack tradeStack = new ItemStack(Material.CHEST);
        ItemMeta tradeMeta = tradeStack.getItemMeta();
        tradeMeta.displayName(Language.translate("ui.profile.trade", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        tradeMeta.lore(Collections.singletonList(Language.translate("ui.profile.trade.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        tradeStack.setItemMeta(tradeMeta);
        this.registerListener(31, tradeStack, String.format("vanilife:trade %s", this.profile.getPlaneName()), ExecutionType.CLIENT);

        ItemStack housingStack = new ItemStack(Material.DARK_OAK_DOOR);
        ItemMeta housingMeta = housingStack.getItemMeta();
        housingMeta.displayName(Language.translate("ui.profile.housing", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        housingMeta.lore(List.of(Language.translate("ui.profile.housing.details.1", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.profile.housing.details.2", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        housingStack.setItemMeta(housingMeta);
        this.inventory.setItem(32, housingStack);
    }

    @Override
    public void onUiClick(InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getSlot() == 14)
        {
            new Typing(this.player)
            {
                @Override
                public void init()
                {
                    this.player.sendMessage(Language.translate("ui.profile.report.pls-send-details", this.player).color(NamedTextColor.GREEN));
                    this.player.sendMessage(Language.translate("ui.profile.report.how-to-cancel", this.player).color(NamedTextColor.YELLOW));
                }

                @Override
                public void onTyped(String string)
                {
                    super.onTyped(string);

                    if (string.equals(":"))
                    {
                        this.player.sendMessage(Language.translate("ui.profile.report.canceled", this.player).color(NamedTextColor.RED));
                        return;
                    }

                    if (250 < string.length())
                    {
                        this.player.sendMessage(Language.translate("ui.profile.report.limit-over", this.player).color(NamedTextColor.RED));
                        return;
                    }

                    // new Report(User.getInstance(this.player), string, profile);
                    this.player.sendMessage(Language.translate("ui.profile.report.created", this.player).color(NamedTextColor.GREEN));
                }
            };
        }

        if (event.getSlot() == 32)
        {
            if (event.isLeftClick())
            {
                Bukkit.dispatchCommand(this.player, "housing " + this.profile.getPlaneName());
            }

            if (event.isRightClick())
            {
                if (! this.profile.isOnline())
                {
                    this.player.sendMessage(Language.translate("msg.offline", this.player, "name=" + this.profile.getPlaneName()).color(NamedTextColor.RED));
                    return;
                }

                User user = User.getInstance(this.player);

                Housing housing = user.getHousing();

                if (housing == null)
                {
                    this.player.sendMessage(Language.translate("msg.housing.not-found", this.player).color(NamedTextColor.RED));
                    return;
                }

                if (user == housing.getUser())
                {
                    this.player.sendMessage(Language.translate("msg.housing.cant-yourself", this.player).color(NamedTextColor.RED));
                    return;
                }

                new HousingInvite(housing, this.profile.getAsPlayer());
            }
        }
    }
}
