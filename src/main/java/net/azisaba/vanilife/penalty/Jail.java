package net.azisaba.vanilife.penalty;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.TrustRank;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Afk;
import net.azisaba.vanilife.util.UserUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Jail
{
    private static final List<Jail> instances = new ArrayList<>();

    public static Jail getInstance(@NotNull User target)
    {
        List<Jail> filteredInstances = Jail.instances.stream().filter(i -> i.getTarget().equals(target)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    private final List<Player> electors;
    private final List<Player> voters = new ArrayList<>();

    private final User target;
    private final String details;

    public Jail(@NotNull User target, @NotNull String details)
    {
        this.target = target;
        this.details = details;

        Jail.instances.add(this);

        this.electors = new ArrayList<>(Bukkit.getOnlinePlayers().stream()
                .filter(player -> ! Afk.isAfk(player) && ! player.getUniqueId().equals(this.target.getId()) && TrustRank.NEW.getLevel() <= User.getInstance(player).getTrustRank().getLevel())
                .collect(Collectors.toMap(
                        player -> Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress(),
                        player -> player,
                        (existing, replacement) -> existing
                )).values());

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), this::onEnd, this.getTicks());

        this.electors.forEach(elector -> {
            elector.playSound(elector, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.1f);
            elector.showTitle(Title.title(Language.translate("jail.title", elector).color(NamedTextColor.RED),
                    target.getName().append(Component.text(CLI.getSpaces(1))).append(Language.translate("jail.subtitle", elector).color(NamedTextColor.YELLOW))));

            elector.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.RED));
            elector.sendMessage(Component.text(CLI.getSpaces(12)).append(Language.translate("jail.title", elector).color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
            elector.sendMessage(Component.text().build());
            elector.sendMessage(Language.translate("jail.controller.details.1", elector, "target=" + this.target.getPlaneName()));
            elector.sendMessage(Language.translate("jail.controller.details.2", elector, "limit=" + (this.getTicks() / 20L)));
            elector.sendMessage(Language.translate("jail.controller.vote", elector)
                    .color(NamedTextColor.GOLD)
                    .hoverEvent(HoverEvent.showText(Language.translate("jail.controller.vote.hover", elector)))
                    .clickEvent(ClickEvent.runCommand("/jail " + this.target.getPlaneName())));
            elector.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.RED));
        });
    }

    public User getTarget()
    {
        return this.target;
    }

    public String getDetails()
    {
        return this.details;
    }

    private long getTicks()
    {
        return 20L * 60;
    }

    public boolean isVoted(@NotNull User user)
    {
        return this.voters.stream().anyMatch(voter -> voter.getUniqueId().equals(user.getId()));
    }

    private void onEnd()
    {
        if (! Jail.instances.contains(this))
        {
            this.electors.forEach(elector -> elector.sendMessage(Language.translate("jail.time-over", elector).color(NamedTextColor.RED)));
            return;
        }

        Jail.instances.remove(this);

        if (this.voters.size() <= this.electors.size() / 2)
        {
            return;
        }

        this.jail();
    }

    public void vote(@NotNull Player voter)
    {
        if (! this.electors.contains(voter) || this.isVoted(User.getInstance(voter)) || voter == this.target)
        {
            return;
        }

        if (UserUtility.isModerator(voter))
        {
            Jail.instances.remove(this);
            this.jail();
            return;
        }

        this.voters.add(voter);

        if (this.electors.size() / 2 < this.voters.size())
        {
            this.onEnd();
        }
    }

    private void jail()
    {
        this.target.jail();

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> ! player.getUniqueId().equals(this.target.getId()))
                .forEach(player -> player.sendMessage(Language.translate("jail.jailed", player).color(NamedTextColor.RED)));

        StringBuilder voters = new StringBuilder();
        this.voters.forEach(voter -> voters.append(String.format("%s (%s)\n", voter.getName(), voter.getUniqueId())));

        TextChannel channel = this.voters.stream().anyMatch(UserUtility::isModerator) ? Vanilife.CHANNEL_HISTORY : Vanilife.CHANNEL_CONSOLE;

        channel.sendMessageEmbeds(new EmbedBuilder()
                .setTitle("Jail 通知")
                .addField("対象者", String.format("%s (%s)", this.target.getPlaneName(), this.target.getId()), false)
                .addField("投票者", voters.toString(), false)
                .addField("詳細", this.details, false)
                .build()).queue();

        if (channel == Vanilife.CHANNEL_CONSOLE)
        {
            Vanilife.CHANNEL_CONSOLE.sendMessage(":oncoming_police_car: " + Vanilife.ROLE_SUPPORT.getAsMention() + " 投票による Jail の実行がありました").queue();
        }
    }
}
