package net.azisaba.vanilife.nkip;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.housing.HousingTime;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.TitleUI;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.Afk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NkipVote
{
    private static final List<NkipVote> instances = new ArrayList<>();

    public static NkipVote getInstance(@NotNull World world)
    {
        return NkipVote.instances.stream().filter(i -> i.getWorld().equals(world)).findFirst().orElse(null);
    }

    private final World world;

    private final List<Player> electors;
    private final List<Player> voters = new ArrayList<>();

    public NkipVote(@NotNull World world)
    {
        this.world = world;
        this.electors = this.world.getPlayers().stream().filter(p -> ! Afk.isAfk(p) && User.getInstance(p).getStatus() == UserStatus.DEFAULT).toList();

        this.electors.forEach(elector -> {
            elector.playSound(elector, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.1f);

            elector.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.DARK_PURPLE));
            elector.sendMessage(Language.translate("msg.nkip", elector).color(NamedTextColor.YELLOW));
            elector.sendMessage(Language.translate("msg.nkip.click-to-vote", elector)
                    .color(NamedTextColor.GOLD)
                    .hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-run", elector, "command=/nkip " + this.world.getName())))
                    .clickEvent(ClickEvent.runCommand("/nkip " + this.world.getName())));
            elector.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.DARK_PURPLE));
        });

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), this::tally, 20L * 60);
        NkipVote.instances.add(this);
    }

    public World getWorld()
    {
        return this.world;
    }

    public void vote(@NotNull Player player)
    {
        this.voters.add(player);
    }

    public void tally()
    {
        NkipVote.instances.remove(this);

        if (this.voters.size() < (double) this.electors.size() / 2)
        {
            this.world.getPlayers().stream()
                    .filter(p -> ! Afk.isAfk(p))
                    .forEach(p -> TitleUI.typing(p, (TextComponent) Language.translate("msg.nkip.cancelled", p), 5L));

            return;
        }

        this.world.setTime(HousingTime.DAY.getTime());
        this.world.getPlayers().stream()
                .filter(p -> ! Afk.isAfk(p))
                .forEach(p -> {
                    TitleUI.typing(p, (TextComponent) Language.translate("msg.nkip.skipped", p), 5L);
                    p.setStatistic(Statistic.TIME_SINCE_REST, 0);
                });
    }
}
