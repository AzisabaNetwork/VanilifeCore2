package net.azisaba.vanilife.housing;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.util.Afk;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HousingAfkRunnable extends BukkitRunnable
{
    protected static final List<Player> afk = new ArrayList<>();

    @Override
    public void run()
    {
        for (Player afk : new ArrayList<>(HousingAfkRunnable.afk))
        {
            if (!Afk.isAfk(afk))
            {
                HousingAfkRunnable.afk.remove(afk);
                continue;
            }

            afk.showTitle(Title.title(Language.translate("housing.afk", afk).color(NamedTextColor.RED),
                    Language.translate("housing.afk.details", afk).color(NamedTextColor.YELLOW),
                    Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(2), Duration.ofSeconds(0))));
        }
    }
}
