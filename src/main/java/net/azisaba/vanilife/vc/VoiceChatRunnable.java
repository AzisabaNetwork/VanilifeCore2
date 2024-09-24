package net.azisaba.vanilife.vc;

import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VoiceChatRunnable extends BukkitRunnable
{
    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            User user = User.getInstance(player);
            VoiceChat vc = VoiceChat.getInstance(user);

            if (vc == null)
            {
                player.playerListName(user.getName());
                continue;
            }

            player.playerListName(user.getName().append(Component.text(" 🔊").color(NamedTextColor.WHITE)));

            VoiceChat vc2 = VoiceChat.search(player);

            if (vc != vc2)
            {
                vc2.connect(user);
            }
        }
    }
}
