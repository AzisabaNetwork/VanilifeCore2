package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.user.TrustRank;
import net.azisaba.vanilife.user.User;
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

            if (VoiceChat.getInstance(user) == null)
            {
                player.playerListName(user.getName().appendSpace().append(Component.text("[" + user.getTrustRank().getName() + "]").color(user.getTrustRank() != TrustRank.TRUSTED ? NamedTextColor.GRAY : NamedTextColor.GOLD)));
            }
            else
            {
                player.playerListName(user.getName().appendSpace().append(Component.text("[" + user.getTrustRank().getName() + "]").color(user.getTrustRank() != TrustRank.TRUSTED ? NamedTextColor.GRAY : NamedTextColor.GOLD)).append(Component.text(" 🔊").color(NamedTextColor.WHITE)));
            }
        });
    }
}