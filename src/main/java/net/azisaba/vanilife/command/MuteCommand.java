package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.util.Watch;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MuteCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! UserUtility.isAdmin(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /mute <player>").color(NamedTextColor.RED));
            return true;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                UUID uuid = Bukkit.getPlayerUniqueId(args[0]);

                if (uuid == null)
                {
                    sender.sendMessage(Component.text(args[0] + " は不明なプレイヤーです").color(NamedTextColor.RED));
                    return;
                }

                if (! UserUtility.exists(uuid))
                {
                    sender.sendMessage(Component.text(args[0] + " は不明なユーザーです").color(NamedTextColor.RED));
                    return;
                }

                User user = User.getInstance(args[0]);

                if (user.getStatus() == UserStatus.MUTED)
                {
                    sender.sendMessage(Component.text(String.format("%s は既にミュートされています", args[0])).color(NamedTextColor.RED));
                    return;
                }

                user.setTrust(user.getTrust() - 10);
                user.setStatus(UserStatus.MUTED);
                sender.sendMessage(Component.text(String.format("%s をミュートしました", args[0])).color(NamedTextColor.GREEN));
            }
        }.runTaskAsynchronously(Vanilife.getPlugin());

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
                    .forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
