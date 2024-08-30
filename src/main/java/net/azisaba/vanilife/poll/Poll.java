package net.azisaba.vanilife.poll;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Poll
{
    private static final List<Poll> instances = new ArrayList<>();

    public static Poll getInstance(UUID id)
    {
        List<Poll> filteredInstances = Poll.instances.stream().filter(i -> i.getId().equals(id)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static Poll getInstance(Player owner)
    {
        List<Poll> filteredInstances = Poll.instances.stream().filter(i -> i.getOwner() == owner).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    private final UUID id;
    private final Player owner;
    private final boolean multiple;
    private final boolean anonymity;
    private final int limit;
    private final List<VoteOption> options = new ArrayList<>();

    public Poll(Player owner, boolean multiple, boolean anonymity, int limit, String... options)
    {
        this.id = UUID.randomUUID();
        this.owner = owner;
        this.multiple = multiple;
        this.anonymity = anonymity;
        this.limit = limit;

        Arrays.stream(options).forEach(o -> this.options.add(new VoteOption(o)));

        Poll.instances.add(this);

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (User.getInstance(player).isBlock(User.getInstance(this.owner)) || User.getInstance(this.owner).isBlock(User.getInstance(player)))
            {
                continue;
            }

            player.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.LIGHT_PURPLE));
            player.sendMessage(Component.text(CLI.getSpaces(1) + "VOTE").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
            player.sendMessage(Component.text(CLI.getSpaces(2)).append(this.owner.displayName()).append(Component.text(" さんが投票を開始しました！")).decorate(TextDecoration.BOLD));
            player.sendMessage(Component.text(CLI.getSpaces(2) + (this.multiple ? "複数回答可" : "複数回答不可")).color(this.multiple ? NamedTextColor.GREEN : NamedTextColor.RED).append(Component.text(CLI.getSpaces(2) + (this.anonymity ? "匿名" : "実名")).color(this.anonymity ? NamedTextColor.GREEN : NamedTextColor.RED)).append(Component.text(String.format("  %s 秒後に終了", this.limit)).color(NamedTextColor.YELLOW)));
            player.sendMessage(Component.text());
            player.sendMessage(Component.text(CLI.getSpaces(1) + "OPTIONS:").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));

            for (VoteOption option : this.options)
            {
                player.sendMessage(Component.text(CLI.getSpaces(2) + option.getName()).color(NamedTextColor.YELLOW).clickEvent(ClickEvent.runCommand(String.format("/vote %s %s", this.id.toString(), option.getName()))).hoverEvent(HoverEvent.showText(Component.text(String.format("クリックして %s に投票", option.getName())))));
            }

            player.sendMessage(Component.text());
            player.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.LIGHT_PURPLE));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                onEnd();
            }
        }.runTaskLater(Vanilife.getPlugin(), 20L * this.limit);
    }

    public UUID getId()
    {
        return this.id;
    }

    public Player getOwner()
    {
        return this.owner;
    }

    public void onEnd()
    {
        List<VoteOption> ranking = new ArrayList<>();

        while (! this.options.isEmpty())
        {
            VoteOption rate = null;

            for (VoteOption option : this.options)
            {
                if (rate == null || rate.getVoters().size() < option.getVoters().size())
                {
                    rate = option;
                }
            }

            this.options.remove(rate);
            ranking.add(rate);
        }

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (User.getInstance(player).isBlock(User.getInstance(this.owner)) || User.getInstance(this.owner).isBlock(User.getInstance(player)))
            {
                continue;
            }

            player.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.LIGHT_PURPLE));
            player.sendMessage(Component.text(CLI.getSpaces(1) + "投票の終了").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
            player.sendMessage(Component.text());

            for (int i = 0; i < ranking.size(); i ++)
            {
                VoteOption option = ranking.get(i);

                player.sendMessage(Component.text(CLI.getSpaces(2) + (i + 1) + "位 ").color((i == 0) ? NamedTextColor.GOLD : NamedTextColor.GRAY).append(Component.text(option.getName()).color(NamedTextColor.WHITE).append(Component.text(String.format(" (%S 票)", option.getVoters().size())).color(NamedTextColor.DARK_GRAY))));

                if (! this.anonymity)
                {
                    StringBuilder sb = new StringBuilder();
                    option.getVoters().forEach(v -> sb.append(v.getName()).append(" "));
                    player.sendMessage(Component.text(CLI.getSpaces(3) + sb.toString()).color(NamedTextColor.GRAY));
                }
            }

            player.sendMessage(Component.text());
            player.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.LIGHT_PURPLE));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        }

        Poll.instances.remove(this);
    }

    public void vote(Player player, String option)
    {
        VoteOption voteOption = this.options.stream().filter(o -> o.getName().equals(option)).toList().getFirst();

        if (voteOption == null)
        {
            player.sendMessage(Component.text(option + " は未定義の投票オプションです").color(NamedTextColor.RED));
            return;
        }

        if (voteOption.isVoter(player))
        {
            voteOption.unVote(player);
            player.sendMessage(Component.text(option + " のへ投票をキャンセルしました").color(NamedTextColor.GREEN));
            return;
        }

        if (! this.multiple)
        {
            this.options.stream().filter(o -> o.isVoter(player)).forEach(o -> o.unVote(player));
        }

        voteOption.vote(player);
        player.sendMessage(Component.text("投票先: ").color(NamedTextColor.GRAY).append(Component.text(option).color(NamedTextColor.GREEN)));
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.4f);
    }
}
