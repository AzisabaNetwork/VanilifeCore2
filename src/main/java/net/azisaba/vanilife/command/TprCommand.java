package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.TeleportRequest;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.util.Watch;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TprCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /tpr <player>").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (this.cooldowns.containsKey(user))
        {
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            sender.sendMessage(Language.translate("msg.cooldown", player, "remaining=" + this.cooldowns.get(user)).color(NamedTextColor.RED));
            return true;
        }

        if (Bukkit.getPlayerExact(args[0]) == null)
        {
            sender.sendMessage(Language.translate("msg.offline", player, "name=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        Player toPlayer = Bukkit.getPlayer(args[0]);
        User toUser = User.getInstance(toPlayer);

        if (player == toPlayer)
        {
            sender.sendMessage(Language.translate("cmd.tpr.cant-yourself", player).color(NamedTextColor.RED));
            return true;
        }

        if (toUser.isBlock(User.getInstance(player)))
        {
            sender.sendMessage(Language.translate("cmd.tpr.cant", player).color(NamedTextColor.RED));
            return true;
        }

        if (! UserUtility.isAdmin(sender) && toUser.getRequests().stream().anyMatch(r -> r.match(TeleportRequest.class, player)))
        {
            sender.sendMessage(Language.translate("cmd.tpr.already", player, "name=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        new TeleportRequest(player, toPlayer);

        this.cooldowns.put(user, 6);

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
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> ! Watch.isWatcher(p))
                    .filter(p -> p.getName().startsWith(args[0]))
                    .forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
