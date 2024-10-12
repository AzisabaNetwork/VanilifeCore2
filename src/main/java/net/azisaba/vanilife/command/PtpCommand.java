package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PtpCommand implements CommandExecutor, TabCompleter
{
    private final Map<User, Integer> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + label + " <plot>").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (this.cooldowns.containsKey(user))
        {
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            sender.sendMessage(Language.translate("msg.cooldown", player, "remaining=" + this.cooldowns.get(user)).color(NamedTextColor.RED));
            return true;
        }

        Plot plot = Plot.getInstance(args[0]);

        if (plot == null)
        {
            sender.sendMessage(Language.translate("cmd.plot.not-found", player).color(NamedTextColor.RED));
            return true;
        }

        if (! plot.isMember(player))
        {
            sender.sendMessage(Language.translate("cmd.ptp.permission-error", player));
            return true;
        }

        Location spawn = plot.getSpawn();
        player.teleport(spawn);
        player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.2f);

        this.cooldowns.put(user, 8);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                int remaining = cooldowns.get(user) - 1;

                if (remaining <= 0)
                {
                    cooldowns.remove(user);
                    this.cancel();
                    return;
                }

                cooldowns.put(user, remaining);
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0L, 20L);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length != 1)
        {
            return List.of();
        }

        if (! (sender instanceof Player player))
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();

        for (Plot plot : Plot.getInstances().stream().filter(p -> p.isMember(player)).toList())
        {
            if (! plot.getName().startsWith(args[0])) continue;
            suggest.add(plot.getName());
        }

        return suggest;
    }
}
