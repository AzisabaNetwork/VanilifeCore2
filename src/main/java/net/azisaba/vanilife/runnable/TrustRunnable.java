package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Afk;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class TrustRunnable extends BukkitRunnable
{
    @Override
    public void run()
    {
        List<Player> targetGroup = Bukkit.getOnlinePlayers().stream()
                .filter(player -> ! Afk.isAfk(player) &&
                        Math.max(User.getInstance(player).getTrust(), 1) / 100D <= Vanilife.random.nextDouble() &&
                        Vanilife.random.nextDouble() < 0.5)
                .collect(Collectors.toList());

        targetGroup.forEach(target -> {
            User user = User.getInstance(target);
            user.setTrust(user.getTrust() + 1);
        });
    }
}
