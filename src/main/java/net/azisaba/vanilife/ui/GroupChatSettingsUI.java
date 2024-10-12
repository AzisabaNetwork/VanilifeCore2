package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.chat.GroupChat;
import net.azisaba.vanilife.chat.ChatScope;
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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GroupChatSettingsUI extends ChestUI
{
    private final GroupChat chat;
    private final BukkitRunnable runnable;

    public GroupChatSettingsUI(@NotNull Player player, @NotNull GroupChat chat)
    {
        super(player, Bukkit.createInventory(null, 45, Component.text(chat.getName()).decorate(TextDecoration.BOLD)));
        this.chat = chat;

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            ItemStack ownerStack = HeadUtility.getPlayerHead(this.chat.getOwner().getPlaneName());
            ItemMeta ownerMeta = ownerStack.getItemMeta();
            ownerMeta.displayName(Component.text(this.chat.getName()).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            ownerMeta.lore(List.of(Language.translate("ui.chat-settings.owner", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(this.chat.getOwner().getName())));
            ownerStack.setItemMeta(ownerMeta);
            this.inventory.setItem(4, ownerStack);
        });

        ItemStack renameStack = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = renameStack.getItemMeta();
        renameMeta.displayName(Language.translate("ui.chat-settings.rename", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        renameMeta.lore(List.of(Language.translate("ui.chat-settings.rename.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        renameStack.setItemMeta(renameMeta);
        this.inventory.setItem(21, renameStack);

        ItemStack scopeStack = new ItemStack(Material.COMPASS);
        ItemMeta scopeMeta = scopeStack.getItemMeta();
        scopeMeta.displayName(Language.translate("ui.chat-settings.scope", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        scopeMeta.lore(List.of(Language.translate("ui.chat-settings.scope.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.chat-settings.scope.public", player).color(this.chat.getScope() == ChatScope.PUBLIC ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.chat-settings.scope.friend", player).color(this.chat.getScope() == ChatScope.FRIEND ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.chat-settings.scope.osatou", player).color(this.chat.getScope() == ChatScope.OSATOU ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.chat-settings.scope.private", player).color(this.chat.getScope() == ChatScope.PRIVATE ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        scopeStack.setItemMeta(scopeMeta);
        this.inventory.setItem(22, scopeStack);

        ItemStack colorStack = new ItemStack(Material.INK_SAC);
        ItemMeta colorMeta = colorStack.getItemMeta();
        colorMeta.displayName(Language.translate("ui.chat-settings.color", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        colorMeta.lore(List.of(Language.translate("ui.chat-settings.color.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        colorStack.setItemMeta(colorMeta);
        this.inventory.setItem(23, colorStack);

        ItemStack deleteStack = new ItemStack(Material.TNT_MINECART);
        ItemMeta deleteMeta = deleteStack.getItemMeta();
        deleteMeta.displayName(Language.translate("ui.chat-settings.delete", player).color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        deleteMeta.lore(List.of(Language.translate("ui.chat-settings.delete.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        deleteStack.setItemMeta(deleteMeta);
        this.inventory.setItem(24, deleteStack);

        ItemStack closeStack = new ItemStack(Material.ARROW);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(40, closeStack);

        this.runnable = new BukkitRunnable()
        {
            private int i = 0;

            @Override
            public void run()
            {
                if (chat.getMembers().isEmpty())
                {
                    chat.addMember(chat.getOwner());
                }

                if (chat.getMembers().size() <= this.i)
                {
                    this.i = 0;
                }

                User member = chat.getMembers().get(this.i);

                ItemStack memberStack = HeadUtility.getPlayerHead(member.getPlaneName());
                ItemMeta memberMeta = memberStack.getItemMeta();

                memberMeta.displayName(Language.translate("ui.chat-settings.member", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

                List<Component> memberLore = new ArrayList<>();

                memberLore.add(Language.translate("ui.chat-settings.member.details", player, "members=" + chat.getMembers().size()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                memberLore.add(Component.text().build());
                memberLore.add(Language.translate("ui.chat-settings.member.help.1", player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
                memberLore.add(Language.translate("ui.chat-settings.member.help.2", player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
                memberLore.add(Component.text().build());

                for (User m : chat.getMembers())
                {
                    memberLore.add(m.getName().decoration(TextDecoration.ITALIC, false));
                }

                memberMeta.lore(memberLore);

                memberStack.setItemMeta(memberMeta);

                Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> inventory.setItem(20, memberStack));

                this.i ++;

                if (chat.getMembers().size() == 1)
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

        this.player.playSound(this.player, Sound.UI_BUTTON_CLICK, 1.0f, 1.2f);

        if (event.getSlot() == 21)
        {
            new AnvilUI(this.player, Language.translate("ui.chat-settings.rename", this.player))
            {
                @Override
                protected void onTyped(@NotNull String string)
                {
                    super.onTyped(string);

                    if (GroupChat.getInstance(string) != null)
                    {
                        this.player.sendMessage(Language.translate("cmd.chat.create.already", player).color(NamedTextColor.RED));
                        return;
                    }

                    if (! GroupChat.namepattern.matcher(string).matches())
                    {
                        this.player.sendMessage(Language.translate("cmd.chat.create.invalid", player).color(NamedTextColor.RED));
                        return;
                    }

                    if (string.length() < 3)
                    {
                        this.player.sendMessage(Language.translate("cmd.chat.create.limit-under", player).color(NamedTextColor.RED));
                        return;
                    }

                    if (10 < string.length())
                    {
                        this.player.sendMessage(Language.translate("cmd.chat.create.limit-over", player).color(NamedTextColor.RED));
                        return;
                    }

                    chat.setName(string);
                }
            };
        }

        if (event.getSlot() == 22)
        {
            chat.setScope(switch (chat.getScope())
            {
                case PUBLIC -> ChatScope.FRIEND;
                case FRIEND -> ChatScope.OSATOU;
                case OSATOU -> ChatScope.PRIVATE;
                default -> ChatScope.PUBLIC;
            });

            new GroupChatSettingsUI(this.player, chat);
        }

        if (event.getSlot() == 23)
        {
            new GroupChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 24)
        {
            User.getInstance(this.player).setChat(chat);
            Bukkit.dispatchCommand(this.player, "/chat delete");
        }

        if (event.getSlot() == 40)
        {
            this.player.closeInventory();
        }
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event)
    {
        super.onClose(event);
        this.runnable.cancel();
    }
}
