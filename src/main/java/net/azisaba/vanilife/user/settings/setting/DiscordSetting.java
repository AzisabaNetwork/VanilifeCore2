package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.DiscordLinkManager;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.HeadUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class DiscordSetting extends ScopeSetting
{
    public DiscordSetting(@NotNull User user)
    {
        super(user);
    }

    @Override
    public @NotNull String getName()
    {
        return "discord";
    }

    @Override
    public @NotNull ItemStack getIcon()
    {
        ItemStack iconStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta iconMeta = (SkullMeta) iconStack.getItemMeta();

        iconMeta.setPlayerProfile(HeadUtility.getPlayerProfile(HeadUtility.DISCORD));
        iconStack.setItemMeta(iconMeta);

        return iconStack;
    }

    @Override
    public void onRightClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();

        player.closeInventory();

        if (event.isShiftClick())
        {
            User user = User.getInstance(player);
            user.setDiscord((net.dv8tion.jda.api.entities.User) null);
            player.sendMessage(Language.translate("settings.discord.unlinked", player).color(NamedTextColor.GREEN));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
            return;
        }

        final String token = DiscordLinkManager.newToken(this.user);

        player.sendMessage(Component.text().build());

        player.sendMessage(Component.text().decorate(TextDecoration.BOLD).append(Language.translate("settings.discord.pls-run-command", player)));
        player.sendMessage(Component.text("/vanilife-link ").color(NamedTextColor.DARK_GRAY).hoverEvent(HoverEvent.showText(Component.text("クリックしてクリップボードにコピー!"))).clickEvent(ClickEvent.copyToClipboard("/vanilife-link token:" + token)).append(Component.text(token).decorate(TextDecoration.OBFUSCATED)));

        player.sendMessage(Component.text().build());

        player.sendMessage(Language.translate("settings.discord.join", player).color(TextColor.color(88, 101, 242)).decorate(TextDecoration.BOLD).hoverEvent(HoverEvent.showText(Component.text("https://discord.gg/azisaba"))).clickEvent(ClickEvent.openUrl("https://discord.gg/azisaba")));

        player.sendMessage(Component.text().build());

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
    }
}
