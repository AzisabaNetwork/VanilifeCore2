package net.azisaba.vanilife.vc;

import net.azisaba.vanilife.user.User;
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
                continue;
            }

            VoiceChat vc2 = VoiceChat.search(player);

            if (vc != vc2)
            {
                vc2.connect(user);
            }
        }
    }
}
