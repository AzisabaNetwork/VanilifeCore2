package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.user.TrustRank;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Afk;
import net.azisaba.vanilife.vc.VoiceChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListRunnable extends BukkitRunnable
{
    @Override
    public void run()
    {
        Bukkit.getOnlinePlayers().forEach(player -> {
            User user = User.getInstance(player);

            Component name = user.getName().appendSpace().append(Component.text("[" + user.getTrustRank().getName() + "]").color(user.getTrustRank() != TrustRank.TRUSTED ? NamedTextColor.GRAY : NamedTextColor.GOLD));

            Component icon = null;

            if (Afk.isAfk(player))
            {
                icon = Component.text("🌙").color(NamedTextColor.GOLD);
            }

            if (VoiceChat.getInstance(user) != null)
            {
                icon = Component.text(" 🔊").color(NamedTextColor.WHITE);
            }

            if (icon != null)
            {
                name = name.appendSpace().append(icon);
            }

            player.playerListName(name);
        });
    }
}
