package net.azisaba.vanilife.vc;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VoiceChatRunnable extends BukkitRunnable
{
    private int usersCache;
    
    @Override
    public void run()
    {
        int users = 0;
        
        for (VoiceChat vc : VoiceChat.getInstances())
        {
            users += vc.getMembers().size();
        }

        if (users != this.usersCache)
        {
            Vanilife.CHANNEL_VOICE.modifyStatus(users + "人が接続中").queue();
        }

        this.usersCache = users;
        
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
