package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.report.Report;
import net.azisaba.vanilife.user.User;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileUI extends InventoryUI
{
    private final User view;

    public ProfileUI(Player player, User view)
    {
        super(player, Bukkit.createInventory(null, 45, view.getName()));
        
        this.view = view;
        
        User user = User.getInstance(player);

        ItemStack friendStack = new ItemStack(user.isFriend(view) ? Material.REDSTONE : Material.DIAMOND);
        ItemMeta friendMeta = friendStack.getItemMeta();
        friendMeta.displayName(Component.text(user.isFriend(view) ? "フレンドを削除" : "フレンドに追加").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        friendMeta.lore(Collections.singletonList(Component.text(user.isFriend(view) ? "フレンドから削除します" : "フレンド申請を送信します").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        friendStack.setItemMeta(friendMeta);
        this.registerListener(12, friendStack, String.format("friend %s", this.view.getPlaneName()), ExecutionType.CLIENT);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                ItemStack headStack = HeadUtility.getPlayerHead(view.getPlaneName());
                ItemMeta headMeta = headStack.getItemMeta();
                headMeta.displayName(view.getName().decoration(TextDecoration.ITALIC, false));

                ArrayList<Component> headLore = new ArrayList<>();
                StringBuilder sb = new StringBuilder();

                if (view.getBio() != null)
                {
                    for (int i = 0; i < view.getBio().length(); i ++)
                    {
                        sb.append(view.getBio().charAt(i));

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

                headLore.add(Component.text("状態: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(view.isOnline() ? "オンライン" : "オフライン").color(view.isOnline() ? NamedTextColor.GREEN : NamedTextColor.RED)));

                if (view.getSettings().birthdaySetting.isWithinScope(user) && view.getBirthday() != null)
                {
                    headLore.add(Component.text("お誕生日: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                            .append(Component.text(Vanilife.sdf3.format(view.getBirthday())).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
                }

                headMeta.lore(headLore);

                headStack.setItemMeta(headMeta);
                inventory.setItem(13, headStack);
            }
        }.runTaskAsynchronously(Vanilife.getPlugin());

        ItemStack reportStack = new ItemStack(Material.PAPER);
        ItemMeta reportMeta = reportStack.getItemMeta();
        reportMeta.displayName(Component.text("報告").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        reportMeta.lore(Collections.singletonList(Component.text("ルール違反ですか？レポートを作成しましょう…").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        reportStack.setItemMeta(reportMeta);
        this.inventory.setItem(14, reportStack);

        ItemStack youtubeStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta youtubeMeta = (SkullMeta) youtubeStack.getItemMeta();
        youtubeMeta.displayName(Component.text("YouTube").color(TextColor.color(255, 0, 0)).decoration(TextDecoration.ITALIC, false));

        if (! this.view.getSettings().youtubeSetting.isWithinScope(user))
        {
            youtubeMeta.lore(Collections.singletonList(Component.text("この項目を表示できません").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }
        else if (this.view.getYoutube() != null)
        {
            youtubeMeta.lore(List.of(Component.text("@" + this.view.getYoutube()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("クリックして開きます！").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
        }
        else
        {
            youtubeMeta.lore(Collections.singletonList(Component.text("この項目が設定されていません").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }

        youtubeMeta.setPlayerProfile(HeadUtility.getPlayerProfile(HeadUtility.YOUTUBE));
        youtubeStack.setItemMeta(youtubeMeta);
        this.inventory.setItem(21, youtubeStack);

        ItemStack twitterStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta twitterMeta = (SkullMeta) twitterStack.getItemMeta();
        twitterMeta.displayName(Component.text("Twitter").color(TextColor.color(29, 161, 242)).decoration(TextDecoration.ITALIC, false));

        if (! this.view.getSettings().twitterSetting.isWithinScope(user))
        {
            twitterMeta.lore(Collections.singletonList(Component.text("この項目を表示できません").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }
        else if (this.view.getTwitter() != null)
        {
            twitterMeta.lore(List.of(Component.text("@" + this.view.getTwitter()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("クリックして開きます！").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
        }
        else
        {
            twitterMeta.lore(Collections.singletonList(Component.text("この項目が設定されていません").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }

        twitterMeta.setPlayerProfile(HeadUtility.getPlayerProfile(HeadUtility.TWITTER));
        twitterStack.setItemMeta(twitterMeta);
        this.inventory.setItem(22, twitterStack);

        ItemStack discordStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta discordMeta = (SkullMeta) discordStack.getItemMeta();
        discordMeta.displayName(Component.text("Discord").color(TextColor.color(88, 101, 242)).decoration(TextDecoration.ITALIC, false));

        if (! this.view.getSettings().discordSetting.isWithinScope(user))
        {
            discordMeta.lore(Collections.singletonList(Component.text("この項目を表示できません").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }
        else if (this.view.getDiscord() != null)
        {
            discordMeta.lore(List.of(Component.text(this.view.getDiscord()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        }
        else
        {
            discordMeta.lore(Collections.singletonList(Component.text("この項目が設定されていません").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        }

        discordMeta.setPlayerProfile(HeadUtility.getPlayerProfile(HeadUtility.DISCORD));
        discordStack.setItemMeta(discordMeta);
        this.inventory.setItem(23, discordStack);

        ItemStack tradeStack = new ItemStack(Material.CHEST);
        ItemMeta tradeMeta = tradeStack.getItemMeta();
        tradeMeta.displayName(Component.text("Trade").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        tradeMeta.lore(Collections.singletonList(Component.text("Trade 申請を送信します").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        tradeStack.setItemMeta(tradeMeta);
        this.registerListener(30, tradeStack, String.format("trade %s", this.view.getPlaneName()), ExecutionType.CLIENT);

        ItemStack closeStack = new ItemStack(Material.OAK_DOOR);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Component.text("閉じる").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(Collections.singletonList(Component.text("この画面を閉じます").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(31, closeStack);

        ItemStack blockStack = new ItemStack(Material.HOPPER);
        ItemMeta blockMeta = blockStack.getItemMeta();
        blockMeta.displayName(Component.text(user.isBlock(view) ? "ブロック解除" : "ブロック").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        blockMeta.lore(List.of(Component.text("このプレイヤーのチャットは非表示になり、").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("あなたにメールや各種申請を送信できなくなります").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("通知されることはありません！").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        blockStack.setItemMeta(blockMeta);
        this.registerListener(32, blockStack, (user.isBlock(view) ? "unblock " : "block ") + this.view.getPlaneName(), ExecutionType.CLIENT);
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
                    this.player.sendMessage(Component.text("詳細を送信してください:").color(NamedTextColor.GREEN));
                    this.player.sendMessage(Component.text("「:」を送信してキャンセルします").color(NamedTextColor.YELLOW));
                }

                @Override
                public void onTyped(String string)
                {
                    super.onTyped(string);

                    if (string.equals(":"))
                    {
                        this.player.sendMessage(Component.text("報告をキャンセルしました").color(NamedTextColor.RED));
                        return;
                    }

                    if (250 < string.length())
                    {
                        this.player.sendMessage(Component.text("250文字以内で入力してください").color(NamedTextColor.RED));
                        return;
                    }

                    new Report(User.getInstance(this.player), string, view);
                    this.player.sendMessage(Component.text("レポートを作成しました").color(NamedTextColor.GREEN));
                }
            };
        }

        if (event.getSlot() == 31)
        {
            this.player.closeInventory();
            return;
        }
    }
}
