package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RtpCommand implements CommandExecutor, TabCompleter
{
    private final Map<User, Integer> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /rtp").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (this.cooldowns.containsKey(user))
        {
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            sender.sendMessage(Language.translate("msg.cooldown", player, "remaining=" + this.cooldowns.get(user)).color(NamedTextColor.RED));
            return true;
        }

        if (user.getMola() < Vanilife.MOLA_RTP)
        {
            player.sendMessage(Language.translate("msg.shortage", player, "need=" + (Vanilife.MOLA_RTP - user.getMola())).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            return true;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(player.getWorld());

        if (world == null)
        {
            sender.sendMessage(Language.translate("cmd.rtp.not-found", player).color(NamedTextColor.RED));
            return true;
        }

        user.setMola(user.getMola() - Vanilife.MOLA_RTP);
        sender.sendMessage(Language.translate("cmd.rtp.searching", player).color(NamedTextColor.GREEN));
        world.getTeleporter().teleport(player);

        this.cooldowns.put(user, 15);

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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
    {
        return List.of();
    }
}
