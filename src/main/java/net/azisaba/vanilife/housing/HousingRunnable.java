package net.azisaba.vanilife.housing;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Afk;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HousingRunnable extends BukkitRunnable
{
    @Override
    public void run()
    {
        World world = Housing.getWorld();

        if (world.getTime() != HousingTime.MIDNIGHT.getTime())
        {
            world.setTime(HousingTime.MIDNIGHT.getTime());
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        }

        for (Player player : Bukkit.getOnlinePlayers())
        {
            Housing housing = Housing.getInstance(player.getLocation());

            if (housing != null)
            {
                player.setPlayerTime(housing.getTime().getTime(), false);
                continue;
            }

            User user = User.getInstance(player);

            if (Afk.isAfk(player) && user.hasHousing() && user.read("settings.housing.afk").getAsBoolean())
            {
                Bukkit.dispatchCommand(player, "housing");
                HousingAfkRunnable.afk.add(player);
                continue;
            }

            player.resetPlayerTime();
        }
    }
}
