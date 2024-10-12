package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.objective.Objective;
import net.azisaba.vanilife.objective.Objectives;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ObjectiveRunnable extends BukkitRunnable
{
    private final Map<UUID, BossBar> bossbarmap = new HashMap<>();

    @Override
    public void run()
    {
        Bukkit.getOnlinePlayers().forEach(player -> {
            User user = User.getInstance(player);
            Objective objective = Objectives.next(user);

            if (objective != null && Plot.getInstance(player.getChunk()) == null)
            {
                if (! this.bossbarmap.containsKey(player.getUniqueId()))
                {
                    this.bossbarmap.put(player.getUniqueId(), BossBar.bossBar(Component.text("@{Azisaba.Vanilife.Objective.BossBar}"), 0.0f, BossBar.Color.PINK, BossBar.Overlay.PROGRESS));
                }

                BossBar bossBar = this.bossbarmap.get(player.getUniqueId());

                bossBar.name(Component.text("Objective: ").color(NamedTextColor.WHITE)
                        .append(objective.getName(Language.getInstance(user)).color(NamedTextColor.YELLOW))
                        .append(Component.text(String.format(" [%s Mola]", objective.getReward())).color(NamedTextColor.GRAY)));
                bossBar.progress((float) user.getAchievements().size() / Objectives.registry.values().size());
                bossBar.addViewer(player);
            }
            else
            {
                BossBar bossBar = this.bossbarmap.get(user.getId());

                if (bossBar != null)
                {
                    bossBar.removeViewer(player);
                    this.bossbarmap.remove(user.getId());
                }
            }
        });
    }
}
